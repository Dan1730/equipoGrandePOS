SELECT * FROM userinformation WHERE userID = 10; -- username should be javier
SELECT * FROM userinformation WHERE username = 'hugo'; -- userID should be 6
SELECT * FROM saleHistory WHERE saleId = 346; -- saleDate should be 2022-06-09
SELECT * FROM saleHistory WHERE saleID = 2; -- revenue should be 20.545952763848877
SELECT * FROM saleLineItem WHERE quantity = 3.5568546819209974; -- saleID should be 129
SELECT * FROM saleLineItem WHERE saleID = 207; -- productIDs should be 12 and 47
SELECT * FROM currentinventory WHERE productID = 53; -- stockQuantity should be 91
SELECT * FROM currentinventory WHERE productID = 10; -- restockQuantity should be 142
SELECT * FROM vendorhistory WHERE saleID = 1; -- cost should be 2003.5
SELECT * FROM vendorhistory WHERE saleID = 4; -- date should be 2022-06-05
SELECT * FROM vendorlineitem WHERE productID = 5; -- saleIDs should be 0, 2, 4
SELECT * FROM vendorlineitem WHERE productID = 24; -- saleIDs should be 0, 1, 4
SELECT * FROM product WHERE productID = 53; -- productName should be Yucca
SELECT * FROM product WHERE productID = 15; -- sellPrice should be 2.15
SELECT COUNT(*) AS rowCount FROM userinformation; -- should have 10 rows
SELECT COUNT(*) AS rowCount FROM saleHistory; -- should have 409 rows
SELECT COUNT(*) AS rowCount FROM saleLineItem; -- should have 596 rows
SELECT COUNT(*) AS rowCount FROM currentinventory; -- should have 53 rows
SELECT COUNT(*) AS rowCount FROM vendorhistory; -- should have 6 rows
SELECT COUNT(*) AS rowCount FROM vendorlineitem; -- should have 145 rows
SELECT COUNT(*) AS rowCount FROM product; -- should have 53 rows
SELECT productName, stockQuantity FROM Product p, currentinventory c WHERE p.productID = c.productID; -- displays every product and the current inventory for that product
SELECT saleID FROM saleHistory WHERE saleDate <= 2022-06-07 AND saleDate >= 2022-06-01;
SELECT productName, sum(quantity) FROM Product p, salelineitem s WHERE s.productID = p.productID GROUP BY p.productName; -- displays every product and how much of each product has been sold at the store
SELECT productName, count(s.*) FROM Product p, salelineitem s WHERE s.productID = p.productID GROUP BY p.productName; -- displays every product and how many unique orders each has been in
SELECT saleDate, count(l.*) FROM salehistory s, salelineitem l WHERE s.saleID = l.saleID GROUP BY saledate ORDER BY saledate; -- displays every date in the shops history and how many unique items were sold on that day
SELECT saleDate AS VendorDate, count(l.*) AS uniqueItems FROM vendorhistory v, vendorlineitem l WHERE v.saleID = l.saleID GROUP BY saledate ORDER BY saledate; -- displays every date where items were ordered from the vendor and how many unique items were ordered
SELECT productname FROM product WHERE EXISTS (SELECT productid FROM currentinventory WHERE product.productid = currentinventory.productid AND stockquantity > 100); -- displays every product name that has over 100 items currently in stock
SELECT saleDate, count(l.*) AS apples FROM saleHistory h, saleLineItem l WHERE h.saleID = l.saleID AND l.productID = 1 GROUP BY saleDate; -- displays the amount of apples sold each day
SELECT productname FROM product WHERE EXISTS (SELECT productid FROM currentinventory WHERE product.productid = currentinventory.productid AND stockquantity < 80 AND sellprice < 2); -- displays every product name that has less than 80 units in stock and a sell price less than 2
SELECT productName, count(s.*) FROM Product p, vendorlineitem s WHERE s.productID = p.productID GROUP BY p.productName; -- displays every product and how many times it has been restocked
SELECT productName, AVG(s.quantity) FROM product p, saleLineItem s WHERE p.productID = s.productID GROUP BY productName; -- displays every product and the average quantity that it is bought in
SELECT productid FROM salelineitem WHERE EXISTS (SELECT saleid FROM salehistory WHERE salehistory.saleid = salelineitem.saleid AND revenue > 40); -- displays every product that was involved in a sale of more than 40 euros
SELECT productid FROM salelineitem WHERE EXISTS (SELECT saleid FROM salehistory WHERE salehistory.saleid = salelineitem.saleid AND revenue > 30 AND saledate = '2022-05-23'); -- displays every product involved in a sale of more than 30 euros on May 23rd, 2022
SELECT saleDate, SUM(p.sellPrice * l.quantity) AS Profit FROM product p, saleHistory h, saleLineItem l WHERE p.productID = l.productID AND h.saleID = l.saleID GROUP BY saleDate; -- displays each sale date and the revenue from that day
SELECT productName, sum(quantity) FROM salelineitem s, salehistory h, product p  WHERE h.saleDate = '2022-05-23' AND s.saleID = h.saleID AND p.productID = s.productID GROUP BY p.productName; -- displays each product name and the quantity sold in a given day
SELECT saleDate, SUM(p.purchasePrice * l.quantity) AS Cost FROM product p, vendorHistory h, vendorLineItem l WHERE p.productID = l.productID AND h.saleID = l.saleID GROUP BY saleDate ORDER BY SaleDate; -- displays the cost of each vendor purchase
SELECT productname FROM product WHERE EXISTS (SELECT productid FROM vendorlineitem WHERE product.productid = vendorlineitem.productid AND saleid = 1); -- displays product names for all products purchased in a given vendor transaction

--Sale History SQL Demo
SELECT productName, sum(quantity) as amountSold, sum(quantity*sellprice) as revenue  FROM Product p, salelineitem s WHERE s.productID = p.productID AND s.saleID BETWEEN 95 AND 186 GROUP BY p.productName;

SELECT productName, stockquantity, sum(quantity) as amountSold, sum(quantity*sellprice) as revenue  FROM Product p, currentinventory c, salelineitem s WHERE s.productID = p.productID AND c.productID = p.productID  AND s.saleID BETWEEN 95 AND 186 GROUP BY p.productName;

SELECT MIN(saleID), MAX(saleID) FROM saleHistory WHERE saleDate BETWEEN '2022-06-03' AND '2022-06-07';  

