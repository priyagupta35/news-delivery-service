# News Delivery Service

# Overview

The News Delivery Service is a Spring Boot microservice responsible for delivering technology news through secure REST APIs.
It retrieves articles populated by the News Ingestion Service and provides user authentication, role-based authorization, article contribution workflows, and AI-powered article summarization.

# Features

- Secure REST APIs
- JWT Authentication
- Spring Security
- Role-Based Access Control
- User Contributions
- Admin Moderation
- AI-powered Article Summarization
- Hibernate (JPA)
- MySQL
- Log4j2 Logging

## Technology Stack

- Java 21
- Spring Boot
- Spring Security
- JWT
- Hibernate (JPA)
- MySQL
- Spring AI
- Ollama
- DeepSeek
- Maven

## Related Service

Articles are periodically ingested by the **News Ingestion Service** before being served by this microservice.
