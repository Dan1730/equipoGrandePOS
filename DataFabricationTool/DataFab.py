from dataclasses import dataclass
from enum import Enum
import csv
import random
import os

#define constants
#file names
PRODUCT_DATA_FILE_NAME = 'ProductData.csv'
SALES__FILE_NAME = 'Sales.csv'
SALES_LINE_ITEMS_FILE_NAME = 'SaleslineItems.csv'
VENDOR_TRANSACTIONS_FILE_NAME = 'VendorTransactions.csv'
VENDOR_TRANSACTIONS_LINE_ITEMS_FILE_NAME = 'VendorTransactionsLineItems.csv'

#simulation values
RESTOCK_QUANTITY = 50
CUSTOMER_BUDGET_MIN = 15.0
CUSTOMER_BUDGET_MAX = 30.0
CUSTOMER_ITEM_KG_MIN = 1.0
CUSTOMER_TIEM_KG_MAX = 8.0

# Enum that stores the different units the shop uses. 
class Unit(Enum):
    INDIVIDUAL = 0          # sell per package or item
    KG = 1                  # per kg

# dataclass to store the properties of a product
# a dataclass basically allows you to use a class in python like a c-style struct
@dataclass
class Product:
    productID: int
    name: str
    sellPrice: float
    sellUnit: Unit
    purchasePrice: float
    purchaseUnit: Unit

    # used to create the inventory using a dictionary
    def __hash__(self):
        return self.productID

# Returns a list of Products.
# Assumes that the csv file provided is the correct one exported from google sheets and is in the
# correct format
def readProductCSV() -> list:
    productsList = []
    with open(PRODUCT_DATA_FILE_NAME) as csvfile:
        reader = csv.reader(csvfile)
        reader.__next__() # skip the header in the csv
        for row in reader:

            # create the data needed for the product and cast it to the approproate type
            productID = int(row[0])
            name = row[1]
            sellPrice = float(row[2])
            sellUnit = Unit(int(row[3]))
            purchasePrice = float(row[4])
            purchaseUnit = Unit(int(row[5]))

            # create the product and add it to productsList
            productsList.append(Product(productID, name, sellPrice, sellUnit, purchasePrice, purchaseUnit))

    return productsList



vendorCurrentDay = 0
vendorTxID = 0
def buyProduct(inventory: dict, product: Product, day: int):

    # so the function can access these variables
    global vendorCurrentDay
    global vendorTxID

    inventory[product] += RESTOCK_QUANTITY
    
    if vendorCurrentDay != day:
        vendorCurrentDay = day
        vendorTxID += 1

    # add transacation to CSV
    with open(VENDOR_TRANSACTIONS_LINE_ITEMS_FILE_NAME, "a") as vendorTransactionLineItemsFile:
        vendorTransactionLineItems = csv.writer(vendorTransactionLineItemsFile)
        vendorTransactionLineItems.writerow([vendorTxID, product.productID, 50])

# restock if inentory is too low
def restock(inventory: dict, day: int):
    for product in inventory:
        if inventory[product] < 10:
            buyProduct(inventory, product, day)

# create a sell to a customer
customerTxID = 0
def createSell(inventory: dict):

    # so the function can access these variables
    global customerTxID

    endTransaction = False

    customerBudget = random.uniform(CUSTOMER_BUDGET_MIN, CUSTOMER_BUDGET_MAX) #decide a budget for the customer
    while customerBudget > 0.0:
        # choose a product
        # if product is unavailiable, choose a different product, but if no products are availible,
        # end transaction
        productsChecked = 1
        desiredProduct = random.choice(list(inventory.keys()))
        while(inventory[desiredProduct] <= 1.0):
            desiredProduct = random.choice(list(inventory.keys()))
            productsChecked += 1
            if productsChecked == len(inventory):
                endTransaction = True
                break

        if endTransaction:
            break

        # choose a quantity
        desiredQuantity = random.uniform(CUSTOMER_ITEM_KG_MIN, max(CUSTOMER_TIEM_KG_MAX, inventory[desiredProduct]))
        # add to transaction
        with open ("SalesLineItems.csv", "a") as salesLineItemsFile:
            salesLineItems = csv.writer(salesLineItemsFile)
            salesLineItems.writerow([customerTxID, desiredProduct.productID, desiredQuantity])

        customerBudget -= desiredProduct.sellPrice * desiredQuantity # deduct from the customer's budget
        inventory[desiredProduct] -= desiredQuantity

    customerTxID += 1 # for the next transaction


def simulateDay(inventory: dict, day: int, multiplier: float):
    for customer in range(int(random.randint(19, 25) * multiplier)): #for each customer. Number of customers varies
        createSell(inventory)

def main():

    # remove old csv files first
    if os.path.exists(SALES_LINE_ITEMS_FILE_NAME):
        os.remove(SALES_LINE_ITEMS_FILE_NAME)

    if os.path.exists(VENDOR_TRANSACTIONS_LINE_ITEMS_FILE_NAME):
        os.remove(VENDOR_TRANSACTIONS_LINE_ITEMS_FILE_NAME)

    #Read CSV of price data of items
    productsList = readProductCSV()

    #Create fake vendor transactions for 3 weeks
    #Create fake customer transactions for 3 weeks
    #Chose two days to be holidays and modify history accordingly, accounting for changes in purchase trends

    inventory = dict.fromkeys(productsList, 0)
    for i in range(21):
        multiplier = 1.0 # used for days preceding holidays to increase sales
        if i % 7 == 0: #Sunday
            restock(inventory, i) #restock on sundays. Don't sell
            continue
        if i == 8 or i == 15: #holidays (last two mondays). Do not do anything
            continue
        if i == 6 or i == 13: #saturdays before holidays
            multiplier = 2.0
        if i % 7 == 3: #Wednesday
            restock(inventory, i) # restock on wednesday

        simulateDay(inventory, i, multiplier)


if __name__ == "__main__":
    main()