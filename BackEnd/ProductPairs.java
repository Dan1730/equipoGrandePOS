import java.util.*;

/**
 * @author Daniel Morrison
 */
public class ProductPairs {
    DatabaseInterface posDatabase;

    ProductPairs(DatabaseInterface posDatabase) {
        this.posDatabase = posDatabase;
    }

    /** Finds the most commonly bought pairs of products over a certain time interval
     *  @param startDate The beginning of the time interval
     *  @param endDate The end of the time interval
     *  @result The most common product pairs formatted in 3 columns, each row being [product1, product2, # of occurances of the pair]
     */
    public String[][] GetBestPairs(String startDate, String endDate){
       
        // Calculate the start and ending sale IDs given the input dates
        String rangeQuery = "SELECT MIN(saleID), MAX(saleID) FROM saleHistory WHERE saleDate BETWEEN '" + startDate + "' AND '" + endDate + "';";  
        String[][] rangeID = posDatabase.GenerateQueryMatrix(rangeQuery, "min", "max");
        if(rangeID[0][0] == null || rangeID[0][1] == null){
            throw new RuntimeException("Error: No sales occured between the dates entered");
        }
        int startID = Integer.parseInt(rangeID[0][0]);
        int endID = Integer.parseInt(rangeID[0][1]);

        // the matrix productComparisonMatrix's indexs are mapped to productIDs through this matrix
        String[][] indexToIdKey = posDatabase.GetStringMatrix("product","productID");

        // the matrix saleLineItems is a list of the saleLineItems table to be pulled from in the loop
        String[][] saleLineItems = posDatabase.GetStringMatrix("saleLineItem","saleID","productID");

        // productComparisonMatrix keeps track of how many times each combination of products is sold together
        int numberOfProducts = DatabaseInterface.RowCount("product");
        int[][] productComparisonMatrix = new int[numberOfProducts][numberOfProducts];

        // loops through every sale and makes additions to productComparisonMatrix
        for(int i = startID; i <= endID; i++){
            int currentSaleID = i;
            ArrayList<Integer> productIDsOfItems = new ArrayList<Integer>();

            // Finds every sale line item with the current Sale ID and puts it in productIDsOfItems
            for(int j = 0; j < saleLineItems.length; j++){
                if(currentSaleID == Integer.parseInt(saleLineItems[j][0])){
                    productIDsOfItems.add(Integer.parseInt(saleLineItems[j][1]));
                }
            }

            /* Converts the collected product IDs (in productIDsOfItems) into the indexes corresponding to those
             * products (in mappedIndexsOfItems) using the indexToIdKey
             */
            ArrayList<Integer> mappedIndexsOfItems = new ArrayList<Integer>();
            for(int j = 0; j < productIDsOfItems.size(); j++){
                for(int k = 0; k < indexToIdKey.length; k++){
                    if(Integer.parseInt(indexToIdKey[k][0]) == productIDsOfItems.get(j)){
                        mappedIndexsOfItems.add(k);
                        k = indexToIdKey.length;
                    }
                }
            }

            // Each mapped index combination is added to the productComparisonMatrix (except pairs of identical fruits)
            for(int j = 0; j < mappedIndexsOfItems.size()-1; j++){
                for(int k = j+1; k < mappedIndexsOfItems.size(); k++){
                    productComparisonMatrix[mappedIndexsOfItems.get(j)][mappedIndexsOfItems.get(k)]++;
                }
            }
        }

        // the final result, all of the pairs formatted as [Product1, Product2, # of occurances] for each array in the ArrayList
        ArrayList<String[]> finalResult = new ArrayList<String[]>();

        // adding pairs that occur more than once to finalResult
        boolean continueSearch = true;
        while(continueSearch){
            int maximumMatches = productComparisonMatrix[0][0];
            int product1Index = 0;
            int product2Index = 0;

            // finds the largest number in productComparisonMatrix and stores the corresponding indices
            for(int i = 0; i < productComparisonMatrix.length-1; i++){
                for(int j = i+1; j < productComparisonMatrix.length; j++){
                    if(productComparisonMatrix[i][j] > maximumMatches){
                        maximumMatches = productComparisonMatrix[i][j];
                        product1Index = i;
                        product2Index = j;
                    }
                }
            }

            // resets the occurences of the found pair, so the next iteration finds a different pair
            productComparisonMatrix[product1Index][product2Index] = 0;

            // remaps the found indexes back to their respective product IDs
            int product1Id = Integer.parseInt(indexToIdKey[product1Index][0]);
            int product2Id = Integer.parseInt(indexToIdKey[product2Index][0]);

            // stores the best pair into the next spot of finalResult
            if(maximumMatches > 1){
                finalResult.add(new String[]{String.valueOf(product1Id), String.valueOf(product2Id), String.valueOf(maximumMatches)});
            } else {

                // If there are no more pairs that occur at least twice, the loop is terminated
                continueSearch = false;
            }
        }

        // Converts from an ArrayList<String[]> to String[][]
        String[][] bestPairs = new String[finalResult.size()][];
        for (int i = 0; i < finalResult.size(); i++) {
            bestPairs[i] = finalResult.get(i);
        }
        if (finalResult.size() == 0) {
            System.out.println("No pairs occured more than once in the given range");
        }

        return bestPairs;
    }

    public static void main(String[] args) {
        DatabaseInterface posDatabase = new DatabaseInterface();

        ArrayList<String[]> test = new ArrayList<String[]>();

        test.add(new String[]{"Test", "Test", "Test"});
        test.add(new String[]{"Test2", "Test2", "Test2"});
        test.add(new String[]{"Test3", "Test3", "Test3"});

        ProductPairs pairs = new ProductPairs(posDatabase);
        String[][] bestPairs = pairs.GetBestPairs("2022-06-19","2022-06-20");    

        for(int i = 0; i < bestPairs.length; i++){
            System.out.println("#" + (i+1) + ": " + bestPairs[i][0] + " and " + bestPairs[i][1] + " matched " + bestPairs[i][2] + " times.");
        }
    }
}