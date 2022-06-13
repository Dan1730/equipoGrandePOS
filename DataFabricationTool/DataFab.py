from dataclasses import dataclass
from enum import Enum
import csv
import random
import os
import datetime

# define constants
# file names
PRODUCT_DATA_FILE_NAME = '../DatabaseTool/InitialDatabase/product.csv'
SALES_FILE_NAME = '../DatabaseTool/InitialDatabase/salehistory.csv'
SALES_LINE_ITEMS_FILE_NAME = '../DatabaseTool/InitialDatabase/salelineItem.csv'
VENDOR_TRANSACTIONS_FILE_NAME = '../DatabaseTool/InitialDatabase/vendorhistory.csv'
VENDOR_TRANSACTIONS_LINE_ITEMS_FILE_NAME = '../DatabaseTool/InitialDatabase/vendorlineitem.csv'

# simulation values
NUM_DAYS_TO_SIMULATE = 21

RESTOCK_QUANTITY = 20
CUSTOMER_BUDGET_MIN = 8.0
CUSTOMER_BUDGET_MAX = 12.0
CUSTOMER_ITEM_KG_MIN = 1.0
CUSTOMER_TIEM_KG_MAX = 7.0

# The simulation is based on three weeks of data and each day is just incremented by one.
# This function converts the number to the correct date, and makes the simulation start 3 Sundays
# before today. 
def DayNumberToDate(num: int) -> str:
    currentDateIRL = datetime.datetime.today()
    weekday = (currentDateIRL.weekday() + 1) % 7 # make it where Sunday is day 0
    startDate = currentDateIRL - datetime.timedelta(days=(weekday + 14))

    dateToReturn = startDate + datetime.timedelta(days=num)

    return dateToReturn.isoformat()[0:10] # get iso format and substring the date

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
        desiredQuantity = random.uniform(CUSTOMER_ITEM_KG_MIN, min(CUSTOMER_TIEM_KG_MAX, inventory[desiredProduct]))

        #if product is an individually sold item, just truncate the float to an int
        if desiredProduct.sellUnit == Unit.INDIVIDUAL:
            desiredQuantity = int(desiredQuantity)

        # add to transaction
        with open (SALES_LINE_ITEMS_FILE_NAME, "a") as salesLineItemsFile:
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
    if os.path.exists(SALES_FILE_NAME):
        os.remove(SALES_FILE_NAME)

    if os.path.exists(VENDOR_TRANSACTIONS_FILE_NAME):
        os.remove(VENDOR_TRANSACTIONS_FILE_NAME)

    if os.path.exists(SALES_LINE_ITEMS_FILE_NAME):
        os.remove(SALES_LINE_ITEMS_FILE_NAME)

    if os.path.exists(VENDOR_TRANSACTIONS_LINE_ITEMS_FILE_NAME):
        os.remove(VENDOR_TRANSACTIONS_LINE_ITEMS_FILE_NAME)

    random.seed(23) # seed the RNG to generate the same data every time

    #Read CSV of price data of items
    productsList = readProductCSV()

    #Create fake vendor transactions for 3 weeks
    #Create fake customer transactions for 3 weeks
    #Chose two days to be holidays and modify history accordingly, accounting for changes in purchase trends

    lastCustomerTxIDEachDay = {}
    lastVendorTxIDEachDay = {}

    inventory = dict.fromkeys(productsList, 0)
    for i in range(NUM_DAYS_TO_SIMULATE):
        multiplier = 1.0 # used for days preceding holidays to increase sales
        if i % 7 == 0: #Sunday
            restock(inventory, i) #restock on sundays. Don't sell
            lastVendorTxIDEachDay[i] = vendorTxID
            continue
        if i % 7 == 1 and i > (NUM_DAYS_TO_SIMULATE - 14): #holidays (last two mondays). Do not do anything
            continue
        if (i % 7 == 6) and (i > NUM_DAYS_TO_SIMULATE - 21) and (i < NUM_DAYS_TO_SIMULATE - 7): #the two saturdays before holidays. By selecting the saturday in the second to last and third to last week.
            multiplier = 2.0
        if i % 7 == 3: #Wednesday
            restock(inventory, i) # restock on wednesday
            lastVendorTxIDEachDay[i] = vendorTxID

        simulateDay(inventory, i, multiplier)
        lastCustomerTxIDEachDay[i] = customerTxID

    # use the itemized csv files that have just been created to create the overall Sales csv
    with open(SALES_FILE_NAME, "w") as salesFile, open(SALES_LINE_ITEMS_FILE_NAME, "r") as salesLineItemsFile:
        sales = csv.writer(salesFile)
        salesLines = csv.reader(salesLineItemsFile)

        # create an iterator for each day that there was a customer sale
        validDaysIter = iter(lastCustomerTxIDEachDay.keys())
        currDay = next(validDaysIter)

        # get the last tx id for the current day
        lastCustTxIdToday = lastCustomerTxIDEachDay[currDay]

        # create a dict to track the totals for transaction ids and what day they happened on
        # key is the tx id, value is a list with the total and day
        # format is {txID: [total, day]}
        txIDTotals = {}

        for row in salesLines:

            # get the data from the csv
            txID = int(row[0])
            productID = int(row[1])
            quantity = float(row[2])

            # if we are past the last tx id for current day, advance to the next day with sales
            if(int(txID) > lastCustTxIdToday):
                currDay = next(validDaysIter)
                lastCustTxIdToday = lastCustomerTxIDEachDay[currDay]

            # if the txID is not yet in the dictionary, add it. If it is, add to its quantity
            if not txID in txIDTotals:
                txIDTotals[txID] = [next(product for product in productsList if product.productID == productID).sellPrice * quantity, currDay]
            else:
                txIDTotals[txID][0] += next(product for product in productsList if product.productID == productID).sellPrice * quantity

        # write out the data to the CSV in the correct format for the DB
        for txID in txIDTotals:
            sales.writerow([txID, DayNumberToDate(txIDTotals[txID][1]), txIDTotals[txID][0]])
        
    # exact same logic as above for the vendor transactions csv
    with open(VENDOR_TRANSACTIONS_FILE_NAME, "w") as vendorTransFile, open(VENDOR_TRANSACTIONS_LINE_ITEMS_FILE_NAME, "r") as vendorTransItemsFile:
        vendorTrans = csv.writer(vendorTransFile)
        vendorTransLines = csv.reader(vendorTransItemsFile)

        validDaysIter = iter(lastVendorTxIDEachDay.keys())
        currDay = next(validDaysIter)
        lastVendorTxIdToday = lastVendorTxIDEachDay[currDay]

        txIDTotals = {}

        for row in vendorTransLines:
            txID = int(row[0])
            productID = int(row[1])
            quantity = float(row[2])

            if(txID > lastVendorTxIdToday):
                currDay = next(validDaysIter)
                lastVendorTxIdToday = lastVendorTxIDEachDay[currDay]

            if not txID in txIDTotals:
                txIDTotals[txID] = [next(product for product in productsList if product.productID == productID).sellPrice * quantity, currDay]
            else:
                txIDTotals[txID][0] += next(product for product in productsList if product.productID == productID).sellPrice * quantity


        for txID in txIDTotals:
            vendorTrans.writerow([txID, DayNumberToDate(txIDTotals[txID][1]), txIDTotals[txID][0]])


if __name__ == "__main__":
    main()