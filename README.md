# News Delivery Service

Part of the [TechPulse](https://github.com/priyagupta35/techpulse) 
microservices platform.

---

## 🌐 Live Deployment  https://news-delivery-service.onrender.com

**Quick Test**  GET https://news-delivery-service.onrender.com/api/articles

> Note — Render free tier sleeps after 15 minutes of inactivity. 
> First request may take 30 to 60 seconds to wake up.

---

## What This Service Does

The News Delivery Service handles all user-facing REST API 
requests for the TechPulse platform. It provides JWT 
authentication, role-based access control, article retrieval, 
community post management, and AI-powered article summarisation 
using Spring AI with DeepSeek running via Ollama.

---

## 🛠 Tech Stack

| Category | Technologies |
|----------|-------------|
| Language | Java 17 |
| Framework | Spring Boot 3.x, Spring MVC, Spring Data JPA, Spring Security |
| Database | PostgreSQL (Neon Cloud), Hibernate ORM |
| Security | JWT Authentication, BCrypt, Role-Based Access Control |
| AI | Spring AI, Ollama, DeepSeek |
| Logging | Log4j2 |
| Containerisation | Docker |
| Deployment | Render Cloud Platform |
| Build Tool | Maven |

---

## 🔌 API Endpoints

### Authentication
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/login` | Login and get JWT token | Public |

### Articles
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/articles` | Get all articles | Public |
| GET | `/api/articles/{id}` | Get article by ID | Public |
| GET | `/api/articles/approved` | Get approved articles | Public |
| GET | `/api/articles/category/{id}` | Filter by category | Public |
| GET | `/api/articles/{id}/summary` | Get AI generated summary | Public |
| POST | `/api/articles/fetch` | Trigger news ingestion | ADMIN |
| DELETE | `/api/articles/{id}` | Delete article | ADMIN |

### Community Posts
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/community-posts` | Get approved posts | Public |
| POST | `/api/community-posts` | Submit new post | CONTRIBUTOR, ADMIN |
| PUT | `/api/community-posts/{id}/status?status=APPROVED` | Moderate post | ADMIN |

---

## ⚙️ Local Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL or Neon account
- News Ingestion Service running on port 8081

### Configure application.properties
```properties
spring.application.name=news-delivery-service
server.port=8080

spring.datasource.url=jdbc:postgresql://your-neon-host/neondb?sslmode=require
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

jwt.secret=your_jwt_secret_min_32_chars
jwt.expiration=86400000
ingestion.service.url=http://localhost:8081

spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=deepseek-r1:1.5b
```

### Run locally
```bash
mvn spring-boot:run
```

### Run with Docker
```bash
docker build -t news-delivery-service .
docker run -p 8080:8080 news-delivery-service
```

---

## Security Flow
Request arrives
↓
JwtFilter extracts Bearer token from Authorization header
↓
Token validated — signature and expiry checked
↓
User loaded from database via UserDetailsServiceImpl
↓
Authentication set in SecurityContextHolder
↓
Spring Security checks role for the endpoint
↓
Request proceeds or returns 401 / 403

---

## How AI Summarisation Works

When a client calls GET /api/articles/{id}/summary the 
AiSummaryService takes the article title and content and 
sends them to DeepSeek running via Ollama. The AI model 
generates a concise 2 to 3 sentence summary which is 
returned to the client as a plain string response.

---

## 📁 Related Repositories

| Repository | Description |
|-----------|-------------|
| [techpulse](https://github.com/priyagupta35/techpulse) | Original monolith and database schema |
| [news-ingestion-service](https://github.com/priyagupta35/news-ingestion-service) | NewsAPI data ingestion microservice |
