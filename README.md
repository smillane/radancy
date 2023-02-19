# Radancy Banking System Test

To start, run `docker compose up`

### Example API Endpoints

localhost:8080

\
create user
```
POST
/user/
BODY:sean
```
\
get accounts information
```
GET
/accounts/
header: username, value: sean
```
\
create account
```
POST
/accounts/
header: username, value: sean
```
\
delete account
```
DELETE
/accounts/
header: username, value: sean
```
\
Deposit transaction
```
PUT
/transactions/{accountID}/{transactionValue}/deposit
header: username, value: sean
```
\
Withdraw transaction
```
PUT
/transactions/{accountID}/{transactionValue}/withdraw
header: username, value: sean
```
\
For transactions, only positive numbers and a maximum of 2 decimal places are allowed.\
For user creation, username's are unique.\
AccountID's are generated with UUID, although if using a DB, would be generated in there, with additional fields, such as "account name" that would be mutable.
