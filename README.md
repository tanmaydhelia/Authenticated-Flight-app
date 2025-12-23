# Flight Booking Microservices Application

> A distributed microservices-based flight booking system built with Spring Boot, Spring Cloud, and event-driven architecture using Kafka. Features JWT authentication, RBAC, and comprehensive monitoring.

## 🎯 Project Overview

This project implements a complete flight booking system using microservices architecture with:
- **Service Discovery** using Netflix Eureka
- **API Gateway** with JWT-based authentication and authorization
- **Centralized Configuration** with Spring Cloud Config
- **Event-Driven Architecture** using Apache Kafka
- **Role-Based Access Control (RBAC)** with JWT tokens
- **Inter-service Communication** using OpenFeign clients
- **Database per Service** pattern for data isolation

---

## 🗂️ Project Structure

```
Microservice-flight-app/
│
├── config-server/                     # Centralized configuration server
│   ├── src/main/java/                # Config server application code
│   ├── src/main/resources/           # Application properties
│   ├── pom.xml                       # Maven dependencies
│   ├── Dockerfile                    # Docker configuration
│   └── target/                       # Build artifacts
│
├── flightapp-service-registry/        # Eureka service discovery server
│   ├── src/main/java/                # Eureka server application
│   ├── src/main/resources/           # Eureka configuration
│   ├── pom.xml                       # Maven dependencies
│   ├── Dockerfile                    # Docker configuration
│   └── target/                       # Build artifacts
│
├── flightapp-api-gateway/             # API Gateway with authentication
│   ├── src/main/java/                # Gateway logic and filters
│   │   ├── filter/                   # AuthenticationFilter for JWT validation
│   │   └── util/                     # JWT utility classes
│   ├── src/main/resources/           # Gateway routes configuration
│   ├── pom.xml                       # Maven dependencies
│   ├── Dockerfile                    # Docker configuration
│   └── target/                       # Build artifacts
│
├── flightapp-identity-service/        # Authentication & user management
│   ├── src/main/java/                # Identity service code
│   │   ├── controller/               # Auth endpoints (register, login, validate)
│   │   ├── service/                  # Authentication business logic
│   │   ├── entity/                   # User and credential entities
│   │   └── repository/               # JPA repositories
│   ├── src/main/resources/           # Database & Eureka config
│   ├── pom.xml                       # Maven dependencies
│   ├── Dockerfile                    # Docker configuration
│   └── target/                       # Build artifacts
│
├── flightapp-flight-service/          # Flight inventory management
│   ├── src/main/java/                # Flight service code
│   │   ├── controller/               # Flight & admin endpoints
│   │   ├── service/                  # Business logic
│   │   ├── entity/                   # Flight entity
│   │   ├── repository/               # JPA repositories
│   │   └── kafka/                    # Kafka consumer for booking events
│   ├── src/main/resources/           # Database, Eureka, Kafka config
│   ├── pom.xml                       # Maven dependencies
│   ├── Dockerfile                    # Docker configuration
│   ├── lombok.config                 # Lombok configuration
│   └── target/                       # Build artifacts & JaCoCo reports
│
├── flightapp-booking-service/         # Booking & ticket management
│   ├── src/main/java/                # Booking service code
│   │   ├── controller/               # Booking endpoints
│   │   ├── service/                  # Booking business logic
│   │   ├── entity/                   # Booking & passenger entities
│   │   ├── repository/               # JPA repositories
│   │   ├── feign/                    # Feign client for Flight Service
│   │   └── kafka/                    # Kafka producer for notifications
│   ├── src/main/resources/           # Database, Eureka, Kafka config
│   ├── pom.xml                       # Maven dependencies
│   ├── Dockerfile                    # Docker configuration
│   ├── lombok.config                 # Lombok configuration
│   └── target/                       # Build artifacts & JaCoCo reports
│
├── flightapp-notification-service/    # Email notification service
│   ├── src/main/java/                # Notification service code
│   │   ├── kafka/                    # Kafka consumer for booking events
│   │   └── service/                  # Email notification logic
│   ├── src/main/resources/           # Kafka configuration
│   ├── pom.xml                       # Maven dependencies
│   ├── Dockerfile                    # Docker configuration
│   └── target/                       # Build artifacts
│
├── jmeter-testing-report/             # Performance test results
│   ├── Test Plan.jmx                 # JMeter test plan
│   ├── Results.jtl                   # Test execution results
│   ├── jmeter.log                    # JMeter logs
│   └── HTML_Report/                  # HTML dashboard
│
├── Postman Collection Report/         # API testing reports
│   ├── FlightApp Gateway.postman_collection.json
│   └── newman-run-report-*.html      # Newman test reports
│
├── docker-compose.yml                 # Docker compose for all services
├── build-services.sh                  # Build script for all services
├── init.sql                          # Database initialization script
├── README.md                         # This file
└── Summary.doc                       # Project summary document
```

---

## 🚀 Services & Ports

| Service | Port | Purpose | Database |
|---------|------|---------|----------|
| **Config Server** | `8888` | Centralized configuration management | N/A |
| **Service Registry (Eureka)** | `8761` | Service discovery and registration | N/A |
| **API Gateway** | `9000` | Single entry point, routing, authentication | N/A |
| **Identity Service** | `9091` | User authentication, JWT token generation | `identity_db` (MySQL) |
| **Flight Service** | `9080` | Flight inventory, search, seat management | `flight_service_DB` (MySQL) |
| **Booking Service** | `9090` | Flight booking, ticket management | `booking_DB` (MySQL) |
| **Notification Service** | `9095` | Email notifications via Kafka | N/A |

---

## 📡 API Endpoints

### **1. Identity Service** (Port: `9091`)
**Base Path:** `/auth`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/auth/register` | Register new user | No |
| `POST` | `/auth/token` | Login and get JWT token | No |
| `GET` | `/auth/validate` | Validate JWT token | Yes (Token) |
| `PUT` | `/auth/change-password` | Change user password | Yes (Token) |

**Access via Gateway:** `http://localhost:9000/auth/{endpoint}`

---

### **2. Flight Service** (Port: `9080`)
**Base Path:** `/api`

| Method | Endpoint | Description | Auth Required | Role |
|--------|----------|-------------|---------------|------|
| `POST` | `/api/search` | Search available flights | Yes | USER/ADMIN |
| `POST` | `/api/airline/inventory/add` | Add new flight to inventory | Yes | ADMIN |
| `GET` | `/api/internal/flight/{id}` | Get flight by ID (internal) | Internal Only | N/A |
| `PUT` | `/api/internal/flight/{id}/seats` | Update available seats (internal) | Internal Only | N/A |

**Access via Gateway:** `http://localhost:9000/flight/{endpoint}`

---

### **3. Booking Service** (Port: `9090`)
**Base Path:** `/api`

| Method | Endpoint | Description | Auth Required | Role |
|--------|----------|-------------|---------------|------|
| `POST` | `/api/book/{flightId}` | Book a flight | Yes | USER/ADMIN |
| `GET` | `/api/ticket/{pnr}` | Get ticket details by PNR | Yes | USER/ADMIN |
| `GET` | `/api/history/{emailId}` | Get booking history | Yes | USER/ADMIN |
| `DELETE` | `/api/cancel/{pnr}` | Cancel a booking | Yes | USER/ADMIN |

**Access via Gateway:** `http://localhost:9000/booking/{endpoint}`

---

## 🔐 Authentication & Authorization

### JWT Token Flow
1. **Register/Login:** Call `/auth/register` or `/auth/token` to get JWT token
2. **Add Header:** Include token in requests: `Authorization: Bearer <your-jwt-token>`
3. **Gateway Validation:** API Gateway validates token before routing to services
4. **Role-Based Access:** Services check user roles (USER/ADMIN) for authorization

### Sample Authentication Request
```bash
# Register User
curl -X POST http://localhost:9000/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com","password":"pass123","roles":"USER"}'

# Login
curl -X POST http://localhost:9000/auth/token \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"pass123"}'

# Use Token
curl -X POST http://localhost:9000/flight/api/search \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{"source":"NYC","destination":"LAX","travelDate":"2025-12-25"}'
```

---

## 🛠️ Technology Stack

### Core Technologies
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Cloud** (Gateway, Config, Netflix Eureka)
- **MySQL** (Database per service)
- **Apache Kafka** (Event-driven messaging)
- **Docker** (Containerization)

### Key Dependencies
- Spring Cloud Gateway
- Spring Security with JWT
- Spring Data JPA
- Spring Cloud OpenFeign
- Spring Kafka
- Lombok
- JaCoCo (Code coverage)
- JMeter (Performance testing)
- Newman/Postman (API testing)

---

## 🏃 Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Apache Kafka (optional, for notifications)
- Docker (optional, for containerized deployment)

### **Option 1: Run Locally (Recommended Order)**

**Step 1: Start Config Server**
```bash
cd config-server
mvn spring-boot:run
# Wait for: "Started ConfigServerApplication on port 8888"
```

**Step 2: Start Service Registry**
```bash
cd flightapp-service-registry
mvn spring-boot:run
# Wait for: "Started ServiceRegistryApplication on port 8761"
# Access Eureka Dashboard: http://localhost:8761
```

**Step 3: Start Identity Service**
```bash
cd flightapp-identity-service
mvn spring-boot:run
# Wait for registration in Eureka
```

**Step 4: Start Flight Service**
```bash
cd flightapp-flight-service
mvn spring-boot:run
# Wait for registration in Eureka
```

**Step 5: Start Booking Service**
```bash
cd flightapp-booking-service
mvn spring-boot:run
# Wait for registration in Eureka
```

**Step 6: Start Notification Service** (Optional)
```bash
cd flightapp-notification-service
mvn spring-boot:run
# Requires Kafka running on localhost:9092
```

**Step 7: Start API Gateway**
```bash
cd flightapp-api-gateway
mvn spring-boot:run
# Application ready on: http://localhost:9000
```

### **Option 2: Docker Compose**
```bash
# Build all services
./build-services.sh

# Start all containers
docker-compose up -d

# Check logs
docker-compose logs -f

# Stop all services
docker-compose down
```

---

## 🧪 Testing

### **1. API Testing with Postman/Newman**
```bash
# Import collection
# File: Postman Collection Report/FlightApp Gateway.postman_collection.json

# Run with Newman
newman run "Postman Collection Report/FlightApp Gateway.postman_collection.json" \
  -r html --reporter-html-export newman-report.html
```

### **2. Performance Testing with JMeter**
```bash
cd jmeter-testing-report

# Run JMeter test (CLI mode)
jmeter -n -t "Test Plan.jmx" -l Results.jtl -e -o HTML_Report/

# View report
# Open: jmeter-testing-report/HTML_Report/index.html
```

### **3. Code Coverage with JaCoCo**
```bash
# Run tests with coverage
cd flightapp-flight-service
mvn clean test

# View coverage report
# Open: target/site/jacoco/index.html
```

---

## 📊 Key Features Implemented

### ✅ Microservices Architecture
- Independent, loosely-coupled services
- Database per service pattern
- RESTful API design

### ✅ Service Discovery & Load Balancing
- Netflix Eureka for service registration
- Client-side load balancing with Spring Cloud LoadBalancer

### ✅ API Gateway with Security
- Single entry point for all services
- JWT-based authentication
- Role-based authorization (RBAC)
- Custom authentication filter

### ✅ Centralized Configuration
- Spring Cloud Config Server
- External Git repository for configurations
- Environment-specific properties

### ✅ Event-Driven Architecture
- Kafka for asynchronous messaging
- Booking events published to Kafka
- Notification service consumes booking events
- Flight inventory updates via Kafka

### ✅ Inter-Service Communication
- OpenFeign for synchronous calls
- Booking Service → Flight Service (seat updates)
- Resilient communication patterns

### ✅ Monitoring & Testing
- JaCoCo code coverage reports
- JMeter performance testing
- Postman/Newman API testing
- Comprehensive logging

---

## 🗄️ Database Schema

### **identity_db** (Identity Service)
- `user_credential` - User authentication data

### **flight_service_DB** (Flight Service)
- `flight` - Flight inventory and details

### **booking_DB** (Booking Service)
- `booking` - Booking records
- `passenger` - Passenger information

---

## 📝 Configuration Files

All services are configured via:
- **Local:** `src/main/resources/application.properties`
- **Centralized:** Spring Cloud Config Server (https://github.com/tanmaydhelia/flight-config-repo)

Key configurations:
- Database connections
- Eureka registration
- Kafka broker settings
- JWT secret keys
- Gateway routes

---

## 🐳 Docker Support

Each service includes:
- `Dockerfile` for containerization
- `docker-compose.yml` for orchestration
- `build-services.sh` for automated builds

---

## 📚 Additional Resources

- **Eureka Dashboard:** http://localhost:8761
- **API Gateway:** http://localhost:9000
- **Config Repository:** https://github.com/tanmaydhelia/flight-config-repo
- **Postman Collection:** `Postman Collection Report/FlightApp Gateway.postman_collection.json`
- **JMeter Test Plan:** `jmeter-testing-report/Test Plan.jmx`

---

## 👤 Author

**Tanmay Dhelia**
- GitHub: [@tanmaydhelia](https://github.com/tanmaydhelia)
- Repository: [Authenticated-Flight-app](https://github.com/tanmaydhelia/Authenticated-Flight-app)
- Branch: `RBAC_functionality`

---

## 📄 License

This project is part of a microservices learning and development initiative.
