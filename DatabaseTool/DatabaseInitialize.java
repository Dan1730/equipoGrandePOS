import java.util.*;

import java.sql.*;
import java.io.*;  

class DatabaseInitialize {
    static Connection databaseConnection = null;
    static Statement executionStatement = null;

    /*
      Establishes a connection between java and AWS with the given
    */
    public static void DatabaseConnect() {
        // Initialize Credentials
        String username = DatabaseUserInfo.username;
        String password = DatabaseUserInfo.password;

        // Intitialize database information for connection
        String connectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315950_4db";

        // Attempt to connect to the database
        try {
            databaseConnection = DriverManager.getConnection(connectionString, username, password);
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    
        System.out.println("Opened database successfully");
    }

    /* Takes data from the CSV and uploads it to the database in a table format
     - Parameter fileName must be a location of the spreadsheet in the local computer */
    public static void ProcessCSV(String fileName, String sqlTableName, int columns) {
        // Setting up scanner and opening file
        Scanner scanner;
        try {
            scanner = new Scanner(new File(fileName));
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": CSV File could not be opened");
            return;
        }
        
        // The while loop puts data from the spreadsheet into the database
        while(scanner.hasNext()) {
            String[] currentLine = scanner.nextLine().split(",");
            String sqlCommand = "INSERT INTO " + sqlTableName + " VALUES (";
            sqlCommand += "'" + currentLine[0] + "'";
            for(int j = 1; j < columns; j++) {
                sqlCommand += ", " + "'" + currentLine[j] + "'";
            }
            sqlCommand += (");");
            RunSQL(sqlCommand);
        }
    }

    /*
        Runs any SQL statement that doesn't provide a result
    */
    public static void RunSQL(String sqlLine) {
        try {
            // Creates a statement and e
            ////Statement executionStatement = databaseConnection.createStatement();
            executionStatement.executeUpdate(sqlLine);
        } 
        catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }

    /*
        Returns a string representation of the item located in given column of the first row in the result of the given query
    */
    public static String RunSelect(String sqlLine, String columnName) {
        try {
            // Creates a statement and executres a quert returns a result set
            //Statement executionStatement = databaseConnection.createStatement();
            ResultSet queryResult = executionStatement.executeQuery(sqlLine);

            if (queryResult.next()) {
                return queryResult.getString(columnName);
            }
        } 
        catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return "Query Error: no result";
    }

    /*
        Returns the number of rows in a given table
    */
    public static int RowCount(String tableName) {
        // Attempts to count the rows in the given table
        try {
            // Creates a statement and executres a quert returns a result set
            //Statement executionStatement = databaseConnection.createStatement();
            ResultSet queryResult = executionStatement.executeQuery("SELECT COUNT(*) AS rowCount FROM " + tableName + ";");

            if (queryResult.next()) {
                return queryResult.getInt("rowCount");
            }
        } 
        catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return 0;
    }

    /*
        Deletes the table if it already exists
    */
    public static void DropTable(String tableName){
        try {
            //Statement executionStatement = databaseConnection.createStatement();
            executionStatement.executeUpdate("DROP TABLE " + tableName + ";");
        } 
        catch (Exception e) {
            System.err.println(e.getClass().getName()+": Attempting to replace a table that doesn't exist");
        }
    }

    /* 
    Replace an existing table in the database with a new table using SQL code 
    */
    public static void ReplaceTable(String tableName, String sqlCode) {
        // Deletes the table if it already exists
        DropTable(tableName);
        
        // Create New Table
        try {
            ////Statement executionStatement = databaseConnection.createStatement();
            executionStatement.executeUpdate(sqlCode);
        } 
        catch (Exception e) {
            System.err.println(e.getClass().getName()+": Failure to replace table");
        }
    }

    /* 
    Creates the default tables in our SQL database 
    */
    public static void CreateTables() {
        try {
            ////Statement executionStatement = databaseConnection.createStatement();

            ReplaceTable("currentinventory",
                "  CREATE TABLE currentinventory (" +
                "  productID INTEGER PRIMARY KEY," +
                "  stockQuantity FLOAT," +
                "  restockQuantity FLOAT" +
                ");"
            );
            
            ReplaceTable("product",
                "  CREATE TABLE Product (" +
                "  productID INTEGER PRIMARY KEY," +
                "  productName VARCHAR(255)," +
                "  sellPrice FLOAT," +
                "  sellUnit INTEGER," +
                "  purchasePrice FLOAT," +
                "  purchaseUnit INTEGER" +
                ");"
            );

            ReplaceTable("saleHistory",
                "  CREATE TABLE saleHistory (" +
                "  saleID INTEGER PRIMARY KEY," +
                "  saleDate DATE," +
                "  revenue FLOAT" +
                ");"
            );
            
            ReplaceTable("saleLineItem",
                "  CREATE TABLE saleLineItem (" +
                "  saleID INTEGER," +
                "  productID INTEGER," +
                "  quantity FLOAT" +
                ");"
            );

            ReplaceTable("vendorHistory",
                "  CREATE TABLE vendorHistory (" +
                "  saleID INTEGER PRIMARY KEY," +
                "  saleDate DATE," +
                "  cost FLOAT" +
                ");"
            );

            ReplaceTable("vendorLineItem",
                "  CREATE TABLE vendorLineItem (" +
                "  saleID INTEGER," +
                "  productID INTEGER," +
                "  quantity FLOAT" +
                ");"
            );
            
            ReplaceTable("userInformation",
                "  CREATE TABLE userInformation (" +
                "  userID INTEGER PRIMARY KEY," +
                "  username VARCHAR(255)," +
                "  password VARCHAR(255)," +
                "  role VARCHAR(255)" +
                ");"
            );

            // Shares permissions of new tables with all of the development team
            ShareTable("currentinventory");
            ShareTable("product");
            ShareTable("salehistory");
            ShareTable("saleLineItem");
            ShareTable("vendorHistory");
            ShareTable("vendorLineItem");
            ShareTable("userInformation");
            ShareTable("revenueHistory");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }

    /* 
        Shares permissions of a table with all of the development team 
    */
    public static void ShareTable(String tableName){
        try {
            ////Statement executionStatement = databaseConnection.createStatement();
            executionStatement.executeUpdate("GRANT ALL ON " + tableName + " TO csce315950_anthony;");
            executionStatement.executeUpdate("GRANT ALL ON " + tableName + " TO csce315950_daniel;");
            executionStatement.executeUpdate("GRANT ALL ON " + tableName + " TO csce315950_caroline;");
            executionStatement.executeUpdate("GRANT ALL ON " + tableName + " TO csce315950_chris;");
            executionStatement.executeUpdate("GRANT ALL ON " + tableName + " TO csce315950_juliana;");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }


    public static void main(String[] args) {
        // Initialize connection for the database
        DatabaseConnect();
        try {
            executionStatement = databaseConnection.createStatement();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }


        // Initialize tables for the database
        CreateTables();

        // Process data from csv file and run the associated SQL commands
        ProcessCSV("InitialDatabase/currentinventory.csv", "currentinventory", 3);
        System.out.println("Status Report: Current Inventory Table Completed and Populated");

        ProcessCSV("InitialDatabase/product.csv", "product", 6);
        System.out.println("Status Report: Product Table Completed and Populated");

        ProcessCSV("InitialDatabase/userinformation.csv", "userinformation", 4);
        System.out.println("Status Report: User Information Table Completed and Populated");

        ProcessCSV("InitialDatabase/salehistory.csv", "salehistory", 3);
        System.out.println("Status Report: Sale History Table Completed and Populated");

        ProcessCSV("InitialDatabase/salelineitem.csv", "salelineitem", 3);
        System.out.println("Status Report: Sale Line Item Table Completed and Populated");

        ProcessCSV("InitialDatabase/vendorhistory.csv", "vendorhistory", 3);
        System.out.println("Status Report: Vendor History Table Completed and Populated");

        ProcessCSV("InitialDatabase/vendorlineitem.csv", "vendorlineitem", 3);
        System.out.println("Status Report: Vendor Line Item Table Completed and Populated");


        // Close the connection for the database
        try {
            databaseConnection.close();
            System.out.println("Connection Closed.");
        } 
        catch(Exception e) {
            System.out.println("Connection NOT Closed.");
        }
    }
}