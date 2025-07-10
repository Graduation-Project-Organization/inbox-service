# Inbox Service

A Spring Boot microservice for managing inbox messages between doctors and patients. Supports creating, retrieving, and deleting messages.

## Features
- RESTful API for sending and receiving messages.
- JWT authentication.
- MongoDB integration.
- Validation and exception handling.
- Swagger/OpenAPI documentation.

## Requirements
- Java 17+ (project uses Java 23 in config; Java 17+ recommended for Spring Boot 3.x)
- Maven
- MongoDB instance (local or remote)

## Getting Started

### 1. Clone the repository
```sh
git clone <your-repo-url>
cd inbox-service
```

### 2. Configure MongoDB
Edit `src/main/resources/application.properties` if you need to set your MongoDB URI:
```
spring.data.mongodb.uri=mongodb://localhost:27017/inbox_db
```

### 3. Build and Run
```sh
mvn clean package
java -jar target/inbox-service-0.0.1-SNAPSHOT.jar
```
Or run directly:
```sh
mvn spring-boot:run
```

### 4. API Documentation
- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- OpenAPI docs: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Example API Endpoints
- `POST   /api/inbox` — Create a new message
- `GET    /api/inbox/{id}` — Get a message by ID
- `GET    /api/inbox?status=sent|received&page=0&size=10` — List messages
- `DELETE /api/inbox` — Delete messages (by IDs)

## Authentication
All endpoints (except `/actuator/health`, `/swagger-ui/**`, `/v3/api-docs/**`) require JWT authentication. Add your JWT token to the `Authorization: Bearer <token>` header or in a cookie called `jwt`.

## Postman Collection
A Postman collection is included: `Inbox-service.postman_collection.json`
