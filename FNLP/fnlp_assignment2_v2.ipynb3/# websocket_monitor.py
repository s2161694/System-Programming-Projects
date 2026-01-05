# test_websocket_basic.py
import asyncio
import json
from web3 import Web3
from web3.providers import WebSocketProvider  # 注意大写的S！
import time
from datetime import datetime

def test_websocket_connection():
    """
    测试WebSocket连接是否正常
    注意：QuickNode的WebSocket URL格式：
    wss://xxx-xxx-xxx.xdai.quiknode.pro/your-api-key/
    """
    
    # 替换为你的WebSocket URL
    WS_URL = "wss://aged-morning-meadow.xdai.quiknode.pro/7c708ef0c025d503b13b8ad5f172b1fee0fc7acb/"
    
    print("=" * 50)
    print("WebSocket连接测试")
    print("=" * 50)
    
    try:
        # 创建WebSocket连接
        w3 = Web3(WebSocketProvider(WS_URL))
        
        # 测试连接
        if w3.is_connected():
            print("✅ WebSocket连接成功！")
            
            # 获取一些基本信息
            block_number = w3.eth.block_number
            gas_price = w3.eth.gas_price
            chain_id = w3.eth.chain_id
            
            print(f"📊 链信息:")
            print(f"  - 链ID: {chain_id} (Gnosis Chain)")
            print(f"  - 当前区块: {block_number:,}")
            print(f"  - Gas价格: {w3.from_wei(gas_price, 'gwei')} Gwei")
            
            return True
        else:
            print("❌ WebSocket连接失败")
            return False
            
    except Exception as e:
        print(f"❌ 连接错误: {e}")
        print("\n可能的原因:")
        print("1. WebSocket URL格式不正确")
        print("2. QuickNode没有启用WebSocket")
        print("3. 网络防火墙阻止了WSS连接")
        return False

# 运行测试
if __name__ == "__main__":
    test_websocket_connection()