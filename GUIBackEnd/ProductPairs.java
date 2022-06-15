import java.util.*;

/**
 * @author Daniel Morrison
 */
public class ProductPairs {
    DatabaseInterface posDatabase;

    ProductPairs(DatabaseInterface posDatabase) {
        this.posDatabase = posDatabase;
    }

    // Returns a 5x3 matrix with each row being a common pair formatted as [product1Id, product2Id, # of occurances]
    public String[][] GetBestPairs(String startDate, String endDate){
       
        // Calculate the start and ending sale IDs given the input dates
        String rangeQuery = "SELECT MIN(saleID), MAX(saleID) FROM saleHistory WHERE saleDate BETWEEN '" + startDate + "' AND '" + endDate + "';";  
        String[][] rangeID = posDatabase.generateQueryMatrix(rangeQuery, "min", "max");
        int startID = Integer.parseInt(rangeID[0][0]);
        int endID = Integer.parseInt(rangeID[0][1]);

        // the matrix numberOfProducts's indexs are mapped to productIDs through this matrix
        String[][] indexToIdKey = posDatabase.getStringMatrix("product","productID");

        // the matrix saleLineItems is a list of the saleLineItems table to be pulled from in the loop
        String[][] saleLineItems = posDatabase.getStringMatrix("saleLineItem","saleID","productID");

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
                    if(j != k){
                        productComparisonMatrix[mappedIndexsOfItems.get(j)][mappedIndexsOfItems.get(k)]++;
                        productComparisonMatrix[mappedIndexsOfItems.get(k)][mappedIndexsOfItems.get(j)]++;
                    }
                }
            }
        }

        // the final result, the top 5 pairs formatted as [Product1, Product2, # of occurances] for each row
        String[][] finalResult = new String[5][3];

        // adding the top 5 pairs to finalResult
        int numberAdded = 0;
        while(numberAdded < 5){
            int maximumMatches = productComparisonMatrix[0][0];
            int product1Index = 0;
            int product2Index = 0;

            // finds the largest number in productComparisonMatrix and stores the corresponding indices
            for(int i = 0; i < productComparisonMatrix.length; i++){
                for(int j = 0; j < productComparisonMatrix.length; j++){
                    if(productComparisonMatrix[i][j] > maximumMatches){
                        maximumMatches = productComparisonMatrix[i][j];
                        product1Index = i;
                        product2Index = j;
                    }
                }
            }

            // resets the occurences of the found pair, so the next iteration finds a different pair
            productComparisonMatrix[product1Index][product2Index] = 0;
            productComparisonMatrix[product2Index][product1Index] = 0;

            // remaps the found indexes back to their respective product IDs
            int product1Id = Integer.parseInt(indexToIdKey[product1Index][0]);
            int product2Id = Integer.parseInt(indexToIdKey[product2Index][0]);

            // stores the best pair into the correct row of finalResult
            finalResult[numberAdded][0] = String.valueOf(product1Id);
            finalResult[numberAdded][1] = String.valueOf(product2Id);
            finalResult[numberAdded][2] = String.valueOf(maximumMatches);

            // Increments and loops again until all of the top 5 pairs have been found
            numberAdded++;
        }
        return finalResult;
    }

    public static void main(String[] args) {
        DatabaseInterface posDatabase = new DatabaseInterface();

        ProductPairs pairFinder = new ProductPairs(posDatabase);
        String[][] bestPairs = pairFinder.GetBestPairs("2022-06-01","2022-06-10");
        for(int i = 0; i < 5; i++){
            System.out.println("#" + (i+1) + ": " + bestPairs[i][0] + " and " + bestPairs[i][1] + " matched " + bestPairs[i][2] + " times.");
        }
    }
}