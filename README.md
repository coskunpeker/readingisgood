# Reading is Good Case Study

This project is a basic implementation of an ecommerce application. It contains domains of Stock, Customer, Order and
Statistic.

### Tech Stack

* Java 11
* Spring Boot
* Maven
* Docker
* H2 In Memory Database
* Swagger Open API Specification
* JWT

### Design

### Authentication

* JWT Token validation is used for authentication.

#### Customer

* This module is responsible to creating new customers, authorizing them and querying their orders.

#### Book

* This module is responsible to creating and updating book stocks.

#### Order

* This module is responsible to creating and querying orders.
* In order to prevent the **race condition** on book stock, **Pessimistic Lock Mechanism** is used. The preference of
  **Pessimistic Lock Mechanism** over **Optimistic Lock Mechanism**
  is related to the scope of the case study.

#### Statistics

* This module is responsible to querying monthly statistics of books and orders.
* The basic implementation of [CQRS Pattern](https://microservices.io/patterns/data/cqrs.html) is used to store and
  query statistic data.


#### Success & Error Responses

* HTTP Status Code 200 is used for *Success Response*.
* HTTP Status Code related to error is used for *Error Responses*.

### Postman Requests

* Postman requests are stored under the **postman_collections** directory.

### Initial Database Records

* When the application is run, two customers are registered in the database. 
  They can be used in postman requests if needed.

````
custumer_1:
  email: test@gmail.com
  password: test
  
custumer_2:
  email: test2@gmail.com
  password: test2
````

### How to run the application?

#### Project Requirements

* Docker

#### Commands

* change the current working directory to project's root folder and run the command below:

````
docker compose up -d 
````

* stopping the application:

```
docker compose down
```
### Note:
>The application needs to download some dependencies. Accordingly, command *docker compose up -d* may take a while to complete 

