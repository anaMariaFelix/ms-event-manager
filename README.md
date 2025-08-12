# 📌Microservice Project — Event Manager with Java + Spring Boot & AWS

## 📝 Project Description

The project consists of an independent microservice — ms-event-manager — organized in a monorepo to facilitate development and maintenance.

- **Microservice Event (Port 8080):** Responsible for registering, updating and consulting events, integrating with the ViaCEP API for address validation, ensuring security via JWT and Persistence made with MongoDB Atlas via Spring Data MongoDB.

---

## 📂 Project Structure

The project uses a monorepo to facilitate the management and development.
Its internal structure was developed following the Spring Boot layering pattern, with a clear separation of responsibilities.

The main folder structure is organized as follows:

```
ms-event-manager/                  # Microservice responsible for event management (port 8080)
├── src/main/java
│   └── com/anamariafelix/ms_event_manager
│       ├── client/                # OpenFeign interfaces for consuming external APIs (e.g., ViaCep and ms-ticket)
│       ├── config/                # Global Settings (Security and Swagger)
│       ├── controller/            # REST controllers that expose endpoints to the end client
│       ├── dto/                   # Data Transfer Objects (Create and Response DTOs)
│       ├── enums/                 # Enumerations used in the system (e.g., Status, types, etc.)
│       ├── exception/             # Custom exception classes
│       ├── jwt/                   # JWT authentication implementation (provider, filter, UserDetailsService)
│       ├── mapper/                # Converters between entities and DTOs
│       ├── model/                 # Domain entities (e.g. Event, User)
│       ├── repository/            # Data access interfaces (Spring Data JPA/Mongo)
│       ├── service/               # Services with business logic
│
└── pom.xml                        # Maven configuration file

```
---
## ✨ Features

### Microservice Event
- [x] User registration and login with JWTn
- [x] Integration with ViaCep via OpeFeign
- [x] Query, create, update, and delete events
- [x] Persistence in the MongoDB Atlas database
- [x] Unit test coverage with `Coverage`
- [x] Documented using Swagger/OpenAPI
- [x] Depoly on AWS EC2 instances
---

## 🛠️ Technologies Used

- **Language:** Java 17
- **Framework:** Spring Boot 3
- **Communication:** Spring Web, OpenFeign
- **Security:** Spring Security, JWT
- **Database:** MongoDB Atlas
- **Persistence:** Spring Data MongoDB 
- **Documentation:** SpringDoc (Swagger)
- **Build:** Maven
- **Layered architecture:** following Spring best practices
---

## 🧩 Tool and Dependency Details

### 🔗 OpenFeign

- **In ms-event-manager:**
  - Exposes REST endpoints that are consumed by ms-ticket-manager as if they were local methods, simplifying integration logic.
  - `ViaCepClient` Consumes the ViaCEP external API via ViaCepClient to obtain address data based on the provided zip code, also with configured timeouts, fallback, and logging.


### 🔒 Spring Security & JWT

**Authentication Flow:**
1. User sends credentials to `POST api/v1/auth`.
2. The JWT token is generated if authentication is valid.
3. The token is returned to the client.

**Authorization Flow:**
1. Client sends the token in the header `Authorization: Bearer <token>`.
2. A filter validates the token and populates the security context.
---

## 🚀 How to Run the Project

### Prerequisites
- Git
- JDK 17+
- Maven 3.9+

### Step by Step

```
# Clone the project Event
git clone https://github.com/anaMariaFelix/ms-event-manager

# Start
cd ms-event-manager
mvn spring-boot:run

```

### 🌐 Swagger Documentation

- **Microservice Event:**  
  -[http://localhost:8080/swagger-ms-event-manager.html](http://localhost:8080/swagger-ui/index.html)

  -[http://localhost:8080/swagger-ms-event-manager.html](http://localhost:8080/docs-ms-event-manager)

---
