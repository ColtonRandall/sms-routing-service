# SMS Routing Service

A simple SMS routing service built with Java and Spring Boot that handles message routing, opt-outs, and carrier selection.

## Features
- Send SMS messages via REST API
- Route messages by carrier (AU/NZ based on phone prefix)
- Handle opt-out management
- Track message delivery status
- In-memory storage

## Technology Stack
- Java 21
- Spring Boot 4.0.1
- Maven

## Project Status
- [x] Step 1: Set up project
- [ ] Step 2: Core domain models
- [ ] Step 3: Create base Message Service structure
- [ ] Step 4: Implement carrier routing logic
- [ ] Step 5: Build REST controllers
- [ ] Step 6: Add validation / error handling
- [ ] Step 7: Write unit tests

## Current Progress
**Step 1 Completed**: Spring Boot project initialized with Maven, basic structure in place.

## API Endpoints (To be implemented)

### Send Message
```
POST /messages
Content-Type: application/json

{
    "destinationNumber": "+6412345678",
    "content": "Test message",
    "format": "SMS"
}
```

### Get Message Status
```
GET /messages/{id}
```
Statuses:
- PENDING
- SENT
- DELIVERED/BLOCKED

### Opt-out Number
```
POST /optout/{phoneNumber}
```

## Building and Running

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

Go to `http://localhost:8080`

## Testing
- Unit testing
- Curl commands (can copy/paste to verify in-console)
- Postman collection (I will provide this in the README)

## Project Structure
```
src/
├── main/
│   └── java/com.sms_routing/
│      └── SmsRoutingServiceApplication.java
└── test/
    └── java/com.sms_routing/
        └── SmsRoutingServiceApplicationTests.java
```