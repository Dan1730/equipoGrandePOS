class CurrentProducts {

    private DatabaseInterface posDatabase;

    public CurrentProducts(DatabaseInterface posDatabase) {
        this.posDatabase = posDatabase;
    }
    
    // Add new Product to both the product table and the currentinventory table
    public void AddProductToProducts(String PID, String name, String sellPrice, String purchasePrice, Unit unit) {
        
        // TODO: make sure PID is unique

        // send query to db 
        String unitInt = "0";
        if(unit == Unit.kgs) {
            unitInt = "1";
        }

        posDatabase.AddTableEntry("Product", PID, name, sellPrice, unitInt, purchasePrice, unitInt);
        posDatabase.AddTableEntry("currentinventory", PID, "0", "0");
    }

    public void RemoveProductFromProducts(String PID) {
        posDatabase.RemoveEntryFromTable("product", "productid", PID);
        posDatabase.RemoveEntryFromTable("currentinventory", "productid", PID);
    }

    // Edit existing product
    public void EditExistingProductAttribute(String PID, String attributeName, String newValue) {
        posDatabase.EditAttribute("Product", attributeName, PID, newValue);
    }
    
    // Get list of products and product IDs
    public String[][] getProductMatrix() {
        return posDatabase.getStringMatrix("product", "productid", "productname", "sellprice", "purchaseprice", "purchaseunit");
    }
}