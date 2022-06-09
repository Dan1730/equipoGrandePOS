from dataclasses import dataclass
from enum import Enum
import csv

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
    with open("ProductData.csv") as csvfile:
        reader = csv.reader(csvfile)
        reader.__next__() # skip the header in the csv
        for row in reader:

            # create the data needed for the product and cast it to the approproate type
            productID = int(row[0])
            name = row[1]
            sellPrice = row[2]
            sellUnit = Unit(int(row[3]))
            purchasePrice = float(row[4])
            purchaseUnit = Unit(int(row[5]))

            # create the product and add it to productsList
            productsList.append(Product(productID, name, sellPrice, sellUnit, purchasePrice, purchaseUnit))

    return productsList

def main():

    #Read CSV of price data of items
    productsList = readProductCSV()

    #Create fake vendor transactions for 3 weeks
    #Create fake customer transactions for 3 weeks

    # inventory = dict.fromkeys(productsList, 0)
    # print(inventory)

    #Chose two days to be holidays and modify history accordingly, accounting for changes in purchase trends

if __name__ == "__main__":
    main()