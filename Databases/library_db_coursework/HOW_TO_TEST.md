# 如何测试你的SQL查询

## 🌐 方法1：在线测试（最简单，推荐！）

### 步骤：

1. **打开在线SQL工具**
   - 访问：https://sqliteonline.com/
   - 或：https://www.db-fiddle.com/

2. **粘贴测试脚本**
   - 打开 `test_sql_3.sql`
   - 全选（Ctrl+A）
   - 复制（Ctrl+C）
   - 粘贴到在线工具中

3. **运行**
   - 点击 "Run" 或 "Execute"
   - 查看结果

4. **检查输出**
   预期结果应该是：
   ```
   branch  | book_count | customer_count
   --------|------------|---------------
   Central | 3          | 2
   West    | 1          | 1
   East    | 1          | 1
   South   | 1          | 0
   North   | 0          | 1
   ```

### 解释：
- **Central**: 3本不同的书（The Hobbit, 1984, Sapiens），2个顾客
- **West**: 1本书（The Hobbit），1个顾客
- **East**: 1本书（Steve Jobs），1个顾客
- **South**: 1本书（Sapiens），0个顾客 ← 测试没有顾客的情况
- **North**: 0本书，1个顾客 ← 测试没有书的情况

---

## 💻 方法2：本地安装SQLite（可选）

如果你想在本地测试，可以安装SQLite：

### Windows安装步骤：

1. **下载SQLite**
   - 访问：https://www.sqlite.org/download.html
   - 下载 "sqlite-tools-win32-x86-*.zip"

2. **解压**
   - 解压到任意文件夹，比如 `C:\sqlite`

3. **添加到PATH（可选）**
   - 右键"此电脑" → 属性 → 高级系统设置 → 环境变量
   - 编辑 Path，添加 `C:\sqlite`

4. **测试**
   - 打开CMD或PowerShell
   - 输入：`sqlite3 --version`
   - 如果看到版本号，说明安装成功

### 使用SQLite测试：

在VS Code中打开终端（Terminal → New Terminal），然后：

```bash
# 进入项目文件夹
cd library_db_coursework

# 创建数据库
sqlite3 library.db < schema.sql

# 插入测试数据
sqlite3 library.db < test_data.sql

# 测试Query 3
sqlite3 library.db < sql/sql_3.txt
```

---

## ✅ 快速检查清单（不需要运行）

即使不运行，你也可以检查：

1. **语法检查**
   - [ ] 没有拼写错误
   - [ ] 所有的括号都配对
   - [ ] 表名和列名正确（区分大小写）

2. **禁用词检查**
   - [ ] 没有使用 NULL
   - [ ] 没有使用 LEFT/RIGHT/OUTER JOIN
   - [ ] 没有使用 COALESCE
   - [ ] 没有使用 WITH
   - [ ] 没有使用 ROUND

3. **输出检查**
   - [ ] 3列：branch, book_count, customer_count
   - [ ] 逻辑正确

4. **编码检查**
   - [ ] VS Code右下角显示 UTF-8（不是UTF-8 with BOM）
   - [ ] VS Code右下角显示 LF（不是CRLF）

---

## 🎯 推荐流程

**对于初学者**：
1. 先用在线工具测试
2. 确认结果正确
3. 再写下一个查询

**对于有经验的**：
1. 写完所有查询
2. 批量测试
3. 检查编码

---

## 💡 提示

- 测试只是为了确保查询能运行
- 最终作业会在他们的系统上测试
- 所以即使你没测试也可以，只要逻辑正确
- 但测试可以让你更有信心！

---

## ❓ 遇到问题？

**查询报错？**
- 检查表名、列名是否正确
- 检查括号是否配对
- 检查是否使用了禁用词

**结果不对？**
- 重新阅读查询要求
- 检查测试数据是否正确
- 逐步调试查询

**不知道如何开始？**
- 先理解查询要求
- 画出数据流程图
- 分步骤写查询
