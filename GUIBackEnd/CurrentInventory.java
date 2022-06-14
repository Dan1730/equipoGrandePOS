public class CurrentInventory {
    DatabaseInterface posDatabase = null;

    CurrentInventory(DatabaseInterface posDatabase) {
        this.posDatabase = posDatabase;
    }
    
    public String[][] ToStringMatrix(){
        // Waiting on Chris lol
        return new String[][]{{"hi"}, {"i"}};
    }

    public void UpdateProductInventory(String productID, String amountInStock, String restockAmount){
        posDatabase.EditAttribute("currentInventory","stockQuantity",productID,amountInStock);
        posDatabase.EditAttribute("CurrentInventory","restockQuantity",productID,restockAmount);
    }

    public static void main(String[] args) {
        DatabaseInterface test = new DatabaseInterface();
        CurrentInventory testInventory = new CurrentInventory(test);

        testInventory.UpdateProductInventory("27","10","20");
    }
}