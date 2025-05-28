# 🎮 Dobble Game Backend

A backend implementation of the Dobble card game using Spring Boot and WebSocket for real-time gameplay.

## 🚀 Tech Stack

### Backend

- ☕ Java 21
- 🍃 Spring Boot 3.4.5
- 📦 Maven
- 🎯 Lombok

### Database

- 🐘 PostgreSQL 15
- 🔄 Spring Data JPA

### Infrastructure

- 🐳 Docker & Docker Compose
- 🔌 WebSocket for real-time communication

## 🛠️ Prerequisites

- Java 21 JDK
- Docker and Docker Compose
- Maven

## 🚀 Getting Started

1. Clone the repository:

```bash
git clone <repository-url>
cd dobble_be
```

2. Prepare the project:

```bash
./mvnw clean install -DskipTests
```

3. Run with Docker Compose:

```bash
docker-compose up --build
```

The application will be available at `http://localhost:8080`

## 📦 Project Structure

```
dobble_be/
├── src/                    # Source code
├── target/                 # Compiled files
├── .mvn/                   # Maven wrapper files
├── Dockerfile             # Docker configuration
├── docker-compose.yaml    # Docker Compose configuration
├── initCards.sql          # Database initialization script
└── pom.xml               # Maven dependencies
```

## 🔧 Configuration

The application uses the following default configuration:

- Application Port: 8080
- Database Port: 5444
- Database Name: dobble
- Database User: dobble
- Database Password: dobble

## 🎯 Features

- Real-time game updates using WebSocket
- RESTful API endpoints
- PostgreSQL database for data persistence
- Docker containerization for easy deployment
- Automatic database schema updates
