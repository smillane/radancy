# Radancy

To start, run `docker compose up`

### Example API Endpoints

localhost:8080

create user<br />
POST<br />
/user/<br />
BODY:sean<br />

get accounts information<br />
GET<br />
/accounts/<br />
header: username, value: sean<br />

create account<br />
POST<br />
/accounts/<br />
header: username, value: sean<br />

delete account<br />
DELETE<br />
/accounts/<br />
header: username, value: sean<br />

Deposit transaction<br />
PUT<br />
/transactions/{accountID}/{transactionValue}/deposit<br />
header: username, value: sean<br />

Withdraw transaction<br />
PUT<br />
/transactions/{accountID}/{transactionValue}/withdraw<br />
header: username, value: sean<br />

For transactions, only positive numbers and a maximum of 2 decimal places are allowed.
