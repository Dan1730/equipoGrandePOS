/**
 * @author Tony B 
 */
public class SalesReport {
    DatabaseInterface posDatabase;

    SalesReport(DatabaseInterface posDatabase) {
        this.posDatabase = posDatabase;
    }

    /**
     * Generates a sales report of an item between two dates
     * @param startDate The beginning date in the range (Format YYYY-MM-DD)
     * @param endDate The ending date in the range (Format YYYY-MM-DD)
     * @return Returns a String matrix with the report that can be formatted and viewed as a table in the GUI
     */
    public String[][] generateReport(String startDate, String endDate){
        String rangeQuery = "SELECT MIN(saleID), MAX(saleID) FROM saleHistory WHERE saleDate BETWEEN '" + startDate + "' AND '" + endDate + "';";  

        String[][] rangeID = posDatabase.generateQueryMatrix(rangeQuery, "min", "max");
        String startID = rangeID[0][0];
        String endID = rangeID[0][1];

        System.out.println(startID + " to " + endID);

        String salesReportQuery = "SELECT productName, sum(quantity) as amountSold, sum(quantity*sellprice) as revenue, "
                                    + "sum(quantity*purchaseprice) as cost, sum(quantity*(sellprice-purchaseprice)) as profit"
                                    + " FROM Product p, salelineitem s WHERE s.productID = p.productID AND s.saleID BETWEEN "
                                    + startID + " AND " + endID + " GROUP BY p.productName;";

        return posDatabase.generateQueryMatrix(salesReportQuery, "productName", "amountSold", "revenue", "cost", "profit");
    }

    public String[][] generateRestockReport(String startDate, String endDate) {
        String rangeQuery = "SELECT MIN(saleID), MAX(saleID) FROM saleHistory WHERE saleDate BETWEEN '" + startDate + "' AND '" + endDate + "';";  

        String[][] rangeID = posDatabase.generateQueryMatrix(rangeQuery, "min", "max");
        String startID = rangeID[0][0];
        String endID = rangeID[0][1];

        String restockReportQuery = "SELECT productName,  stockquantity, amountSold, revenue FROM ("
            + "SELECT productName, stockquantity, sum(quantity) as amountSold, sum(quantity*sellprice) as revenue " 
            + "FROM Product p, currentinventory c, salelineitem s "
            + "WHERE s.productID = p.productID AND c.productID = p.productID AND s.saleID BETWEEN " + startID + " AND " + endID
            + " GROUP BY p.productName, stockQuantity"
            + ") AS SalesReport WHERE amountSold > stockQuantity;";

        return posDatabase.generateQueryMatrix(restockReportQuery, "productName", "stockQuantity", "amountSold", "revenue");
    }

    public static void main(String[] args) {
        SalesReport test = new SalesReport(new DatabaseInterface());

        String[][] testReport = test.generateReport("2022-06-01", "2022-06-02");
        for (int i = 0; i < testReport.length; i++) {
            System.out.println(testReport[i][0] + " " + testReport[i][1] + " " + testReport[i][2] + " " + testReport[i][3] + " " + testReport[i][4]);
        }

        testReport = test.generateRestockReport("2022-05-01", "2022-07-02");
        for (int i = 0; i < testReport.length; i++) {
            System.out.println(testReport[i][0] + " " + testReport[i][1] + " " + testReport[i][2] + " " + testReport[i][3]);
    }
}
}