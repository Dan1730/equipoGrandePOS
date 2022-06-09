SELECT * FROM userinformation WHERE userID = 10; /* username should be javier */
SELECT * FROM userinformation WHERE username = 'hugo'; /* userID should be 6 */
SELECT * FROM saleHistory WHERE saleId = 346; /* saleDate should be 2022-06-09 */
SELECT * FROM saleHistory WHERE saleID = 2; /* revenue should be 43.88951363543289 */
SELECT * FROM saleLineItem WHERE quantity = 1.1610749961068287; /* saleID should be 149 */
SELECT * FROM saleLineItem WHERE saleID = 207; /* productID should be 17 */
SELECT * FROM currentinventory WHERE productID = 53; /* stockQuantity should be 91 */
SELECT * FROM currentinventory WHERE productID = 10; /* restockQuantity should be 142 */
SELECT * FROM vendorhistory WHERE saleID = 1; /* cost should be 2165.0 */
SELECT * FROM vendorhistory WHERE saleDate = 2022-06-05; /* saleID should be 4 */
SELECT * FROM vendorlineitem WHERE productID = 5; /* saleID should be 0 */
SELECT * FROM vendorlineitem WHERE productID = 27; /* quantity should be 50 */
SELECT * FROM product WHERE productID = 53; /* productName should be Yucca */
SELECT * FROM product WHERE productID = 15; /* sellPrice should be 2.15 */
SELECT COUNT(*) AS rowCount FROM userinformation; /* should have 10 rows */
SELECT COUNT(*) AS rowCount FROM saleHistory; /* should have 401 rows */
SELECT COUNT(*) AS rowCount FROM saleLineItem; /* should have 569 rows */
SELECT COUNT(*) AS rowCount FROM currentinventory; /* should have 53 rows */
SELECT COUNT(*) AS rowCount FROM vendorhistory; /* should have 6 rows */
SELECT COUNT(*) AS rowCount FROM vendorlineitem; /* should have 143 rows */
SELECT COUNT(*) AS rowCount FROM product; /* should have 53 rows */

