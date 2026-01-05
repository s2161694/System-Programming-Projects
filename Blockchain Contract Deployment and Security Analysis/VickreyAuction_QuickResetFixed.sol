// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

/**
 * @title VickreyAuction - Quick Reset Fixed Version
 * @notice Sealed-bid second-price auction contract for ERC721 NFTs
 * @dev FIX: Can create new auction immediately after finalization
 */

interface IERC721 {
    function ownerOf(uint256 tokenId) external view returns (address);
    function transferFrom(address from, address to, uint256 tokenId) external;
    function safeTransferFrom(address from, address to, uint256 tokenId) external;
}

contract VickreyAuction {
    
    // NFT contract address on Sepolia (provided by assignment)
    IERC721 public constant nftContract = IERC721(0x1546Bd67237122754D3F0cB761c139f81388b210);
    
    // Auction state
    struct Auction {
        address seller;
        uint256 tokenId;
        uint256 reservePrice;
        uint256 biddingDeadline;
        uint256 revealDeadline;
        bool finalized;
        bool exists;
    }
    
    // Bid information
    struct Bid {
        bytes32 commitment;  // Hash of (bidAmount, nonce)
        uint256 deposit;     // ETH deposited
        bool revealed;
        uint256 actualBid;
        uint256 revealTime;  // For tie-breaking: earlier reveal wins
    }
    
    // Current auction
    Auction public currentAuction;
    
    // Bidder data
    mapping(address => Bid) public bids;
    address[] private bidders;  // Track all bidders for cleanup
    
    // Refund management
    mapping(address => uint256) public pendingReturns;
    
    // Reentrancy guard
    uint256 private locked = 0;
    
    modifier noReentrancy() {
        require(locked == 0, "No reentrancy");
        locked = 1;
        _;
        locked = 0;
    }
    
    // Events
    event AuctionCreated(
        address indexed seller, 
        uint256 indexed tokenId, 
        uint256 reservePrice,
        uint256 biddingDeadline,
        uint256 revealDeadline
    );
    event BidCommitted(address indexed bidder, uint256 deposit);
    event BidRevealed(address indexed bidder, uint256 amount);
    event AuctionFinalized(address indexed winner, uint256 pricePaid, uint256 indexed tokenId);
    event AuctionCancelled(uint256 indexed tokenId);
    event RefundClaimed(address indexed bidder, uint256 amount);
    
    /**
     * @notice Create a new auction
     * @param _tokenId The NFT token ID to auction
     * @param _reservePrice Minimum acceptable price in wei
     * @param _biddingDuration Duration of bidding phase in seconds
     * @param _revealDuration Duration of reveal phase in seconds
     */
    function createAuction(
        uint256 _tokenId,
        uint256 _reservePrice,
        uint256 _biddingDuration,
        uint256 _revealDuration
    ) external {
        // ✨ FIX: Improved check - can create new auction immediately after finalization
        if (currentAuction.exists) {
            require(currentAuction.finalized, "Previous auction not finalized");
            // Clean up previous auction data
            _cleanupBids();
            delete currentAuction;
        }
        
        // Validate inputs
        require(_reservePrice > 0, "Reserve must be > 0");
        require(_biddingDuration >= 60, "Bidding too short");
        require(_revealDuration >= 60, "Reveal too short");
        
        // Verify NFT ownership
        require(nftContract.ownerOf(_tokenId) == msg.sender, "Not token owner");
        
        // Transfer NFT to contract (seller must approve first)
        nftContract.transferFrom(msg.sender, address(this), _tokenId);
        
        // Verify transfer was successful
        require(nftContract.ownerOf(_tokenId) == address(this), "NFT transfer failed");
        
        // Create new auction
        uint256 biddingDeadline = block.timestamp + _biddingDuration;
        uint256 revealDeadline = biddingDeadline + _revealDuration;
        
        currentAuction = Auction({
            seller: msg.sender,
            tokenId: _tokenId,
            reservePrice: _reservePrice,
            biddingDeadline: biddingDeadline,
            revealDeadline: revealDeadline,
            finalized: false,
            exists: true
        });
        
        emit AuctionCreated(msg.sender, _tokenId, _reservePrice, biddingDeadline, revealDeadline);
    }
    
    /**
     * @notice Commit a bid during bidding phase
     * @param _commitment Hash of (bidAmount, nonce) generated off-chain
     * @dev Bidder must send ETH >= their intended bid amount as deposit
     */
    function commitBid(bytes32 _commitment) external payable {
        require(currentAuction.exists, "No active auction");
        require(block.timestamp <= currentAuction.biddingDeadline, "Bidding ended");
        require(msg.value >= currentAuction.reservePrice, "Deposit < reserve");
        require(bids[msg.sender].commitment == bytes32(0), "Already committed");
        require(msg.sender != currentAuction.seller, "Seller cannot bid");
        require(_commitment != bytes32(0), "Invalid commitment");
        
        bids[msg.sender] = Bid({
            commitment: _commitment,
            deposit: msg.value,
            revealed: false,
            actualBid: 0,
            revealTime: 0
        });
        
        bidders.push(msg.sender);
        
        emit BidCommitted(msg.sender, msg.value);
    }
    
    /**
     * @notice Reveal a committed bid during reveal phase
     * @param _bidAmount The actual bid amount (must be <= deposit)
     * @param _nonce Random nonce used in commitment
     */
    function revealBid(uint256 _bidAmount, uint256 _nonce) external {
        require(currentAuction.exists, "No active auction");
        require(block.timestamp > currentAuction.biddingDeadline, "Still in bidding");
        require(block.timestamp <= currentAuction.revealDeadline, "Reveal ended");
        require(!currentAuction.finalized, "Auction finalized");
        
        Bid storage bid = bids[msg.sender];
        require(bid.commitment != bytes32(0), "No bid found");
        require(!bid.revealed, "Already revealed");
        
        // Verify commitment
        bytes32 computedHash = keccak256(abi.encodePacked(_bidAmount, _nonce));
        require(computedHash == bid.commitment, "Invalid reveal");
        require(_bidAmount <= bid.deposit, "Bid exceeds deposit");
        
        // Mark as revealed
        bid.revealed = true;
        bid.actualBid = _bidAmount;
        bid.revealTime = block.timestamp;
        
        // Calculate refund (deposit - actualBid)
        uint256 excess = bid.deposit - _bidAmount;
        if (excess > 0) {
            pendingReturns[msg.sender] += excess;
        }
        
        emit BidRevealed(msg.sender, _bidAmount);
    }
    
    /**
     * @notice Finalize auction after reveal phase ends
     * @dev Anyone can call this. Determines winner and transfers NFT/payment
     */
    function finalizeAuction() external noReentrancy {
        require(currentAuction.exists, "No active auction");
        require(block.timestamp > currentAuction.revealDeadline, "Reveal not ended");
        require(!currentAuction.finalized, "Already finalized");
        
        currentAuction.finalized = true;
        
        // Find winner and second price
        (address winner, uint256 highestBid, uint256 secondHighestBid) = _determineWinner();
        
        if (winner != address(0)) {
            // Valid winner found
            uint256 paymentAmount = secondHighestBid >= currentAuction.reservePrice 
                ? secondHighestBid 
                : currentAuction.reservePrice;
            
            // Refund winner's excess (highestBid - paymentAmount)
            uint256 winnerRefund = highestBid - paymentAmount;
            if (winnerRefund > 0) {
                pendingReturns[winner] += winnerRefund;
            }
            
            // Refund all losing bids
            for (uint i = 0; i < bidders.length; i++) {
                address bidder = bidders[i];
                if (bidder != winner && bids[bidder].revealed && bids[bidder].actualBid >= currentAuction.reservePrice) {
                    pendingReturns[bidder] += bids[bidder].actualBid;
                }
            }
            
            // Transfer NFT to winner
            nftContract.transferFrom(address(this), winner, currentAuction.tokenId);
            
            // Pay seller
            (bool success, ) = currentAuction.seller.call{value: paymentAmount}("");
            require(success, "Payment to seller failed");
            
            emit AuctionFinalized(winner, paymentAmount, currentAuction.tokenId);
        } else {
            // No valid bids, return NFT to seller
            nftContract.transferFrom(address(this), currentAuction.seller, currentAuction.tokenId);
            emit AuctionCancelled(currentAuction.tokenId);
        }
        
        // Refund unrevealed and below-reserve bids
        _refundInvalidBids();
    }
    
    /**
     * @notice Withdraw pending refunds
     */
    function withdraw() external noReentrancy {
        uint256 amount = pendingReturns[msg.sender];
        require(amount > 0, "Nothing to withdraw");
        
        pendingReturns[msg.sender] = 0;
        
        (bool success, ) = msg.sender.call{value: amount}("");
        require(success, "Withdraw failed");
        
        emit RefundClaimed(msg.sender, amount);
    }
    
    /**
     * @notice Allow bidders to withdraw if they didn't reveal after deadline
     * @dev Can be called after reveal phase ends
     */
    function withdrawUnrevealedBid() external noReentrancy {
        require(currentAuction.exists, "No auction");
        require(block.timestamp > currentAuction.revealDeadline, "Reveal not ended");
        
        Bid storage bid = bids[msg.sender];
        require(bid.commitment != bytes32(0), "No bid found");
        require(!bid.revealed, "Already revealed");
        
        uint256 refund = bid.deposit;
        require(refund > 0, "Nothing to refund");
        
        // Mark as processed
        bid.deposit = 0;
        
        (bool success, ) = msg.sender.call{value: refund}("");
        require(success, "Refund failed");
        
        emit RefundClaimed(msg.sender, refund);
    }
    
    // ========== INTERNAL FUNCTIONS ==========
    
    /**
     * @dev Determine auction winner using valid revealed bids
     * @return winner Address of highest bidder
     * @return highestBid Highest bid amount
     * @return secondHighestBid Second highest bid amount
     */
    function _determineWinner() internal view returns (
        address winner,
        uint256 highestBid,
        uint256 secondHighestBid
    ) {
        winner = address(0);
        highestBid = 0;
        secondHighestBid = 0;
        uint256 earliestRevealTime = type(uint256).max;
        
        for (uint i = 0; i < bidders.length; i++) {
            address bidder = bidders[i];
            Bid memory bid = bids[bidder];
            
            // Only consider revealed bids >= reserve
            if (!bid.revealed || bid.actualBid < currentAuction.reservePrice) {
                continue;
            }
            
            if (bid.actualBid > highestBid) {
                // New highest bid
                secondHighestBid = highestBid;
                highestBid = bid.actualBid;
                winner = bidder;
                earliestRevealTime = bid.revealTime;
            } else if (bid.actualBid == highestBid) {
                // Tie: earlier reveal wins
                if (bid.revealTime < earliestRevealTime) {
                    secondHighestBid = highestBid; // Previous winner becomes second
                    winner = bidder;
                    earliestRevealTime = bid.revealTime;
                } else {
                    // Current winner keeps position, this bid becomes second
                    secondHighestBid = bid.actualBid;
                }
            } else if (bid.actualBid > secondHighestBid) {
                // Update second highest
                secondHighestBid = bid.actualBid;
            }
        }
        
        return (winner, highestBid, secondHighestBid);
    }
    
    /**
     * @dev Refund unrevealed and below-reserve bids
     */
    function _refundInvalidBids() internal {
        for (uint i = 0; i < bidders.length; i++) {
            address bidder = bidders[i];
            Bid memory bid = bids[bidder];
            
            if (!bid.revealed) {
                // Unrevealed: refund full deposit
                pendingReturns[bidder] += bid.deposit;
            } else if (bid.actualBid < currentAuction.reservePrice) {
                // Below reserve: refund actual bid
                pendingReturns[bidder] += bid.actualBid;
            }
        }
    }
    
    /**
     * @dev Clean up bid data from previous auction
     */
    function _cleanupBids() internal {
        for (uint i = 0; i < bidders.length; i++) {
            delete bids[bidders[i]];
        }
        delete bidders;
    }
    
    // ========== VIEW FUNCTIONS ==========
    
    /**
     * @notice Get current auction information
     */
    function getAuctionInfo() external view returns (
        address seller,
        uint256 tokenId,
        uint256 reservePrice,
        uint256 biddingDeadline,
        uint256 revealDeadline,
        bool finalized,
        bool exists
    ) {
        return (
            currentAuction.seller,
            currentAuction.tokenId,
            currentAuction.reservePrice,
            currentAuction.biddingDeadline,
            currentAuction.revealDeadline,
            currentAuction.finalized,
            currentAuction.exists
        );
    }
    
    /**
     * @notice Get bid information for a specific bidder
     */
    function getBidInfo(address _bidder) external view returns (
        bytes32 commitment,
        uint256 deposit,
        bool revealed,
        uint256 actualBid
    ) {
        Bid memory bid = bids[_bidder];
        return (bid.commitment, bid.deposit, bid.revealed, bid.actualBid);
    }
    
    /**
     * @notice Generate commitment hash off-chain helper
     * @param _bidAmount Your bid amount in wei
     * @param _nonce Random number for privacy
     */
    function generateCommitment(uint256 _bidAmount, uint256 _nonce) 
        external 
        pure 
        returns (bytes32) 
    {
        return keccak256(abi.encodePacked(_bidAmount, _nonce));
    }
    
    /**
     * @notice Get number of bidders
     */
    function getBidderCount() external view returns (uint256) {
        return bidders.length;
    }
    
    /**
     * @notice Check contract's ETH balance
     */
    function getContractBalance() external view returns (uint256) {
        return address(this).balance;
    }
}
