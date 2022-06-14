class CurrentProducts {

    private DatabaseInterface posDatabase;

    public CurrentProducts(DatabaseInterface posDatabase) {
        this.posDatabase = posDatabase;
    }
    
    // Add new Product to both the product table and the currentinventory table
    public void AddProductToProducts(String PID, String name, String sellPrice, String purchasePrice, Unit unit) {
        
        // TODO: make sure PID is unique

        // send query to db 
        String unitInt = Unit.toString(unit);

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

    public void AddOrEditProduct(String PID, String name, String sellPrice, String purchasePrice, Unit unit) {
        if(posDatabase.DoesIDExistInTable("product", PID)) {
            EditExistingProductAttribute(PID, "productname", name);
            EditExistingProductAttribute(PID, "sellprice", sellPrice);
            EditExistingProductAttribute(PID, "purchaseprice", purchasePrice);
            EditExistingProductAttribute(PID, "sellunit", Unit.toString(unit));
            EditExistingProductAttribute(PID, "purchaseunit", Unit.toString(unit));            
        }
        else {
            AddProductToProducts(PID, name, sellPrice, purchasePrice, unit);
        }
    }
    
    // Get list of products and product IDs
    public String[][] getProductMatrix() {
        return posDatabase.getStringMatrix("product", "productid", "productname", "sellprice", "purchaseprice", "purchaseunit");
    }
}