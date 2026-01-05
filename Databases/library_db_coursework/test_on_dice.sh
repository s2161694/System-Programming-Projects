#!/bin/bash
# DICE测试脚本
# 在DICE上运行此脚本来测试所有查询

echo "======================================"
echo "测试所有SQL查询"
echo "======================================"

for i in {1..5}; do
    echo ""
    echo "--- 测试 sql_$i.txt ---"
    if [ -f "sql/sql_$i.txt" ]; then
        /afs/inf.ed.ac.uk/group/teaching/dbs/2025/cw/c2/sql/check-sql.sh sql/sql_$i.txt
    else
        echo "文件不存在: sql/sql_$i.txt"
    fi
done

echo ""
echo "======================================"
echo "测试所有关系代数查询"
echo "======================================"

for i in {1..5}; do
    echo ""
    echo "--- 测试 ra_$i.txt ---"
    if [ -f "relational_algebra/ra_$i.txt" ]; then
        /afs/inf.ed.ac.uk/group/teaching/dbs/2025/cw/c2/ra/check-ra.sh relational_algebra/ra_$i.txt
    else
        echo "文件不存在: relational_algebra/ra_$i.txt"
    fi
done

echo ""
echo "======================================"
echo "测试完成！"
echo "======================================"
