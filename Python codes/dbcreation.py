import sqlite3

conn = sqlite3.connect('test.db')
print "Opened database successfully";
a="\'\'\'CREATE TABLE COMPANY                                  
         (ID INT PRIMARY KEY     NOT NULL,
         NAME           TEXT    NOT NULL,
         AGE            INT     NOT NULL,
         ADDRESS        CHAR(50),
         SALARY         REAL);\'\'\'"
conn.execute(a)
print "Table created successfully";

conn.close()
