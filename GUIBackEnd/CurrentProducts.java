class CurrentProducts {

    private DatabaseInterface posDatabase;

    public CurrentProducts(DatabaseInterface posDatabase) {
        this.posDatabase = posDatabase;
    }
    
    // Add new Product
    public void AddProductToProducts(String PID, String name, String sellPrice, String purchasePrice, Unit unit) {
        
        // TODO: make sure PID is unique

        // send query to db 
        String unitInt = "0";
        if(unit == Unit.kgs) {
            unitInt = "1";
        }

        posDatabase.AddTableEntry("Product", PID, name, sellPrice, unitInt, purchasePrice, unitInt);
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