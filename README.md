# Radancy Banking System Test

To start, run `docker compose up`

### Example API Endpoints

localhost:8080

\
Create User
```
POST
/user/
BODY:sean
```
\
Get Accounts Information
```
GET
/accounts/
header: username, value: sean
```
\
Create Account
```
POST
/accounts/
header: username, value: sean
```
\
Delete Account
```
DELETE
/accounts/
header: username, value: sean
```
\
Deposit Transaction
```
PUT
/transactions/{accountID}/{transactionValue}/deposit
header: username, value: sean
```
\
Withdraw Transaction
```
PUT
/transactions/{accountID}/{transactionValue}/withdraw
header: username, value: sean
```
\
For transactions, only positive numbers and a maximum of 2 decimal places are allowed.\
For user creation, username's are unique.\
AccountID's are generated with UUID, although if using a DB, would be generated in there, with additional fields, such as "account name" that would be mutable.
