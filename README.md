# Digital Gold Backend System

A production-grade backend system built with Java Spring Boot and MongoDB that simulates the digital gold purchase flow.

## Features

- Real-time gold price fetching from external API
- Payment gateway integration (Razorpay compatible)
- Transaction management and tracking
- Portfolio/Holdings management
- Profit/Loss calculation
- Request tracing and logging
- Global exception handling

## Tech Stack

- Java 17+
- Spring Boot 3.2.0
- MongoDB (Spring Data MongoDB)
- WebClient (for HTTP calls)
- Lombok
- SLF4J + Logback

## Project Structure

```
com.project.gold
├── controller
├── service
├── repository
├── client
├── model
├── dto
├── config
└── exception
```

## API Endpoints

### Buy Gold
```
POST /api/v1/gold/buy
Content-Type: application/json

Request: { "userId": "user123", "amount": 10 }
```

### Get Holdings
```
GET /api/v1/gold/holdings/{userId}
```

### Get Transactions
```
GET /api/v1/gold/transactions/{userId}
```

### Get Current Gold Price
```
GET /api/v1/gold/price
```

## Configuration

Configure external APIs and MongoDB in `application.yml`.

## Running the Application

```bash
mvn clean install
mvn spring-boot:run
```

Application runs on http://localhost:8080

## Prerequisites

- Java 17+
- Maven 3.6+
- MongoDB
