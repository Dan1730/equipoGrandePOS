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
        
        // While loop puts data into the linesOfSpreadsheet variable
        // If columns or rows is bad, errors will be caught
        while(scanner.hasNext()) {
            lines++;
            if(scanner.hasNext()) {
                linesOfSpreadsheet[lines-1] = scanner.next().split(",");
            }
        }
        
        // Assertion that the correct amount of data was in the file
        assert lines == rows : "Rows or columns inputted incorrectly";
        
        String[] SqlCommands = new String[rows];

        // implement CSV data into SqlCommands by line
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
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }

    public static void CreateTables() {
        try {
            // Create a table
            Statement executionStatement = databaseConnection.createStatement();
            
            // Create Current Inventory Table
            executionStatement.executeUpdate("DROP TABLE currentinventory;");
            executionStatement.executeUpdate(
                "CREATE TABLE currentinventory (" +
                "  productID INTEGER PRIMARY KEY," +
                "  stockQuantity FLOAT," +
                "  restockQuantity FLOAT" +
                ");"
            );

            // Create Product Table
            executionStatement.executeUpdate("DROP TABLE product;");
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

            // Create saleHistory table
            executionStatement.executeUpdate("DROP TABLE salehistory;");
            executionStatement.executeUpdate(
                "CREATE TABLE saleHistory (" +
                "  saleID INTEGER PRIMARY KEY," +
                "  saleDate DATE," +
                "  revenue FLOAT" +
                ");"
            );
            
            ShareTable("currentinventory");
            ShareTable("product");
            ShareTable("salehistory");
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

    public static void main(String[] args) {
        // Initialize connection for the database
        DatabaseConnect();

        // Initialize tables for the database
        CreateTables();

        // Process data from csv file and run the associated SQL commands
        ProcessCSV("currentinventory.csv", "currentinventory", 53, 3);

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