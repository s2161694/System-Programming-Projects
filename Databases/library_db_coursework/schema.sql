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
 date date NOT NULL -- cannot precede the start date of the referenced loan
);
