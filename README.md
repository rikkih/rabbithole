# Rabbithole Chat Backend

![rabbithole-sillhouette.jpg](assets/rabbithole-sillhouette.jpg)  

---

## Overview

Rabbithole is a **real-time chat backend** built with Java and Spring Boot. It supports:

- Creating and managing chats and chat participants
- Sending and receiving messages via **WebSocket** for real-time updates
- Producing and consuming Kafka events for chat message synchronization
- Secure, authenticated chats between users using **OAuth2** (Auth0 integration)
- Validation, persistence, and scalable architecture

---

## Related Projects

- **Rabbithole Frontend** — [github.com/hoderick/rabbithole-frontend](https://github.com/rikkih/RabbitHoleWeb)  
  React-based frontend client for the Rabbithole chat backend.

- **Rabbithole Infrastructure** — [github.com/hoderick/rabbithole-infra](https://github.com/rikkih/RabbitHoleInfra)  
  Infrastructure as Code and deployment automation.

Feel free to explore these repos to see the full-stack and deployment setup!
To run the Frontend locally, you'll need to follow the instructions on rabbithole-frontend.

## Project Structure

src/main/java/com/hoderick/rabbithole/
├── RabbitholeApplication.java
├── audit/
├── chat/
│   ├── api/
│   ├── dto/
│   ├── mapper/
│   ├── model/
│   ├── repository/
│   └── service/
├── config/
├── event/
├── exception/
├── s3/
└── user/
...

- **chat**: Core chat domain including REST/WebSocket APIs, DTOs, entities, repositories, and services
- **config**: Security, WebSocket, OAuth2, Kafka, and JPA configurations
- **event**: Kafka producers and consumers for chat message events
- **s3**: AWS S3 integration for file storage
- **user**: User profile management with API, DTO, mapper, repository, and service
- **audit**: Auditing support via `Auditable` model

---

## Features

- Real-time chat messaging with WebSocket support
- User authentication & authorization using OAuth2
- Kafka event streaming for distributed chat synchronization
- AWS S3 file storage support (e.g., for avatars)
- Comprehensive validation and error handling
- Well-structured layered architecture with DTOs and mappers

---

## Getting Started

### Prerequisites (local)

- Java 21+
- Gradle
- Kafka cluster running (See docker-compose.yml)
- PostgresQL, MinIO, Redis running
- AWS credentials for S3 access
- OAuth2 provider (e.g., Auth0) configured

### Build & Run (local)

```bash
# Build the project
./gradlew clean build

# Run the application
./gradlew bootRun
```

### Configuration (local)
Configure your application.yml for:

- OAuth2 settings (client ID, secret, issuer URI)
- Kafka brokers and topics
- AWS S3 credentials and bucket
- Database connection

