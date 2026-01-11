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
- [x] Step 1b: Git repository and documentation initialized
- [x] Step 2: Core domain models
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
│   └── java/com.smsrouting/
│      └── SmsRoutingServiceApplication.java
└── test/
    └── java/com.smsrouting/
        └── SmsRoutingServiceApplicationTests.java
```

## Design Decisions / Assumptions
- I explicitly did not use Lombok. Even though it's useful for reducing boilerplate code (i.e. getters/setters), I 
  figured given this is a small project it wouldn't be necessary - also to avoid the need for the lombok plugin 
  within the project.
- I used ENUMs for `Carrier` and `MessageStatus` for a cleaner, limited set of values, and to give the values 
  'type-safety' (i.e. avoid mistyping a status). It just keeps the code cleaner and more organised/structured.

## Future considerations / Improvements
- Generate each message with a `createdAt` timestamp.