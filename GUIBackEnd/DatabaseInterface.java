import java.util.*;
import java.sql.*;
import java.io.*; 

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

    public void AddTableEntry(String tableName, ArrayList<String> entries){
        try{
            String sqlStatement = "INSERT INTO " + tableName + " VALUES('" + entries.get(0);
            for(int i = 1; i < entries.size(); i++){
                sqlStatement += "', '" + entries.get(i);
            }
            sqlStatement += "');";

            System.out.println(sqlStatement);
            executionStatement.executeUpdate(sqlStatement);
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }
}
