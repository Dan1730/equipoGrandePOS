import java.util.*;
import java.sql.*;
import java.io.*;

class CurrentProducts {

    private DatabaseInterface posDatabase;

    public CurrentProducts(DatabaseInterface posDatabase) {
        this.posDatabase = posDatabase;
    }
    
    // Add new Product
    public void AddProductToProducts(String PID, String name, String sellPrice, String purchasePrice, Unit unit) {
        // make sure PID is unique
        // send query to db 
        String unitInt = "0";
        if(unit == Unit.kgs) {
            unitInt = "1";
        }

        ArrayList<String> attributes;
        attributes.add(PID);
        attributes.add(name);
        attributes.add(sellPrice);
        attributes.add(unitInt);
        attributes.add(purchasePrice);
        attributes.add(unitInt);

        posDatabase.AddTableEntry("Product", attributes);
    }

    // Edit existing product
    public void EditExistingProductAttribute(String PID, String attributeName, String newValue) {

        // TODO: this method
        throw UnsupportedOperationException;
    }
    // Get list of products and product IDs

}