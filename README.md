# RewardEngine
**Rewards Calculator based on customer transaction**
A retailer offers a reward program to its customers, awarding points based on each recorded purchase or transaction.
Rewards Calculator based on customer transaction
#The rest API to get customer rewards month wise for three months based on customer Id

#A retailer offers a rewards program to its customers, awarding points based on each recorded purchase. A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent over $50 in each transaction (e.g. a $140 purchase = 2x$40 + 1x$50 = 130 points). Given a record of every transaction during a three month period, calculate the reward points earned for each customer per month and total.

The package name is structured as com.retailer.rewardengine
Exception is thrown if customer does not exists.
H2 in-memory database to store the information.
Install H2 db locally and run it . change the db settings in application.yml file.
http://localhost:8080/reward-engine/customers/{customerId}/rewards