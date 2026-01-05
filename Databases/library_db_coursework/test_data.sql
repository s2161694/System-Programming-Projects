-- 示例测试数据
-- 这些数据可以帮助你测试查询是否正确

-- Books
INSERT INTO Books VALUES ('The Hobbit', 'J.R.R. Tolkien', 'FICTION', 'FANTASY');
INSERT INTO Books VALUES ('1984', 'George Orwell', 'FICTION', 'SCI-FI');
INSERT INTO Books VALUES ('Steve Jobs', 'Walter Isaacson', 'NON-FICTION', 'BIOGRAPHY');
INSERT INTO Books VALUES ('Sapiens', 'Yuval Noah Harari', 'NON-FICTION', 'HISTORY');

-- Customers
INSERT INTO Customers VALUES ('C001', 'Alice Smith', 'Central');
INSERT INTO Customers VALUES ('C002', 'Bob Johnson', 'West');
INSERT INTO Customers VALUES ('C003', 'Carol White', 'East');
INSERT INTO Customers VALUES ('C004', 'David Brown', 'Central');

-- Copies
INSERT INTO Copies VALUES ('CP001', 'The Hobbit', 'J.R.R. Tolkien', 'Central');
INSERT INTO Copies VALUES ('CP002', 'The Hobbit', 'J.R.R. Tolkien', 'West');
INSERT INTO Copies VALUES ('CP003', '1984', 'George Orwell', 'Central');
INSERT INTO Copies VALUES ('CP004', 'Steve Jobs', 'Walter Isaacson', 'East');
INSERT INTO Copies VALUES ('CP005', 'Sapiens', 'Yuval Noah Harari', 'Central');

-- Loans (使用简单的日期格式 YYYY-MM-DD)
INSERT INTO Loans VALUES ('L001', 'CP001', 'C001', '2024-01-01', '2024-01-15');
INSERT INTO Loans VALUES ('L002', 'CP002', 'C002', '2024-01-05', '2024-01-19');
INSERT INTO Loans VALUES ('L003', 'CP003', 'C001', '2024-02-01', '2024-02-15');
INSERT INTO Loans VALUES ('L004', 'CP001', 'C001', '2024-03-01', '2024-03-15');

-- Returns
INSERT INTO Returns VALUES ('L001', '2024-01-12');  -- On time
INSERT INTO Returns VALUES ('L002', '2024-01-25');  -- Late
INSERT INTO Returns VALUES ('L003', '2024-02-10');  -- On time

-- Holds
INSERT INTO Holds VALUES ('C003', 'The Hobbit', 'J.R.R. Tolkien', 1);
INSERT INTO Holds VALUES ('C004', 'The Hobbit', 'J.R.R. Tolkien', 2);
