import java.util.*; 
import java.time.*;

/**
 * @author Anthony Bragg, Daniel Morrison
 */
public class Sale{
    DatabaseInterface posDatabase;
    ArrayList<SaleItem> itemsInSale;
    int maxID;


    /**
     * Sale constructor that passes in a reference to a database object. Creates a new list of items in the sale
     * also stores the max saleID from the database.
     * @param posDatabase is a reference to the database connection, this gives the sale access to interacting with the database
     */
    public Sale(DatabaseInterface posDatabase){
        itemsInSale = new ArrayList<SaleItem>();
        this.posDatabase = posDatabase;
        maxID = Integer.valueOf(posDatabase.GetMaxAttribute("product", "productID"));
    }
    
    /**
     * Converts a PID to a Product Name
     * @param id a String representation of the product ID
     * @return a String representation of the product name
     */
    public String GetProductName(String id) {
        return posDatabase.GetAttribute("product","productName",id);
    }

   /**
    * Executes a query and returns the result as a String matrix
    * @return Returns a 2D string matrix that has product names and product IDs from the product table
    */
    public String[][] ToStringMatrix() {
        String[][] stringMatrix = new String[itemsInSale.size()][2];
        for (int i = 0; i < itemsInSale.size(); i++) {
            stringMatrix[i][0] = posDatabase.GetAttribute("product","productName",String.valueOf(itemsInSale.get(i).GetProductID()));
            stringMatrix[i][1] = String.format("%.2f",itemsInSale.get(i).GetAmount());
        }
        return stringMatrix;
    }

    /**
     * Removes an item from the current sale
     * @param productID The ID of the product to remove
     */
    public void RemoveItem(String productID){
        for(int i = 0; i < itemsInSale.size(); i++){
            if(productID.equals(String.valueOf(itemsInSale.get(i).GetProductID()))){
                itemsInSale.remove(i);
                break;
            }
        }
    }

    /**
     * Adds an item to the current sale
     * @param productID The ID of the item to add
     * @param amount The amount of product to add
     */
    public void AddItem(String productID, String amount){
        int numberID = Integer.parseInt(productID);
        float amt = Float.parseFloat(amount);
        if (numberID > 0 && numberID <= maxID){
            itemsInSale.add(new SaleItem(numberID, amt));
        }
    }

    /**
     * Calculates the total price of the current sale
     * @return Returns the total price of the current sale
     */
    public float TotalPrice(){
        float sum = 0.0f;
        for(int i = 0; i < itemsInSale.size(); i++){
            SaleItem current = itemsInSale.get(i);
            sum += GetItemPrice(current.GetProductID(), current.GetAmount());
        }
        return sum;
    }

    /**
     * Calculates the price of a single item in the sale
     * @param productID The productID of the item
     * @param amt The amount of the item being sold
     * @return The price of a single item in the sale
     */
    public float GetItemPrice(int productID, float amt){
       
        // Connect to the database to find the price of the product per unit
        float pricePerUnit = Float.parseFloat(posDatabase.GetAttribute("product","sellPrice",String.valueOf(productID)));
        return pricePerUnit * amt;
    }

    /**
     * Updates the postgre SQL database tables after the completion of a sale
     */
    public void MakeSale(){

        // add an entry to saleHistory
        int saleID = Integer.parseInt(posDatabase.GetMaxAttribute("saleHistory","saleID"))+1;
        posDatabase.AddTableEntry("saleHistory", String.valueOf(saleID), String.valueOf(LocalDateTime.now().toString().substring(0,9)), String.valueOf(TotalPrice()));

        for(int i = 0; i < itemsInSale.size(); i++){
            // subtract the amt sold of each object from its inventory in the currentInventory database.
            float currentStock = Float.parseFloat(posDatabase.GetAttribute("currentInventory","stockQuantity",String.valueOf(itemsInSale.get(i).GetProductID())));
            float newStock = currentStock - itemsInSale.get(i).GetAmount();
            
            if (newStock < 0) {
                newStock = 0;
            }

            posDatabase.EditAttribute("currentInventory","stockQuantity",String.valueOf(itemsInSale.get(i).GetProductID()),String.valueOf(newStock));

            // add the items to saleLineItem
            posDatabase.AddTableEntry("saleLineItem",String.valueOf(saleID), String.valueOf(itemsInSale.get(i).GetProductID()), String.valueOf(currentStock - newStock));
        }
    }

    /**
     * Function for testing the class
     * @param args Input from the terminal
     */
    public static void main(String[] args) {
        /* 
        DatabaseInterface testDatabase = new DatabaseInterface();
        Sale testSale = new Sale(testDatabase);
 
        for (int i = 1; i < 20; i++) {
            testSale.AddItem(i, 500);
        }

        testSale.RemoveItem(5);

        testSale.MakeSale();
        
        testDatabase.GetMaxAttribute("product", "productID");
        testDatabase.GetAttribute("product", "productname", "1");
        testDatabase.EditAttribute("saleLineItem","quantity","24","2.4");


        ArrayList<String> entries = new ArrayList<String>();
        entries.add("71");
        entries.add("Cheese Pizza");
        entries.add("1");
        entries.add("2");
        entries.add("3");
        entries.add("4");
        testDatabase.AddTableEntry("product",entries);
        */

    }
}