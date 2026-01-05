# SQL查询说明

## Query 1: 每本库存书的借阅次数和平均借阅时长
**输出**: title, author, 借阅次数, 平均时长（1位小数）
**说明**:
- 只包含至少有一个副本的书
- 平均时长 = 归还日期 - 开始日期（天数）
- 只计算已归还的借阅
- 从未被借或全部是活跃借阅的书：平均时长显示为NULL
- 不能直接使用NULL关键字！
- 无重复

**注意**: 禁用ROUND函数，需要用其他方法格式化

## Query 2: 每个顾客的活跃借阅、按时归还和逾期归还统计
**输出**: cust_id, active_loans, on_time_returns, late_returns
**说明**:
- active_loans: 未归还的借阅数
- on_time_returns: 归还日期 ≤ 到期日期
- late_returns: 归还日期 > 到期日期
- 没有任何借阅的顾客：全部显示0
- 无重复

## Query 3: 每个分支的书籍数量和顾客数量
**输出**: branch, book_count, customer_count
**说明**:
- book_count: 该分支有副本的不同书籍数
- customer_count: 在该分支有账户的顾客数
- 没有书的分支：book_count = 0
- 没有顾客的分支：customer_count = 0
- 无重复

## Query 4: 每个书籍类别的借阅者数量和副本数量
**输出**: genre, subgenre, 借阅者数, 副本数
**说明**:
- 每个genre/subgenre组合
- 借阅者数: 借过该类别书的不同顾客（包括活跃借阅）
- 副本数: 该类别所有副本（不管是否可借）
- 无重复

## Query 5: 总是在新借阅前归还所有旧借阅的顾客
**输出**: cust_id, 最近借阅日期
**说明**:
- 每次借书时，之前的借阅都已归还
- 只有一次借阅的顾客也算
- 没有借阅的顾客不在输出中
- 无重复

**关键限制**:
- 不能使用: LEFT/RIGHT/FULL OUTER JOIN
- 不能使用: NULL, COALESCE, NULLIF
- 不能使用: WITH (CTE), ROUND
- 不能使用: LIMIT, ROW_NUMBER等窗口函数
