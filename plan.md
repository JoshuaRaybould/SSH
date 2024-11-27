# Plan 
## Database Plan 

### Existing Systems
- SSH camera, generate random data in java -> put into database 
- SSH Cloud Database 
- Web API -> will be simulated in the terminal 

The process of how the app would work. Going from the SSH camera – to the cloud – to the app. 

If we were to do it like that – when would we update the app? Once opened or daily. 
Initially agreed on when opened. However, realised that if lots of users – it will slow it down. So we instead plan to work out quality when an item is first added to the fridge, and calculate the quality of all items once a day. \
How on earth is the data stored from the fridge camera? – Update Ben reread the document, it should be simple. The data will be stored in a fridge\_items table and other databse tables in the cloud database. 

### Database layout 

“tenants\_fridge\_items”: \
tenant\_id \
fridge\_item\_id \
quantity \
date\_time \
quality

“fridge\_item”: \
fridge\_item\_id \
price \
fridge_item_name \
estimated\_shelf\_life 

“tenants”: \
tenant\_id \
tenant\_name 

“recipes”: \
recipe\_id \
recipe\_name \
recipe\_instructions 

"recipes_items": \
recipe\_id \
item\_id

(Possibly)” Recipe Log Book: 

The idea is that we don’t want to be repeating the same recipes every time to the same user so it would include new fields with recipe\_id and recipe\_rating. But as this isn’t a key feature we have decided to save it for if we have extra time at the end. 

## Calculations Plan 

Changing the calculations as we have come across some problems. The intention is to use the items that are going out of date rather than prioritising the fresher items (which is what the calculations do as of now). 

New conditions being implemented too.  

M: Recipe match rating, ranging from 0-100% \
M = (Sum of U/ N)\
N: Number of fridge ingredients 

U: If ingredient unavailable or Q=0 then 0 \
&nbsp;&nbsp;&nbsp;&nbsp; else U = 1 – Q   

Q: Quality of the ingredient, ranging from 0-100% \
Q = (1 - Days since captured/Estimated Shelf Life + 1) 

## User-Interface Plan 

A table of recipes. Ordered by the M number 
(Possibly allowing them to see their ingredients with quality scores) 

## Work Plan 

Database Unit(Docker): Josh. \
Manual population of items: Arman \
Recipes: Ben \
Logic to randomly generate items: Arman \
Logic to calculate Quality (and put data into database): Jamal \
Logic to get it out the database and calculate and M value: Jamal \
Web API: Josh \
JavaFX: Ben 
