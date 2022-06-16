class CurrentProducts {

    private DatabaseInterface posDatabase;

    public CurrentProducts(DatabaseInterface posDatabase) {
        this.posDatabase = posDatabase;
    }
    
    // Add new Product to both the product table and the currentinventory table
    public void AddProductToProducts(String PID, String name, String sellPrice, String purchasePrice, Unit unit) {
        

        String unitInt = unit.toStringInt();

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
        if(posDatabase.DoesIDExistInTable("product", "productid", PID)) {
            System.out.println("edit");
            EditExistingProductAttribute(PID, "productname", name);
            EditExistingProductAttribute(PID, "sellprice", sellPrice);
            EditExistingProductAttribute(PID, "purchaseprice", purchasePrice);
            EditExistingProductAttribute(PID, "sellunit", unit.toStringInt());
            EditExistingProductAttribute(PID, "purchaseunit", unit.toStringInt());            
        }
        else {
            System.out.println("new");
            AddProductToProducts(PID, name, sellPrice, purchasePrice, unit);
        }
    }
    
    // Get list of products and product IDs
    public String[][] GetProductMatrix() {
        return posDatabase.GetStringMatrix("product", "productid", "productname", "sellprice", "purchaseprice", "purchaseunit");
    }
}