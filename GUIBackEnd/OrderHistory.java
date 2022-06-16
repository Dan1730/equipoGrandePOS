/**
 * @author Daniel Morrison
 */
public class OrderHistory{
    DatabaseInterface posDatabase;

    public OrderHistory(DatabaseInterface data){
        posDatabase = data;
    }

    /**
     * Returns the String matrix of all vendor orders for input into GUI table
     */
    public String[][] fetchTableData(String txId, String date, String productID){
        String sqlConditions = "";

        if (date != null) {
            sqlConditions += " AND saleDate = '" + date + "'";
        }

        if (productID != null) {
            sqlConditions += " AND l.productID = '" + productID + "'";
        }

        if (txId != null) {
            sqlConditions += " AND l.saleID = '" + txId + "'";
        }

        String sqlQuery = "SELECT saleDate, l.saleID, l.productID, Quantity,"
        + "sum(quantity*sellPrice) as SalePrice FROM vendorHistory h, vendorLineItem l, product p WHERE " 
        + " h.saleID = l.saleID" + sqlConditions + " AND p.productID = l.productID GROUP BY saleDate, l.SaleID, l.productID, Quantity;";

        System.out.println(sqlQuery);
        return posDatabase.generateQueryMatrix(sqlQuery, "saledate", "saleID", "productID", "Quantity", "SalePrice");
    }

    /**
     * Calculates the price of a single item in the sale
     * @param productID The productID of the item
     * @param amt The amount of the item being sold
     * @return The price of a single item in the sale
     */
    public float GetItemPrice(int productID, float amt){
        // Connect to the database to find the price of the product per unit
        float pricePerUnit = Float.parseFloat(posDatabase.GetAttribute("product","sellPrice",String.valueOf(productID)));
        return pricePerUnit * amt;
    }

     public static void main(String[] args) {
        DatabaseInterface posDatabase = new DatabaseInterface();
        OrderHistory orderHistory = new OrderHistory(posDatabase);

        String[][] table = orderHistory.fetchTableData("0", "2022-05-29", "2");

        for(int i = 0; i < table.length; i++){
            System.out.print("Row " + i + ": ");
            for(int j = 0; j < table[0].length; j++){
                System.out.print(table[i][j] + ", ");
            }
            System.out.println("");
        }
     }
}