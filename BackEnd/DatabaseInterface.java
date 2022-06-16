import java.sql.*;
import java.util.ArrayList;

/**
 * @author Anthony Bragg, Daniel Morrison, Chris Weeks
 */
public class DatabaseInterface {
    static Connection databaseConnection = null;
    static Statement executionStatement = null;

    /**
     * This is the default constructor for the DatabaseInterface Class, it establishes a 
     * connection to AWS based on information in the DatabaseUserInformation class
     */
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
    
    /**
     * This function counts the number of rows in a given table in the postgre SQL database
     * @param tableName The table whose rows must be counted
     * @return Returns the number of rows in the table
     * @throws Exception when the given table does not exist
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

    /**
     * This function executes a query to get a specific attribute from the postgre SQL database
     * @param queryString queryString is the SQL command to  executed in a String format
     * @param resultName resultName is the name of the column that the result is to be taken from
     * @return Returns a string that represents the attribute given.
     */
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

    /**
     * This function takes in a SQL query as a string a returns the ResultSet Object.
     * @param queryString the SQL command to execute as a String
     * @return Returns a ResultSet Object for the query or null if query failed
     */
    ResultSet ExecuteRawQuery(String queryString) {
        try {
            return executionStatement.executeQuery(queryString);
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            return null;
        }
    }
    
    boolean DoesIDExistInTable(String tableName, String attributeName, String ID) {
        ResultSet queryResult = ExecuteRawQuery("SELECT * FROM " + tableName + " WHERE " + attributeName + "=" + ID);
        try {
            return queryResult.next();
        }
        catch(SQLException e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            return false;
        }
        
    }

    /**
     * Retrives the maximum attribute of a column in one of our postgre SQL database's tables
     * @param tableName The name of the table that we are pulling values from
     * @param columnName The column that we are pulling values from
     * @return Returns the highest value in the given table and column
     */
    String GetMaxAttribute(String tableName, String columnName) {
        return ExecuteAttributeQuery("SELECT MAX(" + columnName + ") FROM " + tableName + ";", "MAX");
    }

    /**
     * Retrieves a given attribute from the postgre SQL database
     * @param tableName The name of the table which has the attribute
     * @param columnName The column which has the attribute
     * @param ID The productID in the row which has the attribute
     * @return Returns the specified attribute from the table
     */
    String GetAttribute(String tableName, String columnName, String ID) {
        return ExecuteAttributeQuery("SELECT " + columnName + " FROM " + tableName + " WHERE productID = " + ID + ";", columnName);
    }

    /**
     * Retrives a given attribute frmo the postgre SQL database where productID cannot be used to identify the correct row
     * @param tableName The name of the table which has the attribute
     * @param columnName The column which has the attribute
     * @param ID The unique key that identifies the row which has the attribute
     * @param typeID The column that the unique key is in
     * @return Returns the specified attribute from the table
     */
    String GetAttribute(String tableName, String columnName, String ID, String typeID) {
        return ExecuteAttributeQuery("SELECT " + columnName + " FROM " + tableName + " WHERE " + typeID + " = " + ID + ";", columnName);
    }

    /**
     * Edit an attribute for an ID in the specified table
     * @param tableName The table name to execute the query on
     * @param columnName The attribute name to edit
     * @param ID The ID of the row to query for
     * @param newValue The new value for the attribute
     */
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

    /**
     * Adds a new entry to a table
     * @param tableName The table to execute the query on
     * @param attributes The values of the attributes of the new entry in the table. The values
     *      should be listed in the same order that they appear in the table
     */
    public void AddTableEntry(String tableName, String ... attributes){
        try{
            String sqlStatement = "INSERT INTO " + tableName + " VALUES('" + attributes[0];
            for(int i = 1; i < attributes.length; i++){
                sqlStatement += "', '" + attributes[i];
            }
            sqlStatement += "');";

            System.out.println(sqlStatement);
            executionStatement.executeUpdate(sqlStatement);
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }
    
    // remove entry (entries) from table where attributeName = attributeValue
    public void RemoveEntryFromTable(String tableName, String attributeName, String attributeValue) {
        ExecuteRawQuery("DELETE FROM " + tableName + " WHERE " + attributeName + "=" + attributeValue);
    }

    /**
     * Executes a query and returns the result as a String matrix
     * @param tableName The table to execute the query on 
     * @param attributes The list of attributes to query on The attributes that will be pulled from the table
     * @return Returns a 2D Array of Strings where each row is a row returned by the query and the
     *      columns are in the order they appeared in the attributes varargs.
     */
    public String[][] getStringMatrix(String tableName, String ... attributes) {
        // construct the SQL query
        String sqlStatement = "SELECT " + attributes[0];
        for(int i = 1; i < attributes.length; i++){
            sqlStatement += ", " + attributes[i];
        }
        sqlStatement += " FROM " + tableName + " ORDER BY " + attributes[0] + ";";

        return generateQueryMatrix(sqlStatement, attributes);
    }

    String[][] generateQueryMatrix (String sqlQuery, String ... attributes) {
            ArrayList<String[]> strArrList = new ArrayList<String[]>();
    
            // execute the query
            ResultSet queryResult = ExecuteRawQuery(sqlQuery);
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
    
            return returnArray;
    }

    /**
     * Function for testing the class
     * @param args Input from the terminal
     */
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
