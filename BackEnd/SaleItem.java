/**
 * @author Daniel Morrison
 */
public class SaleItem {
    private int productID;
    private float amount;

    /**
    * Creates a sale item given an ID and amount
    * @param ID the ID of the product in the line item
    * @param amt the amount of the item on this line item
    */
    public SaleItem(int ID, float amt){
        productID = ID;
        amount = amt;
    }

    /**
    * Gets the product ID of this item
    * @return Return the productID
    */
    public int GetProductID(){
        return productID;
    }

    /**
    * Gets the amount of the product being sold
    * @return Return the amount being sold
    */
    public float GetAmount(){
        return amount;
    }
}