/**
 * @author Anthony and Daniel
 */
public class CurrentInventory {
    DatabaseInterface posDatabase = null;

    /**
     * Constructor for the CurrentInventory object
     * @param posDatabase A connection to the SQL postgre database
     */
    CurrentInventory(DatabaseInterface posDatabase) {
        this.posDatabase = posDatabase;
    }
    
    /**
     * Executes a query and returns the result as a String matrix
     * @return Returns a 2D Array of Strings where each row is a row returned by the query and the
     *      columns are in the order they appeared in the currentInventory table
     */
    public String[][] ToStringMatrix(){
        return posDatabase.getStringMatrix("currentInventory", "productID", "stockquantity", "restockquantity");
    }

    /**
     * Updates the stock columns of Current Inventory when given a specific PID
     * @param productID the product to modify inventory of
     * @param amountInStock the amount to change current stock to
     * @param restockAmount the amount to change the restock quantity to
     */
    public void UpdateProductInventory(String productID, String amountInStock, String restockAmount){
        posDatabase.EditAttribute("currentInventory","stockQuantity",productID,amountInStock);
        posDatabase.EditAttribute("CurrentInventory","restockQuantity",productID,restockAmount);
    }

    /**
     * Function for testing the class
     * @param args Input from the terminal
     */
    public static void main(String[] args) {
        DatabaseInterface test = new DatabaseInterface();
        CurrentInventory testInventory = new CurrentInventory(test);

        testInventory.UpdateProductInventory("27","10","20");
    }
}