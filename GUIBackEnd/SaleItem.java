public class SaleItem{
    private int productID;
    private float amount;

    public SaleItem(int ID, float amt){
        productID = ID;
        amount = amt;
    }

    public int GetProductID(){
        return productID;
    }

    public float GetAmount(){
        return amount;
    }
}