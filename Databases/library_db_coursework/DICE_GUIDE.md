# DICE测试指南

## 📤 第1步：上传文件到DICE

### 方法A：使用SCP（命令行）

在Windows PowerShell或CMD中：
```bash
# 上传单个文件
scp sql_3.txt YOUR_USERNAME@student.ssh.inf.ed.ac.uk:~/

# 上传整个文件夹
scp -r library_db_coursework YOUR_USERNAME@student.ssh.inf.ed.ac.uk:~/
```

### 方法B：使用WinSCP（图形界面）

1. 下载并安装 WinSCP：https://winscp.net/
2. 连接设置：
   - 主机名：`student.ssh.inf.ed.ac.uk`
   - 用户名：你的DICE用户名
   - 密码：你的DICE密码
3. 连接后，直接拖拽文件上传

### 方法C：在DICE上直接创建

1. SSH连接到DICE
2. 创建文件夹和文件
3. 从Windows复制粘贴内容

---

## 🔐 第2步：连接到DICE

```bash
# 使用SSH连接
ssh YOUR_USERNAME@student.ssh.inf.ed.ac.uk

# 输入密码后，你就在DICE上了
```

---

## 🗄️ 第3步：导入官方测试数据（只需做一次）

### SQL数据（PostgreSQL）
```bash
psql -h pgteach -1 -f /afs/inf.ed.ac.uk/group/teaching/dbs/2025/cw/c2/sql/sampledb.sql
```

这会在PostgreSQL中创建一个测试数据库。

---

## 🧪 第4步：测试查询

### 测试单个SQL查询
```bash
# 运行查询并查看结果
psql -h pgteach -f sql_3.txt

# 用官方检查脚本验证
/afs/inf.ed.ac.uk/group/teaching/dbs/2025/cw/c2/sql/check-sql.sh sql_3.txt
```

### 测试单个RA查询
```bash
# 运行查询
java -jar real-0.7.jar -e bag -d /afs/inf.ed.ac.uk/group/teaching/dbs/2025/cw/c2/ra/sampledb.json -q "$(cat ra_1.txt)"

# 用官方检查脚本验证
/afs/inf.ed.ac.uk/group/teaching/dbs/2025/cw/c2/ra/check-ra.sh ra_1.txt
```

### 批量测试所有查询
```bash
# 进入项目文件夹
cd library_db_coursework

# 运行测试脚本
./test_on_dice.sh

# 或者手动批量检查
/afs/inf.ed.ac.uk/group/teaching/dbs/2025/cw/c2/sql/check-sql.sh sql/*.txt
/afs/inf.ed.ac.uk/group/teaching/dbs/2025/cw/c2/ra/check-ra.sh relational_algebra/*.txt
```

---

## ✅ 第5步：检查文件编码

```bash
# 检查单个文件
file sql_3.txt

# 期望输出：
# sql_3.txt: ASCII text
# 或
# sql_3.txt: UTF-8 Unicode text

# 不应该有 "with BOM"！

# 批量检查所有文件
file sql/*.txt
file relational_algebra/*.txt
```

如果显示 "with BOM"，需要转换：
```bash
# 移除BOM（如果需要）
sed -i '1s/^\xEF\xBB\xBF//' sql_3.txt
```

---

## 📊 第6步：查看查询结果

### 查看SQL查询结果（带格式）
```bash
psql -h pgteach -f sql_3.txt
```

### 只看前几行
```bash
psql -h pgteach -f sql_3.txt | head -20
```

### 保存结果到文件
```bash
psql -h pgteach -f sql_3.txt > sql_3_output.txt
```

---

## 🔧 常见问题

### Q: 如果忘记导入测试数据？
```bash
# 重新导入（会覆盖）
psql -h pgteach -1 -f /afs/inf.ed.ac.uk/group/teaching/dbs/2025/cw/c2/sql/sampledb.sql
```

### Q: REAL找不到？
```bash
# 检查REAL版本
java -jar real-0.7.jar --version

# 如果找不到，可能需要从课程网站下载
```

### Q: 权限被拒绝？
```bash
# 确保脚本有执行权限
chmod +x test_on_dice.sh
```

### Q: 编码问题？
在VS Code中（Windows）：
1. 右下角点击编码
2. 选择 "Save with Encoding"
3. 选择 "UTF-8"（不是 UTF-8 with BOM）

---

## 📋 完整测试流程示例

```bash
# 1. 连接到DICE
ssh YOUR_USERNAME@student.ssh.inf.ed.ac.uk

# 2. 导入测试数据（第一次）
psql -h pgteach -1 -f /afs/inf.ed.ac.uk/group/teaching/dbs/2025/cw/c2/sql/sampledb.sql

# 3. 进入项目文件夹
cd library_db_coursework

# 4. 测试所有SQL查询
for i in {1..5}; do
    echo "Testing sql_$i.txt"
    /afs/inf.ed.ac.uk/group/teaching/dbs/2025/cw/c2/sql/check-sql.sh sql/sql_$i.txt
done

# 5. 测试所有RA查询
for i in {1..5}; do
    echo "Testing ra_$i.txt"
    /afs/inf.ed.ac.uk/group/teaching/dbs/2025/cw/c2/ra/check-ra.sh relational_algebra/ra_$i.txt
done

# 6. 检查编码
file sql/*.txt relational_algebra/*.txt

# 7. 如果一切正常，准备提交！
```

---

## 🎯 提交前最终检查

- [ ] 所有10个文件都通过了检查脚本
- [ ] 文件编码正确（UTF-8 without BOM）
- [ ] 文件名全部小写
- [ ] 没有使用禁用词
- [ ] 查询在官方数据上能正确运行

---

## 💡 小贴士

1. **先在本地写完所有查询**，再上传到DICE一次性测试
2. **使用检查脚本**，它会告诉你大部分问题
3. **保存DICE上的输出**，方便调试
4. **备份你的文件**，以防出错

祝你顺利！🎉
