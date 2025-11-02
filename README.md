# Order Enricher Service

A quick Spring Boot 3 / Java 21 microservice that enriches customer orders with product and customer data before persisting them.

---

## Overview

The Order Enricher Service receives lightweight `OrderRequest` payloads and expands them into rich `OrderResponse` objects containing customer details, product metadata, and timestamps.

It provides REST endpoints for:

* Creating enriched orders
* Retrieving an order by ID
* Querying orders by customer or product
* Handling validation and errors consistently

---

## Key Features

* Spring Boot 3 and Java 21
* Lombok for concise data models and builders
* Jakarta Validation for input verification
* Spring Web MVC REST API
* JPA/Hibernate entity mapping
* In-memory or mock repository support
* Comprehensive JUnit 5 and Mockito test coverage
* Deterministic JSON serialization for predictable tests

---

## Project Structure

```
order-enricher-service
├── src
│   ├── main
│   │   ├── java/com/teamviewer/order_enricher_service
│   │   │   ├── controller/OrderController.java
│   │   │   ├── service/
│   │   │   ├── model/EnrichedOrderEntity.java
│   │   │   ├── repository/EnrichedOrderRepository.java
│   │   │   ├── DTO/
│   │   │   └── OrderEnricherServiceApplication.java
│   │   └── resources/application.properties
│   └── test/java/com/teamviewer/order_enricher_service
│       ├── controller/OrderControllerTest.java
│       ├── service/OrderEnrichmentServiceTest.java
│       ├── service/ProductServiceTest.java
│       ├── service/CustomerServiceTest.java
│       └── OrderEnricherServiceApplicationTests.java
├── build.gradle
└── settings.gradle
```

---

## Core Components

| Layer      | Description                                                           |
| ---------- | --------------------------------------------------------------------- |
| Controller | REST endpoints for creating and retrieving orders                     |
| Service    | Business logic combining customer, product, and enrichment operations |
| Repository | Persistence or mock data access layer                                 |
| Model      | `EnrichedOrderEntity` JPA representation                              |
| DTOs       | Immutable records for requests and responses                          |
| Tests      | JUnit 5 tests with Mockito and MockMvc                                |


## Tech Stack

| Category   | Technology                |
| ---------- | ------------------------- |
| Language   | Java 21                   |
| Framework  | Spring Boot 3             |
| Build Tool | Gradle                    |
| JSON       | Jackson                   |
| ORM        | JPA / Hibernate           |
| Validation | Jakarta Validation        |
| Testing    | JUnit 5, Mockito, MockMvc |
| Logging    | SLF4J                     |
| IDE        | IntelliJ IDEA             |

---

## API Endpoints

### POST `/orders`

**Request:**

```json
{
  "orderId": "O123",
  "customerId": "C100",
  "productIds": ["P100", "P200"],
  "timestamp": "2024-10-12T10:15:30Z"
}
```

**Response (2XX Created):**

```json
{
  "orderId": "O123",
  "customer": { "id": "C100", "name": "Jane Doe", "street": "42 Galaxy Rd", "zip": "63101", "country": "USA" },
  "products": [
    { "id": "P100", "name": "Laptop", "price": 1299.99, "category": "Electronics" },
    { "id": "P200", "name": "Mouse", "price": 29.99, "category": "Accessories" }
  ],
  "timestamp": "2024-10-12T10:15:30Z"
}
```

---

### GET `/orders/{orderId}`

Retrieve a specific order by ID.

**Response (200 OK):**

```json
{
  "orderId": "O123",
  "customer": { "id": "C100", "name": "Jane Doe" },
  "products": [...],
  "timestamp": "2024-10-12T10:15:30Z"
}
```

---

### GET `/orders?customerId=C100`

Returns all orders for a given customer.

### GET `/orders?productId=P100`

Returns all orders containing a specific product.

### Error Handling

| Scenario       | Status | Example Message                        |
| -------------- | ------ | -------------------------------------- |
| Unknown order  | 404    | `"Order not found O999"`               |
| Missing params | 400    | *(empty body)*                         |
| Invalid input  | 400    | `"Validation failed for request body"` |

---

## Build and Run

### Local Build

```bash
./gradlew clean build
```

### Run Locally

```bash
./gradlew bootRun
```

Application runs on:

```
http://localhost:8080
```

---

## Example cURL Commands

```bash
# Create order
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"orderId":"O123","customerId":"C100","productIds":["P100","P200"],"timestamp":"2024-10-12T10:15:30Z"}'

# Get order by ID
curl http://localhost:8080/orders/O123

# Query by customer
curl http://localhost:8080/orders?customerId=C100
```

---

## Design Highlights

* Record-based DTOs provide immutability and simplicity.
* Service layer is unit-testable and decoupled from Spring context.
* Controller layer verified using MockMvc for realistic HTTP behavior.
* Validation handled through annotations for consistent error responses.
* Exception handling ensures meaningful HTTP status codes.

---

## Future Improvements

Given more time, I would focus on expanding the service toward production readiness and scalability.
Planned areas for improvement would include:

* Comprehensive API documentation through OpenAPI/Swagger for better consumer visibility.
* Replacing the in-memory repository with a PostgreSQL persistence layer managed by Flyway for versioned schema migrations.
* Adding full integration and contract tests using Testcontainers to validate persistence and service interactions.
* Introducing Docker-based containerization with separate development and production profiles.
* Implementing monitoring and metrics (via Micrometer and Prometheus, or Dynatrace, Splunk, etc) to track request performance and failures.
* Enhancing resilience by adding caching for product lookups and circuit breakers for external dependencies.
* Refining validation and error responses to include standardized HTTP error codes and user-friendly messages.

These improvements would make the service more maintainable, observable, and ready for deployment in a real-world environment.


Author: Alyssa Noble
