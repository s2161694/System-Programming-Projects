# VS Code 详细使用指南

## 📥 第一步：下载和解压

1. 下载 `library_db_coursework.zip`
2. 右键 → 解压到当前文件夹
3. 你会得到一个 `library_db_coursework` 文件夹

## 🚀 第二步：在VS Code中打开

1. 启动 **VS Code**
2. 点击 **File** → **Open Folder**
3. 选择 `library_db_coursework` 文件夹
4. 点击 **选择文件夹**

## ✅ 第三步：检查编码设置

VS Code会自动应用 `.vscode/settings.json` 中的配置。

**检查方法**：
- 打开任意文本文件
- 查看窗口右下角
- 应该显示：
  - `UTF-8` （编码）
  - `LF` （行尾符）

**如果不对，手动设置**：
1. 点击右下角的编码名称
2. 选择 "Save with Encoding"
3. 选择 "UTF-8"（不是 UTF-8 with BOM）

## 📝 第四步：开始写查询

### 创建关系代数查询文件

1. 在左侧文件树中，右键 `relational_algebra` 文件夹
2. 选择 "New File"
3. 输入文件名：`ra_1.txt`
4. 开始写你的查询

重复步骤创建 ra_2.txt 到 ra_5.txt

### 创建SQL查询文件

1. 在左侧文件树中，右键 `sql` 文件夹
2. 选择 "New File"
3. 输入文件名：`sql_1.txt`
4. 开始写你的查询

重复步骤创建 sql_2.txt 到 sql_5.txt

## 💡 VS Code 使用技巧

### 保存文件
- Windows: `Ctrl + S`
- Mac: `Cmd + S`

### 查看所有文件
- Windows: `Ctrl + P`
- Mac: `Cmd + P`

### 分屏查看
- 右键文件标签 → Split Right
- 这样可以同时看schema和你的查询

### 搜索
- Windows: `Ctrl + F`
- Mac: `Cmd + F`

## 🔍 检查禁用词

在编写SQL查询时，确保不使用 `forbidden_words.txt` 中的词：

**禁用词包括**：
- COALESCE, NULL, NULLIF
- LEFT, RIGHT, FULL, OUTER (不能用外连接)
- WITH (不能用CTE)
- ROUND (不能用四舍五入)
- LIMIT, ROW_NUMBER (不能用这些)

**小技巧**：
在VS Code中按 `Ctrl+F`，搜索这些词，确保你没有使用它们

## 📋 提交前检查清单

打开每个文件，确认：

- [ ] 文件名是小写：ra_1.txt 不是 RA_1.txt
- [ ] 右下角显示 UTF-8（不是 UTF-8 with BOM）
- [ ] 右下角显示 LF（不是 CRLF）
- [ ] 没有使用禁用词
- [ ] 列名正确（区分大小写）
- [ ] 每个文件只有一个查询语句
- [ ] SQL查询没有注释

## 🧪 测试查询（可选）

如果你安装了SQLite：

1. 打开 VS Code 终端：Terminal → New Terminal
2. 创建数据库：
   ```bash
   sqlite3 library.db < schema.sql
   sqlite3 library.db < test_data.sql
   ```
3. 测试查询：
   ```bash
   sqlite3 library.db < sql/sql_1.txt
   ```

## ❓ 常见问题

**Q: UTF-8 with BOM 和 UTF-8 有什么区别？**
A: BOM是文件开头的特殊标记。作业要求不能有BOM，所以必须选择 "UTF-8"（不是 UTF-8 with BOM）

**Q: CRLF 和 LF 有什么区别？**
A: 
- CRLF：Windows风格的换行符
- LF：Linux/Mac风格的换行符
- 作业可能在Linux服务器上测试，所以用LF更安全

**Q: 我可以在查询中加注释吗？**
A: 关系代数可以，但SQL查询不能有注释

**Q: 分号是必需的吗？**
A: 不是，可选的。加不加都行。

**Q: 我怎么知道我的查询是对的？**
A: 
1. 仔细阅读 QUERIES_EXPLAINED.md 中的说明
2. 用示例数据测试
3. 确保输出列名和数量正确

## 📚 参考文件

- `README.md` - 项目概览
- `QUICK_START.md` - 快速开始
- `relational_algebra/QUERIES_EXPLAINED.md` - 关系代数查询详细说明
- `sql/QUERIES_EXPLAINED.md` - SQL查询详细说明
- `schema.sql` - 数据库结构
- `test_data.sql` - 测试数据
- `forbidden_words.txt` - 禁用词列表

## 🎯 下一步

现在你已经准备好了！开始写你的第一个查询吧。

建议从简单的开始：
1. SQL Query 3（分支统计）
2. SQL Query 2（顾客统计）
3. 然后再做其他的
