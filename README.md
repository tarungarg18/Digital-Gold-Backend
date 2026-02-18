# Digital Gold Backend System

A production-grade backend system built with Java Spring Boot and MongoDB that simulates the digital gold purchase flow.

## Features

- ✅ Real-time gold price fetching from external API
- ✅ Payment gateway integration (Razorpay compatible)
- ✅ Transaction management and tracking
- ✅ Portfolio/Holdings management
- ✅ Profit/Loss calculation
- ✅ Request tracing and logging
- ✅ Global exception handling
- ✅ Comprehensive error handling

## Tech Stack

- **Java 17+**
- **Spring Boot 3.2.0**
- **MongoDB** (Spring Data MongoDB)
- **WebClient** (for HTTP calls)
- **Lombok**
- **SLF4J + Logback**

## Project Structure

```
com.project.gold
├── controller      # REST API endpoints
├── service         # Business logic
├── repository      # MongoDB data access
├── client          # External API clients
├── model           # MongoDB entities
├── dto             # Data Transfer Objects
├── config          # Configuration classes
└── exception       # Exception handling
```

## API Endpoints

### 1. Buy Gold
```
POST /api/v1/gold/buy
Content-Type: application/json

Request Body:
{
  "userId": "user123",
  "amount": 10
}

Response:
{
  "status": "SUCCESS",
  "gramsAllocated": 0.00167,
  "priceUsed": 6000,
  "transactionId": "...",
  "paymentId": "txn_123"
}
```

### 2. Get Holdings
```
GET /api/v1/gold/holdings/{userId}

Response:
{
  "userId": "user123",
  "totalGrams": 0.005,
  "currentValue": 30,
  "investedAmount": 29,
  "profitLossAmount": 1,
  "profitLossPercent": 3.45,
  "avgBuyPrice": 5800
}
```

### 3. Get Transactions
```
GET /api/v1/gold/transactions/{userId}

Response: Array of Transaction objects
```

### 4. Get Current Gold Price
```
GET /api/v1/gold/price

Response: 6000.0
```

## Configuration

Configure external APIs and MongoDB in `application.yml`:

```yaml
external:
  apis:
    gold-price:
      url: ${GOLD_PRICE_API_URL:https://api.goldapi.io/api/XAU/INR}
      timeout: 5000
    
    payment-gateway:
      url: ${PAYMENT_GATEWAY_API_URL:https://api.razorpay.com/v1/payments}
      timeout: 10000
      api-key: ${RAZORPAY_API_KEY:}
      api-secret: ${RAZORPAY_API_SECRET:}

spring:
  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017/digital_gold}
```

## Environment Variables

- `MONGODB_URI` - MongoDB connection string
- `GOLD_PRICE_API_URL` - Gold price API endpoint
- `PAYMENT_GATEWAY_API_URL` - Payment gateway API endpoint
- `RAZORPAY_API_KEY` - Razorpay API key
- `RAZORPAY_API_SECRET` - Razorpay API secret

## Prerequisites

Before running the application, you need:

- **Java 17+** (JDK)
- **Maven 3.6+** (or use an IDE with built-in Maven)
- **MongoDB** (local or cloud instance)

> **Don't have these installed?** See [SETUP.md](SETUP.md) for detailed installation instructions.

### Quick Setup Check

Run the setup checker script:
```powershell
.\check-setup.ps1
```

## Running the Application

### Option 1: Using Maven (Command Line)

1. **Build the project:**
   ```bash
   mvn clean install
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Application will start on:** `http://localhost:8080`

### Option 2: Using IDE (Recommended for beginners)

**IntelliJ IDEA:**
1. Open project folder
2. Wait for Maven dependencies to download
3. Right-click `DigitalGoldApplication.java` → Run

**Eclipse:**
1. Import as Maven project
2. Right-click project → Run As → Spring Boot App

**VS Code:**
1. Install Java Extension Pack
2. Open project folder
3. Run from Run menu or F5

## Database Collections

### transactions
```json
{
  "_id": ObjectId,
  "userId": "user123",
  "amount": 10,
  "goldPrice": 6000,
  "grams": 0.00167,
  "paymentId": "txn_123",
  "status": "SUCCESS",
  "createdAt": ISODate
}
```

### holdings
```json
{
  "_id": ObjectId,
  "userId": "user123",
  "totalGrams": 0.005,
  "avgBuyPrice": 5800,
  "totalInvestedAmount": 29
}
```

## Business Logic

### Gold Calculation
```
grams = amount / currentPrice
```

### Profit/Loss Calculation
```
currentValue = totalGrams * latestPrice
investedAmount = sum of all transactions
profit% = ((currentValue - investedAmount) / investedAmount) * 100
```

## Logging

The application uses SLF4J with Logback for logging. All logs include:
- Request tracing (Trace ID)
- External API calls
- Payment status
- Database operations
- Errors and exceptions

Logs are written to:
- Console (with trace ID)
- File: `logs/digital-gold.log`

## Error Handling

The application includes:
- Global exception handler
- Custom exceptions (PaymentFailedException)
- Validation error handling
- Proper HTTP status codes

## Request Tracing

Every request gets a unique Trace ID (X-Trace-Id header) that is:
- Generated automatically if not provided
- Logged in all log statements
- Returned in response headers

## License

This project is for educational/demonstration purposes.
