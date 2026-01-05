import json
import requests
import time

class RPCTester:
    def __init__(self, rpc_url):
        self.rpc_url = rpc_url
        self.session = requests.Session()
        
    def test_basic_connection(self):
        """测试基础连接"""
        print("="*50)
        print("测试1: 基础连接测试")
        print("="*50)
        
        # 构造JSON-RPC请求
        payload = {
            "jsonrpc": "2.0",  # JSON-RPC版本
            "method": "eth_blockNumber",  # 获取最新区块号
            "params": [],  # 无参数
            "id": 1  # 请求ID
        }
        
        try:
            response = self.session.post(
                self.rpc_url,
                json=payload,
                timeout=5
            )
            
            if response.status_code == 200:
                data = response.json()
                block_hex = data['result']  # 返回16进制
                block_num = int(block_hex, 16)  # 转10进制
                print(f"✅ 连接成功!")
                print(f"   当前区块高度: {block_num}")
                print(f"   原始返回: {block_hex}")
                return True
            else:
                print(f"❌ 连接失败: HTTP {response.status_code}")
                return False
                
        except Exception as e:
            print(f"❌ 连接错误: {e}")
            return False

# 使用示例
tester = RPCTester("https://aged-morning-meadow.xdai.quiknode.pro/7c708ef0c025d503b13b8ad5f172b1fee0fc7acb/")
tester.test_basic_connection()