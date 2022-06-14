import java.util.*;
import java.sql.*;
import java.io.*; 

public class SaleItem{
    float amt;
    int productID;

    public SaleItem(int ID, float amt){
        this.amt = amt;
        productID = ID;
    }

    public int getProductID(){
        return productID;
    }

    public float getAmount(){
        return amt;
    }
}