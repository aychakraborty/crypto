# crypto recommendation service

This is a crypto recommendation service application for XM project

# Requirements for the recommendation service:

● Reads all the prices from the csv files
● Calculates oldest/newest/min/max for each crypto for the whole month
● Exposes an endpoint that will return a descending sorted list of all the cryptos,
comparing the normalized range (i.e. (max-min)/min)
● Exposes an endpoint that will return the oldest/newest/min/max values for a requested
crypto
● Exposes an endpoint that will return the crypto with the highest normalized range for a
specific day

# Solution:

1. Read all files in resource/files folder.
2. Create a map structure with key as crypto name and value as list of specific crypto records.
3. For each crypto, find out MAX, MIN, OLDEST, NEWEST value using the above map.
4. Store the above result in a map with key as crypto and value as a map with key as MAX, MIN, OLDEST, NEWEST and values as their respective values after calculation.
5. Calculate Normalised value for each crypto as per business logic. Sort the cryptos on basis of the same in descending order and expose the 1st endpoint.
6. Take a request crypto name and fetch the details from above map and display the result exposing the 2nd endpoint.
7. Take a request of a date and create similar list by finding out what are the crypto records that exist for that particular date. Calculate the MAX and MIN and figure out the Normalised value. Use the same to find out the highest normalised crypto for the input date.

# Endpoints:

1. Exposes an endpoint that will return a descending sorted list of all the cryptos,
comparing the normalized range (i.e. (max-min)/min)

Example:
http://localhost:9090/crypto/api/sortcryptos

["ETH","XRP","DOGE","LTC","BTC”]

2. Exposes an endpoint that will return the oldest/newest/min/max values for a requested
crypto

http://localhost:9090/crypto/api/{crypto}

Examples:
http://localhost:9090/crypto/api/BTC

{"OLDEST":"Sat Jan 01 05:00:00 CET 2022","MIN":"33276.59","MAX":"47722.66","NEWEST":"Mon Jan 31 21:00:00 CET 2022"}

http://localhost:9090/crypto/api/ETH

{"OLDEST":"Sat Jan 01 09:00:00 CET 2022","MIN":"2336.52","MAX":"3828.11","NEWEST":"Mon Jan 31 21:00:00 CET 2022”}

3. Exposes an endpoint that will return the crypto with the highest normalized range for a
specific day

http://localhost:9090/crypto/api/date/{date}

Examples:
http://localhost:9090/crypto/api/date/01-01-2022

XRP

# Things to consider with answers:

● Documentation is our best friend, so it will be good to share one for the endpoints

All Endpoints shared in README.md file.

● Initially the cryptos are only five, but what if we want to include more? Will the
recommendation service be able to scale?

Yes, as long as we are keeping all the files in the resources/files folder, everything will be picked up and calculated.

● New cryptos pop up every day, so we might need to safeguard recommendations service
endpoints from not currently supported cryptos

Currently, out of scope of the project.

● For some cryptos it might be safe to invest, by just checking only one month's time
frame. However, for some of them it might be more accurate to check six months or even
a year. Will the recommendation service be able to handle this?

Yes, as long as all the files and the data are consistent, the application will be able to handle of six months or even a year.
