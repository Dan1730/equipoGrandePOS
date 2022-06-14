import java.util.*;
import java.sql.*;
import java.io.*;  
import java.time.*;

// Functions for reporting a sale
class Sale{
    DatabaseInterface posDatabase;
    ArrayList<SaleItem> itemsInSale;
    int maxID;


    
    public Sale(DatabaseInterface posDatabase){
        itemsInSale = new ArrayList<SaleItem>();
        this.posDatabase = posDatabase;
        maxID = Integer.valueOf(posDatabase.GetMaxAttribute("product", "productID"));
    }
    
    // Formats all of the items of the sale into a matrix for display in the GUI
    public String[][] toStringMatrix() {
        String[][] stringMatrix = new String[itemsInSale.size()][2];
        for (int i = 0; i < itemsInSale.size(); i++) {
            stringMatrix[i][0] = posDatabase.GetAttribute("product","productName",String.valueOf(itemsInSale.get(i).GetProductID()));
            stringMatrix[i][1] = String.format("%.2f",itemsInSale.get(i).GetAmount());
        }
        return stringMatrix;
    }

    // Remove an item from the order
    public void RemoveItem(int productID){
        for(int i = 0; i < itemsInSale.size(); i++){
            if(itemsInSale.get(i).GetProductID() == productID){
                itemsInSale.remove(i);
                break;
            }
        }
    }

    // Adds an item to the order
    public void AddItem(int productID, float amt){
        if (productID > 0 && productID <= maxID){
            itemsInSale.add(new SaleItem(productID, amt));
        }
    }

    // Calculates the total price of the sale
    public float TotalPrice(){
        float sum = 0.0f;
        for(int i = 0; i < itemsInSale.size(); i++){
            SaleItem current = itemsInSale.get(i);
            sum += GetItemPrice(current.GetProductID(), current.GetAmount());
        }
        return sum;
    }

    // Returns the price of a single item
    public float GetItemPrice(int productID, float amt){
       
        // Connect to the database to find the price of the product per unit
        float pricePerUnit = Float.parseFloat(posDatabase.GetAttribute("product","sellPrice",String.valueOf(productID)));
        return pricePerUnit * amt;
    }

    // Updates the database after sale has been made
    public void MakeSale(){

        // add an entry to saleHistory
        ArrayList<String> entries = new ArrayList<String>();
        int saleID = Integer.parseInt(posDatabase.GetMaxAttribute("saleHistory","saleID"))+1;
        entries.add(String.valueOf(saleID)); // saleID
        entries.add(String.valueOf(LocalDateTime.now().toString().substring(0,9))); // saleDate
        entries.add(String.valueOf(TotalPrice())); // revenue
        posDatabase.AddTableEntry("saleHistory", entries);

        for(int i = 0; i < itemsInSale.size(); i++){
            // subtract the amt sold of each object from its inventory in the currentInventory database.
            float currentStock = Float.parseFloat(posDatabase.GetAttribute("currentInventory","stockQuantity",String.valueOf(itemsInSale.get(i).GetProductID())));
            float newStock = currentStock - itemsInSale.get(i).GetAmount();
            
            if (newStock < 0) {
                newStock = 0;
            }

            posDatabase.EditAttribute("currentInventory","stockQuantity",String.valueOf(itemsInSale.get(i).GetProductID()),String.valueOf(newStock));

            // add the items to saleLineItem
            entries = new ArrayList<String>();
            entries.add(String.valueOf(saleID));
            entries.add(String.valueOf(itemsInSale.get(i).GetProductID()));
            entries.add(String.valueOf(currentStock - newStock));
            posDatabase.AddTableEntry("saleLineItem",entries);
        }
    }

    public static void main(String[] args) {
        DatabaseInterface testDatabase = new DatabaseInterface();
        Sale testSale = new Sale(testDatabase);
 
        for (int i = 1; i < 20; i++) {
            testSale.AddItem(i, 500);
        }

        testSale.RemoveItem(5);

        testSale.MakeSale();
        
        /*
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