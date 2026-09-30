# E-Commerce Inventory Service

Inventory management microservice for an e-commerce application built using **Java, Spring Boot and MySQL**.

This service manages product stock and handles inventory operations required during the order process.

## Features

* Inventory creation and management
* Product stock tracking
* Check available stock
* Reserve product stock
* Update inventory quantity
* REST APIs
* JWT-based authentication
* Role-based authorization
* MySQL database integration
* Spring Data JPA
* Eureka Service Discovery
* Spring Boot Actuator

## APIs

| API                             | Description                    |
| ------------------------------- | ------------------------------ |
| `POST /stock/create`            | Create inventory for a product |
| `GET /stock/getInventories`     | Get all inventory records      |
| `GET /stock/getstock/{skuCode}` | Get stock by SKU code          |
| `PUT /stock/update/{id}`        | Update inventory               |
| `POST /stock/reserve`           | Reserve available stock        |

> API paths may vary depending on the current controller configuration.

## Service Communication

The Inventory Service communicates with other microservices as part of the e-commerce order flow.

```text
Product Service :8083
        |
        | OpenFeign
        ↓
Inventory Service :8084
        |
        ↓
Stock Management
```

During the order flow, inventory is responsible for checking and managing available product stock.

## Technologies

* Java 17
* Spring Boot 3.5.6
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* MySQL
* Eureka Service Discovery
* Maven
* Spring Boot Actuator
* Apache Kafka

## Configuration

Main configuration file:

```text
src/main/resources/application.yml
```

Default service port:

```text
8084
```

Local URL:

```text
http://localhost:8084
```

## Running the Service

Make sure MySQL, Eureka Server and required dependent services are running.

Run using Maven:

```bash
mvn spring-boot:run
```

Or on Windows:

```bash
mvnw.cmd spring-boot:run
```

## Project Role

The Inventory Service is responsible for managing product stock in the E-Commerce Microservices application.

It plays an important role in the order processing flow by checking available stock and managing inventory quantities.


