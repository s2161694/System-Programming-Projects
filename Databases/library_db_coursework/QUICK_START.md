# 快速开始指南

## 1. 在Windows上打开项目

1. 下载整个 `library_db_coursework` 文件夹到你的电脑
2. 用VS Code打开这个文件夹：
   - 打开VS Code
   - File → Open Folder
   - 选择 library_db_coursework 文件夹

## 2. 检查编码设置

VS Code会自动使用 `.vscode/settings.json` 中的设置，确保：
- 文件编码：UTF-8（无BOM）
- 行尾符：LF（Linux格式）

在VS Code右下角检查：
- 应该显示 "UTF-8"
- 应该显示 "LF"

如果不是，点击它们进行更改。

## 3. 开始写查询

### 关系代数查询
在 `relational_algebra/` 文件夹中创建：
- ra_1.txt
- ra_2.txt
- ra_3.txt
- ra_4.txt
- ra_5.txt

### SQL查询
在 `sql/` 文件夹中创建：
- sql_1.txt
- sql_2.txt
- sql_3.txt
- sql_4.txt
- sql_5.txt

## 4. 测试SQL查询（可选）

如果安装了SQLite：

```bash
# 创建数据库
sqlite3 library.db < schema.sql

# 测试查询
sqlite3 library.db < sql/sql_1.txt
```

## 5. 提交前检查清单

- [ ] 文件名全部小写
- [ ] 编码是 UTF-8 without BOM
- [ ] 没有使用禁用词
- [ ] 列名正确（区分大小写）
- [ ] 每个文件只包含一个查询语句

## 6. 常见问题

**Q: 如何确保UTF-8 without BOM？**
A: 在VS Code右下角点击编码 → "Save with Encoding" → 选择 "UTF-8"

**Q: 文件是.txt还是纯文本？**
A: 用.txt扩展名即可，VS Code会识别为纯文本

**Q: 可以有注释吗？**
A: SQL查询不能有注释

**Q: 要加分号吗？**
A: 可选，加不加都行
