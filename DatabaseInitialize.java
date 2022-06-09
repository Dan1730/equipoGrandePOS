import java.util.*;
import java.sql.*;
import java.io.*;  

class DatabaseInitialize {
    static Connection databaseConnection = null;

    // DatabaseConnect():
    // -----------------------------------------------------
    // Establishes a connection between java and AWS
    // Precondition: AWS Server and Credentials are correct
    // Postcondition: A connection is established */
    public static void DatabaseConnect() {
        // Initialize Credentials
        String username = "csce315950_4user";
        String password = "4team4";

        // Intitialize database information for connection
        String databaseName = "csce315950_4db";
        String connectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + databaseName;

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

    /* Takes data from the CSV and formats it into SQL code
     - Returns a list of strings
     - Parameter fileName must be a location of the spreadsheet in the local database */
    public static void ProcessCSV(String fileName, String SqlTableName, int rows, int columns) {
        // Setting up scanner and opening file
        Scanner scanner;
        try {
            scanner = new Scanner(new File(fileName));
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
            return;
        }
        int lines = 0;
        String[][] linesOfSpreadsheet = new String[rows][columns];
        
        // The while loop puts data from the spreadsheet into the linesOfSpreadsheet variable
        while(scanner.hasNext()) {
            lines++;
            if(scanner.hasNext()) {
                linesOfSpreadsheet[lines-1] = scanner.next().split(",");
            }
        }
        
        // Assertion that the correct amount of data was in the file
        assert lines == rows : "Rows or columns inputted incorrectly";
        
        String[] SqlCommands = new String[rows];

        // Format CSV data into SQL commands
        for(int i = 0; i < lines; i++) {
            SqlCommands[i] = "INSERT INTO " + SqlTableName + " VALUES (";
            for(int j = 0; j < columns; j++) {
                if(j != 0) {
                    SqlCommands[i] += ", ";
                }
                SqlCommands[i] += "'" + linesOfSpreadsheet[i][j] + "'";
            }
            SqlCommands[i] += (");");
        }

        // Run the SqlCommands to produce the database
        for(int i = 0; i < lines; i++) {
            RunSQL(SqlCommands[i]);
        }
    }

    //
    public static void RunSQL(String sqlLine) {
        try {
            // Creates a statement and e
            Statement executionStatement = databaseConnection.createStatement();
            executionStatement.executeUpdate(sqlLine);
        } 
        catch (Exception e) {
            System.err.println(e.getClass().getName()+": Table Doesn't Exist");
        }
    }

    public static void CreateTables() {
        try {
            // Create a table
            Statement executionStatement = databaseConnection.createStatement();
            
            // Deletes Current Inventory Table
            try {
                executionStatement.executeUpdate("DROP TABLE currentinventory;");
            } 
            catch (Exception e) {
                System.err.println(e.getClass().getName()+": Table Doesn't Exist");
            }

            // Create Current Inventory Table
            executionStatement.executeUpdate(
                "CREATE TABLE currentinventory (" +
                "  productID INTEGER PRIMARY KEY," +
                "  stockQuantity FLOAT," +
                "  restockQuantity FLOAT" +
                ");"
            );

            // Deletes Product Table
            try {
                executionStatement.executeUpdate("DROP TABLE product;");
            } 
            catch (Exception e) {
                System.err.println(e.getClass().getName()+": Table Doesn't Exist");
            }

            // Create Product Table
            executionStatement.executeUpdate(
                "CREATE TABLE Product (" +
                "  productID INTEGER PRIMARY KEY," +
                "  productName VARCHAR(255)," +
                "  sellUnit INTEGER," +
                "  sellPrice FLOAT," +
                "  purchaseUnit INTEGER," +
                "  purchasePrice FLOAT" +
                ");"
            );

            // Deletes sale history table
            try {
                executionStatement.executeUpdate("DROP TABLE salehistory;");
            } 
            catch (Exception e) {
                System.err.println(e.getClass().getName()+": Table Doesn't Exist");
            }

            // Create saleHistory table
            executionStatement.executeUpdate(
                "CREATE TABLE saleHistory (" +
                "  saleID INTEGER PRIMARY KEY," +
                "  saleDate DATE," +
                "  revenue FLOAT" +
                ");"
            );

            // Deletes sale line item table
            try {
                executionStatement.executeUpdate("DROP TABLE saleLineItem;");
            } 
            catch (Exception e) {
                System.err.println(e.getClass().getName()+": Table Doesn't Exist");
            }

            // Create Sale Line Item Table
            executionStatement.executeUpdate(
                "CREATE TABLE saleLineItem (" +
                "  saleID INTEGER PRIMARY KEY," +
                "  productID INTEGER," +
                "  quantity FLOAT" +
                ");"
            );

            // Deletes vendor history table
            try {
                executionStatement.executeUpdate("DROP TABLE vendorHistory;");
            } 
            catch (Exception e) {
                System.err.println(e.getClass().getName()+": Table Doesn't Exist");
            }
            
            // Create Vendor History table
            executionStatement.executeUpdate(
                "CREATE TABLE vendorHistory (" +
                "  saleID INTEGER PRIMARY KEY," +
                "  saleDate DATE," +
                "  cost FLOAT" +
                ");"
            );

            // Deletes vendor line item table
            try {
                executionStatement.executeUpdate("DROP TABLE vendorLineItem;");
            } 
            catch (Exception e) {
                System.err.println(e.getClass().getName()+": Table Doesn't Exist");
            }
            
            // Create Vendor Line Item table
            executionStatement.executeUpdate(
                "CREATE TABLE vendorLineItem (" +
                "  saleID INTEGER PRIMARY KEY," +
                "  productID INTEGER," +
                "  quantity FLOAT" +
                ");"
            );
            
            // Deletes user information table
            try {
                executionStatement.executeUpdate("DROP TABLE userInformation;");
            } 
            catch (Exception e) {
                System.err.println(e.getClass().getName()+": Table Doesn't Exist");
            }

            // Create user information table
            executionStatement.executeUpdate(
                "CREATE TABLE userInformation (" +
                "  userID INTEGER PRIMARY KEY," +
                "  username VARCHAR(255)," +
                "  password VARCHAR(255)," +
                "  role VARCHAR(255)" +
                ");"
            );

            // Deletes revenue history table
            try {
                executionStatement.executeUpdate("DROP TABLE revenueHistory;");
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
            }

            // Creates revenue history table
            executionStatement.executeUpdate(
                "CREATE TABLE revenueHistory (" +
                "  revenueDate DATE," +
                "  revenue FLOAT," +
                "  expenses FLOAT," +
                "  profit FLOAT" +
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

    public static void ShareTable(String tableName){
        try {
            Statement executionStatement = databaseConnection.createStatement();
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

    // This function contains > 15 SQL commands to run in
    public static void performQueries(){
        // User query #1
        RunSQL("");

        // User query #2
        RunSQL("");

        // Revenue History query #1
        RunSQL("");

        // Revenue History query #2
        RunSQL("");

        // Sales query #1
        RunSQL("");

        // Sales query #2
        RunSQL("");

        // Sale Items query #1
        RunSQL("");

        // Sale Items query #2
        RunSQL("");

        // Current Inventory query #1
        RunSQL("");

        // Current Inventory query #2
        RunSQL("");

        // Vendor Transactions query #1
        RunSQL("");

        // Vendor Transactions query #2
        RunSQL("");

        // Vendor Transaction Items query #1
        RunSQL("");

        // Vendor Transaction Items query #2
        RunSQL("");

        // Product query #1
        RunSQL("");

        // Product query #2
        RunSQL("");
    }

    public static void main(String[] args) {
        // Initialize connection for the database
        DatabaseConnect();

        // Initialize tables for the database
        CreateTables();

        // Process data from csv file and run the associated SQL commands
        ProcessCSV("InitialDatabase/currentinventory.csv", "currentinventory", 53, 3);

        // Verify database for correctness
        
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