import java.util.ArrayList;

public class ExcessInventory {
    private DatabaseInterface dbInterface;

    
    /**
     * These two queries get the first and last sale id given a date
     */
    private final String SaleHistoryFString = "SELECT MIN(saleID) as minSaleID, MAX(saleID) as maxSaleID FROM saleHistory WHERE saleDate BETWEEN '%s' AND '%s';";
    private final String VendorHistoryFString = "SELECT MIN(saleID) as minSaleID, MAX(saleID) as maxSaleID FROM vendorHistory WHERE saleDate BETWEEN '%s' AND '%s';";

    /**
     * This query generates product ids with the amount sold between two sale ids. Get the correct sale ids using the queries above
     */
    private final String SaleLineItemFString = "SELECT productid, sum(quantity) as amountSold FROM salelineitem WHERE saleid BETWEEN %s AND %s GROUP BY productid ORDER BY productid;";

    /**
     * This query returns product ids with the inventory at the time of the provided sale ids. 
     * The sale ids must be generated through the queries above
     */
    private final String pastInventoryFString = 
        "SELECT c.productid, (c.stockquantity + COALESCE(s.salequantity, 0) - COALESCE(v.salequantity, 0)) as stockOnDate FROM currentinventory c " + 
        "LEFT JOIN(SELECT sli.productid, sum(sli.quantity) as salequantity FROM salelineitem sli WHERE sli.saleid >= %s GROUP BY sli.productid) AS s ON c.productid = s.productid " + 
        "LEFT JOIN(SELECT vli.productid, sum(vli.quantity) as salequantity FROM vendorlineitem vli WHERE vli.saleid > %s GROUP BY vli.productid) AS v ON c.productid = v.productid " + 
        "ORDER BY c.productid;";
    
    public ExcessInventory(DatabaseInterface dbInterface) {
        this.dbInterface = dbInterface;
    }

    public String[][] CalculatePercentExcessInventory(String startDate, String endDate) {
        
        // Get Start Inventory
        String[][] minMaxCustSaleIDSM = dbInterface.generateQueryMatrix(String.format(SaleHistoryFString, startDate, endDate), "minSaleID", "maxSaleID");

        String[][] minMaxVendSaleIDSM = dbInterface.generateQueryMatrix(String.format(VendorHistoryFString, startDate, endDate), "minSaleID", "maxSaleID");

        String firstSID = minMaxCustSaleIDSM[0][0];
        String lastSID = minMaxCustSaleIDSM[0][1];
        String firstVID = minMaxVendSaleIDSM[0][0];
        // String lastVID = minMaxCustSaleIDSM[0][1];

        String[][] stockOnDateSM = dbInterface.generateQueryMatrix(String.format(pastInventoryFString, firstSID, firstVID), "productid", "stockOnDate");

        // Get Amount Sold between startDate and endDate
        String[][] amountSoldInPeriodSM = dbInterface.generateQueryMatrix(String.format(SaleLineItemFString, firstSID, lastSID), "productid", "amountsold");

        // Get Product names
        String[][] productNamesSM = dbInterface.getStringMatrix("product", "productid", "productname");

        // calculate ratio between inventory and amount sold
        ArrayList<String[]> ratioMatrix = new ArrayList<String[]>();
        for(int i = 0; i < amountSoldInPeriodSM.length; i++)
        {
            float ratio = Float.parseFloat(amountSoldInPeriodSM[i][1]) / Float.parseFloat(stockOnDateSM[i][1]);
            if(ratio <= 0.1) {
                ratioMatrix.add(new String[] {amountSoldInPeriodSM[i][0], productNamesSM[i][1], String.format("%.2f%%", (ratio) * 100)});
            }
        }

        // return a String matrix of product ids and ratio
        String[][] ratioSM = new String[ratioMatrix.size()][3];
        for(int r = 0; r < ratioMatrix.size(); r++) {
            for(int c = 0; c < ratioMatrix.get(r).length; c++) {
                ratioSM[r][c] = ratioMatrix.get(r)[c];
            }
        }

        return ratioSM;
    }

    public static void main(String[] args) {
        DatabaseInterface db = new DatabaseInterface();
        ExcessInventory excessInventory = new ExcessInventory(db);

        String[][] ratios = excessInventory.CalculatePercentExcessInventory("2022-06-05", "2022-06-12");

        for(String[] row : ratios) {
            for(String col : row) {
                System.out.print(col + "\t");
            }
            System.out.println("");
        }
    }
}
