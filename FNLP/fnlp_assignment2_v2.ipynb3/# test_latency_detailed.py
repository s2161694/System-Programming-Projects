# test_latency_detailed.py
import time
import statistics
from web3 import Web3
import pandas as pd
from datetime import datetime

class LatencyAnalyzer:
    def __init__(self, rpc_url):
        self.w3 = Web3(Web3.HTTPProvider(rpc_url))
        self.results = []
        
    def measure_latency(self, method_name, method_func, iterations=50):
        """测量特定方法的延迟"""
        print(f"\n测试方法: {method_name}")
        print("-" * 40)
        
        latencies = []
        errors = 0
        
        for i in range(iterations):
            try:
                start = time.perf_counter()  # 高精度计时器
                result = method_func()
                end = time.perf_counter()
                
                latency_ms = (end - start) * 1000
                latencies.append(latency_ms)
                
                # 实时显示
                if i % 10 == 0:
                    print(f"  测试 {i+1}/{iterations}: {latency_ms:.2f}ms")
                    
            except Exception as e:
                errors += 1
                print(f"  错误: {e}")
        
        if latencies:
            # 统计分析
            stats = {
                'method': method_name,
                'avg': statistics.mean(latencies),
                'median': statistics.median(latencies),
                'min': min(latencies),
                'max': max(latencies),
                'stdev': statistics.stdev(latencies) if len(latencies) > 1 else 0,
                'p95': sorted(latencies)[int(len(latencies) * 0.95)],  # 95分位
                'errors': errors
            }
            
            print(f"\n📊 统计结果:")
            print(f"  平均延迟: {stats['avg']:.2f}ms")
            print(f"  中位数: {stats['median']:.2f}ms")
            print(f"  最小/最大: {stats['min']:.2f}ms / {stats['max']:.2f}ms")
            print(f"  标准差: {stats['stdev']:.2f}ms")
            print(f"  P95: {stats['p95']:.2f}ms (95%请求快于此)")
            print(f"  错误率: {errors}/{iterations}")
            
            self.results.append(stats)
            return stats
        
    def run_full_test(self):
        """运行完整测试套件"""
        print("🚀 开始全面延迟测试")
        print("=" * 50)
        
        # 测试不同的RPC方法
        tests = [
            ("获取区块号", lambda: self.w3.eth.block_number),
            ("获取Gas价格", lambda: self.w3.eth.gas_price),
            ("获取账户余额", lambda: self.w3.eth.get_balance("0x0000000000000000000000000000000000000000")),
            ("获取区块信息", lambda: self.w3.eth.get_block('latest')),
            ("获取交易数", lambda: self.w3.eth.get_transaction_count("0x0000000000000000000000000000000000000000"))
        ]
        
        for test_name, test_func in tests:
            self.measure_latency(test_name, test_func, iterations=30)
            time.sleep(0.5)  # 避免限流
        
        self.generate_report()
    
    def generate_report(self):
        """生成测试报告"""
        print("\n" + "=" * 50)
        print("📈 延迟测试总结报告")
        print("=" * 50)
        
        df = pd.DataFrame(self.results)
        
        # 判断是否满足MEV要求
        avg_latency = df['avg'].mean()
        
        if avg_latency < 50:
            print(f"✅ 平均延迟 {avg_latency:.2f}ms < 50ms - 适合MEV!")
        else:
            print(f"⚠️ 平均延迟 {avg_latency:.2f}ms > 50ms - 需要优化!")
        
        print("\n详细数据:")
        print(df.to_string(index=False))
        
        # 保存报告
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        filename = f"latency_report_{timestamp}.csv"
        df.to_csv(filename, index=False)
        print(f"\n报告已保存到: {filename}")

# 运行测试
analyzer = LatencyAnalyzer("https://aged-morning-meadow.xdai.quiknode.pro/7c708ef0c025d503b13b8ad5f172b1fee0fc7acb/")
analyzer.run_full_test()