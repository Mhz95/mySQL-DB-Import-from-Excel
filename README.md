## Project 581 : DB import from excel + GUI

#### Steps to run:  
1- Setup MySQL locally  
2- Create any DB then update line 16 at `Database.java` with your DBNAME  
```
            this.conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/DBNAME?user=root&serverTimezone=UTC");

```
3- Use any excel sheet as input by updating path to excel sheet at line 19 at `DBExcelImport.java`  
4- run `DBExcelImport.java` if you see excel content at console then it works fine !   