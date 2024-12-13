# NBP API Exchangerates APP

This project implements a RESTful application as part of a recruitment task. It interacts with the National Bank of
Poland (NBP) API to fetch currency exchange rates, stores requests in a database, and exposes endpoints for retrieving
and querying this data. The application is developed using Java, Spring Boot, Maven, and supports data persistence via a
relational database.
![NBP API Exchangerates APP](https://raw.githubusercontent.com/terabajt/NbpApiExchangerates/refs/heads/main/media/NbpApiImage.gif)

Front in Angular is available at [NbpApiExchangeratesFront](https://github.com/terabajt/NbpApiExchangeratesFront)

## Endpoint 1: Fetch Currency Value

### Path: POST /currencies/get-current-currency-value-command

#### Description: Fetches the current exchange rate for a given currency using the NBP API.

Request Body:

```{
"currency": "EUR",
"name": "Jan Nowak"
}
```

Response:

```
{
"value": 4.2954
}
```

### Additional Features:

#### Logs the request data in a database, including:

Currency code.
Requestor's name.
Request timestamp.
Retrieved exchange rate.

Returns an error if the requested currency is invalid.

## Endpoint 2: Retrieve Request History

### Path: GET /currencies/requests

#### Description: Returns all past requests stored in the database.

Response:

```
[
{
"currency": "EUR",
"name": "Jan Nowak",
"date": "2022-01-01T10:00:00.000Z",
"value": 4.2954
}
]
```

## Project Setup

### Prerequisites

Java 17 or higher
Maven 3.6+
Database: H2 (in-memory for testing) or any supported relational database.

## API Usage

### Fetch Current Currency Value

Request:

```
curl -X POST http://localhost:8080/currencies/get-current-currency-value-command \
-H "Content-Type: application/json" \
-d '{
"currency": "USD",
"name": "John Doe"
}'
```

Response:

```
{
"value": 3.9876
}
```

### Retrieve Request History

Request:

```
curl -X GET http://localhost:8080/currencies/requests
```

Response:

```
[
{
"currency": "USD",
"name": "John Doe",
"date": "2024-12-01T14:00:00.000Z",
"value": 3.9876
}
]
```

### Implementation Details

#### Architecture

##### Controller Layer:

Handles incoming HTTP requests and returns appropriate responses.

##### Service Layer:

Contains business logic for fetching and saving currency data.

##### Repository Layer:

Handles database operations using Spring Data JPA.

#### Technologies Used

##### Spring Boot: Framework for building RESTful web services.

##### Spring Data JPA: Simplifies database interactions.

##### H2 Database: In-memory database for quick testing and development.

##### Maven: Build and dependency management tool.

### Flow for Fetching Currency

- The user sends a POST request with the currency code and their name.
- The application calls the NBP API (http://api.nbp.pl/api/exchangerates/tables/A?format=json) to fetch the exchange
  rate.
- The result is logged in the database and returned to the user.
- Error Handling
- Returns appropriate HTTP status codes and messages for:
- Invalid currency codes.
- Database errors.
- Internal server errors.

### Future Enhancements

- Add authentication and authorization.
- Support for multiple APIs for currency exchange rates.
- Implement a caching layer to reduce API calls to the NBP API.

### Postman Collections

You can import the Postman collections (in JSON format) from the `/postman` directory (located in the root of the
project) and use the built-in tests.

### Docker

To run docker use

```
sudo docker build -t spring-docker-maven-app .
```

and

``` 
docker run -p 8080:8080 spring-docker-maven-app
```

to run Docker on 8080 port.

For questions or further information, contact me at michalpasynek@gmail.com.