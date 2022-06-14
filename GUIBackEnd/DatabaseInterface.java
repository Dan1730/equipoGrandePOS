import java.sql.*;
import java.util.ArrayList;

public class DatabaseInterface {
    static Connection databaseConnection = null;
    static Statement executionStatement = null;

    // Creates a connection to the database
    DatabaseInterface() {
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

        try {
            executionStatement = databaseConnection.createStatement();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    
        System.out.println("Opened database successfully");
    }

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


    String ExecuteAttributeQuery(String queryString, String resultName) {
        try {
            ResultSet queryResult = executionStatement.executeQuery(queryString);
            
            if (queryResult.next()) {
                return queryResult.getString(resultName);
            }
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }

        return "Query Error: no result";
    }

    ResultSet ExecuteRawQuery(String queryString) {
        try {
            return executionStatement.executeQuery(queryString);
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            return null;
        }
    }

    // Getting
    String GetMaxAttribute(String tableName, String columnName) {
        return ExecuteAttributeQuery("SELECT MAX(" + columnName + ") FROM " + tableName + ";", "MAX");
    }

    // Getting
    String GetAttribute(String tableName, String columnName, String ID) {
        return ExecuteAttributeQuery("SELECT " + columnName + " FROM " + tableName + " WHERE productID = " + ID + ";", columnName);
    }

    // Getting Attrubute for a query that is not product ID based
    String GetAttribute(String tableName, String columnName, String ID, String typeID) {
        return ExecuteAttributeQuery("SELECT " + columnName + " FROM " + tableName + " WHERE " + typeID + " = " + ID + ";", columnName);
    }

    public void EditAttribute(String tableName, String columnName, String ID, String newValue) {
        try{
            String sqlStatement = "UPDATE " + tableName + " SET " 
            + columnName + " = '" + newValue + "' WHERE " + "productID = '" + ID + "';";
            System.out.println(sqlStatement);
            executionStatement.executeUpdate(sqlStatement);
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }

    public void AddTableEntry(String tableName, String ... attributes){
        try{
            String sqlStatement = "INSERT INTO " + tableName + " VALUES('" + attributes[0];
            for(String i : attributes){
                sqlStatement += "', '" + i;
            }
            sqlStatement += "');";

            System.out.println(sqlStatement);
            executionStatement.executeUpdate(sqlStatement);
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }

    // returns a string matrix of the specified query. Attributes are in the order provided in varargs
    public String[][] getStringMatrix(String tableName, String ... attributes) {
        ArrayList<String[]> strArrList = new ArrayList<String[]>();

        // construct the SQL query
        String sqlStatement = "SELECT " + attributes[0];
        for(String i : attributes){
            sqlStatement += ", " + i;
        }
        sqlStatement += " FROM " + tableName + ";";

        System.out.println(sqlStatement);

        // execute the query
        ResultSet queryResult = ExecuteRawQuery(sqlStatement);
        System.out.println(queryResult);
        // convert the query result into an arrayList of String arrays
        try {
            while(queryResult.next()) {
                String[] row = new String[attributes.length];
                for(int i = 0; i < attributes.length; i++) {
                    row[i] = queryResult.getString(attributes[i]);
                }
                strArrList.add(row);
            }
        }
        catch(SQLException e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            return null;
        }
        
        // convert the arraylist of arrays of strings to a 2D string array and return
        String[][] returnArray = new String[strArrList.size()][attributes.length];
        for(int i = 0; i < strArrList.size(); i++) {
            returnArray[i] = strArrList.get(i);
        }

        return (String[][])returnArray;
    }

    public static void main(String[] args) {
        DatabaseInterface dbInterface = new DatabaseInterface();
        String[][] matrix = dbInterface.getStringMatrix("product", "productname", "productid");

        for(String[] row : matrix) {
            for(String str : row) {
                System.out.print(str + ", ");
            }
            System.out.println("");
        }
    }
}
