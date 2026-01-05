-- 完整测试脚本 - 可以在 https://sqliteonline.com/ 运行

-- 1. 创建表
CREATE TABLE Books (
 title text,
 author text,
 genre text NOT NULL CHECK ( genre IN ('FICTION', 'NON-FICTION') ),
 subgenre text NOT NULL,
 PRIMARY KEY (title, author),
 CHECK ( CASE WHEN genre='FICTION'
 THEN subgenre IN ('SCI-FI', 'FANTASY','ADVENTURE',
 'MYSTERY', 'CRIME', 'HISTORY', 'ROMANCE', 'HORROR')
 WHEN genre='NON-FICTION'
 THEN subgenre IN ('BIOGRAPHY', 'SELF-HELP', 'HISTORY',
 'TRAVEL', 'COOKING', 'CRIME', 'REFERENCE')
 END )
);

CREATE TABLE Copies (
 copy_id text PRIMARY KEY,
 title text NOT NULL,
 author text NOT NULL,
 branch text NOT NULL,
 FOREIGN KEY (title,author) REFERENCES Books(title,author)
);

CREATE TABLE Customers (
 cust_id text PRIMARY KEY,
 name text NOT NULL,
 branch text NOT NULL
);

CREATE TABLE Loans (
 loan_id text PRIMARY KEY,
 copy text NOT NULL REFERENCES Copies(copy_id),
 customer text NOT NULL REFERENCES Customers(cust_id),
 start date NOT NULL,
 due date NOT NULL,
 CHECK ( due >= start )
);

CREATE TABLE Holds (
 customer text NOT NULL REFERENCES Customers(cust_id),
 title text NOT NULL,
 author text NOT NULL,
 queue smallint CHECK ( queue > 0 ),
 PRIMARY KEY (customer, title, author),
 UNIQUE (title, author, queue),
 FOREIGN KEY (title,author) REFERENCES Books(title,author)
);

CREATE TABLE Returns (
 loan text PRIMARY KEY REFERENCES Loans(loan_id),
 date date NOT NULL
);

-- 2. 插入测试数据
INSERT INTO Books VALUES ('The Hobbit', 'J.R.R. Tolkien', 'FICTION', 'FANTASY');
INSERT INTO Books VALUES ('1984', 'George Orwell', 'FICTION', 'SCI-FI');
INSERT INTO Books VALUES ('Steve Jobs', 'Walter Isaacson', 'NON-FICTION', 'BIOGRAPHY');
INSERT INTO Books VALUES ('Sapiens', 'Yuval Noah Harari', 'NON-FICTION', 'HISTORY');

INSERT INTO Customers VALUES ('C001', 'Alice Smith', 'Central');
INSERT INTO Customers VALUES ('C002', 'Bob Johnson', 'West');
INSERT INTO Customers VALUES ('C003', 'Carol White', 'East');
INSERT INTO Customers VALUES ('C004', 'David Brown', 'Central');
INSERT INTO Customers VALUES ('C005', 'Eve Davis', 'North');  -- North分支没有书

INSERT INTO Copies VALUES ('CP001', 'The Hobbit', 'J.R.R. Tolkien', 'Central');
INSERT INTO Copies VALUES ('CP002', 'The Hobbit', 'J.R.R. Tolkien', 'West');
INSERT INTO Copies VALUES ('CP003', '1984', 'George Orwell', 'Central');
INSERT INTO Copies VALUES ('CP004', 'Steve Jobs', 'Walter Isaacson', 'East');
INSERT INTO Copies VALUES ('CP005', 'Sapiens', 'Yuval Noah Harari', 'Central');
INSERT INTO Copies VALUES ('CP006', 'Sapiens', 'Yuval Noah Harari', 'South');  -- South分支没有顾客

INSERT INTO Loans VALUES ('L001', 'CP001', 'C001', '2024-01-01', '2024-01-15');
INSERT INTO Loans VALUES ('L002', 'CP002', 'C002', '2024-01-05', '2024-01-19');
INSERT INTO Loans VALUES ('L003', 'CP003', 'C001', '2024-02-01', '2024-02-15');
INSERT INTO Loans VALUES ('L004', 'CP001', 'C001', '2024-03-01', '2024-03-15');

INSERT INTO Returns VALUES ('L001', '2024-01-12');
INSERT INTO Returns VALUES ('L002', '2024-01-25');
INSERT INTO Returns VALUES ('L003', '2024-02-10');

INSERT INTO Holds VALUES ('C003', 'The Hobbit', 'J.R.R. Tolkien', 1);
INSERT INTO Holds VALUES ('C004', 'The Hobbit', 'J.R.R. Tolkien', 2);

-- 3. 运行 SQL Query 3
SELECT 
    branches.branch,
    (SELECT COUNT(DISTINCT title || '|' || author)
     FROM Copies c
     WHERE c.branch = branches.branch) AS book_count,
    (SELECT COUNT(*)
     FROM Customers cu
     WHERE cu.branch = branches.branch) AS customer_count
FROM (
    SELECT branch FROM Copies
    UNION
    SELECT branch FROM Customers
) AS branches;

-- 预期结果：
-- Central | 3 | 2  (3本不同的书，2个顾客)
-- West    | 1 | 1  (1本书，1个顾客)
-- East    | 1 | 1  (1本书，1个顾客)
-- South   | 1 | 0  (1本书，0个顾客)
-- North   | 0 | 1  (0本书，1个顾客)
