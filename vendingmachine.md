# Vending Machine Kata
Based on https://github.com/guyroyse/vending-machine-kata

In this exercise you will build the brains of a vending machine.  It will accept money, make change, maintain
inventory, and dispense products.  All the things that you might expect a vending machine to accomplish.

The point of this kata to to provide a larger than trivial exercise that can be used to practice TDD.  A significant
portion of the effort will be in determining what tests should be written and, more importantly, written next.

## Features

### Accept Coins

_As a vendor_  
_I want a vending machine that accepts coins_  
_So that I can collect money from the customer_

The vending machine will accept valid coins (5c - 2euro) and reject invalid ones (1c,2c, parking tokens, ...).  When a
valid coin is inserted the amount of the coin will be added to the current amount and the display will be updated.
When there are no coins inserted, the machine displays INSERT COIN.  Rejected coins are placed in the coin return.

NOTE: The temptation here will be to create Coin objects that know their value.  However, this is not how a real
vending machine works.  Instead, it identifies coins by their weight and size and then assigns a value to what
was inserted.  You will need to do something similar.  This can be simulated using strings, constants, enums,
symbols, or something of that nature. See bottom for coin properties.

### Select Product

_As a vendor_  
_I want customers to select products_  
_So that I can give them an incentive to put money in the machine_

There are three products: cola for $1.00, chips for $0.50, and candy for $0.65.  When the respective button is pressed
and enough money has been inserted, the product is dispensed and the machine displays THANK YOU.  If the display is
checked again, it will display INSERT COIN and the current amount will be set to $0.00.  If there is not enough money
inserted then the machine displays PRICE and the price of the item and subsequent checks of the display will display
either INSERT COIN or the current amount as appropriate.

### Make Change

_As a vendor_  
_I want customers to receive correct change_  
_So that they will use the vending machine again_

When a product is selected that costs less than the amount of money in the machine, then the remaining amount is placed
in the coin return.

### Return Coins

_As a customer_  
_I want to have my money returned_  
_So that I can change my mind about buying stuff from the vending machine_

When the return coins button is pressed, the money the customer has placed in the machine is returned and the display shows
INSERT COIN.

### Sold Out

_As a customer_  
_I want to be told when the item I have selected is not available_  
_So that I can select another item_

When the item selected by the customer is out of stock, the machine displays SOLD OUT.  If the display is checked again,
it will display the amount of money remaining in the machine or INSERT COIN if there is no money in the machine.

### Exact Change Only

_As a customer_  
_I want to be told when exact change is required_  
_So that I can determine if I can buy something with the money I have before inserting it_

When the machine is not able to make change with the money in the machine for any of the items that it sells, it will
display EXACT CHANGE ONLY instead of INSERT COIN.

## Coin Properties

| Denomination | Color  | Secondary Color | Diameter (mm) | Thickness (mm) | Mass (g) |
|--------------|--------|-----------------|---------------|----------------|----------|
| 1c           | Bronze | None            | 16.25         | 1.67           | 2.30     |
| 2c           | Bronze | None            | 18.75         | 1.67           | 3.06     |
| 5c           | Bronze | None            | 21.25         | 1.67           | 3.92     |
| 10c          | Gold   | None            | 19.75         | 1.93           | 4.10     |
| 20c          | Gold   | None            | 22.25         | 2.14           | 5.74     |
| 50c          | Gold   | None            | 24.25         | 2.38           | 7.80     |
| €1           | Silver | Gold            | 23.25         | 2.33           | 7.50     |
| €2           | Gold   | Silver          | 25.75         | 2.20           | 8.50     |

Source: https://en.wikipedia.org/wiki/Euro_coins
