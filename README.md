<div align="center">

# News Delivery Service 📡
### User-Facing REST APIs & AI Summarization Microservice for TechPulse

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6_JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![Spring AI](https://img.shields.io/badge/Spring_AI-DeepSeek_r1-4F46E5?style=for-the-badge&logo=openai&logoColor=white)](https://spring.io/projects/spring-ai)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon_Cloud-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://neon.tech/)
[![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Render](https://img.shields.io/badge/Render-Deployed-46E3B7?style=for-the-badge&logo=render&logoColor=black)](https://render.com/)

<p align="center">
  The core user-facing REST microservice for the <b><a href="https://github.com/priyagupta35/techpulse">TechPulse</a></b> platform.<br/>
  Delivers secured endpoints for article consumption, community moderation, stateless <b>JWT authentication & RBAC</b>, and integrates <b>Spring AI</b> with local LLMs (DeepSeek via Ollama) for real-time article briefings.
</p>

---

</div>

## 🌐 Live Cloud Deployment

| Component | Endpoint | Status |
| :--- | :--- | :--- |
| **Live Base URL** | [`https://news-delivery-service.onrender.com`](https://news-delivery-service.onrender.com) | `200 OK` |
| **Public Articles Feed** | [`GET https://news-delivery-service.onrender.com/api/articles`](https://news-delivery-service.onrender.com/api/articles) | `200 OK` |
| **Approved Community Posts**| [`GET https://news-delivery-service.onrender.com/api/community-posts`](https://news-delivery-service.onrender.com/api/community-posts) | `200 OK` |

> ℹ️ **Note:** Hosted on Render Free Tier. Services spin down after inactivity and may require 30–60 seconds on the initial wake-up request.

---

## 📌 Role in the TechPulse Ecosystem

```
  ┌─────────────────────────┐
  │   Client Apps / Web     │
  └────────────┬────────────┘
               │ Bearer JWT Token / HTTP
               ▼
  ┌─────────────────────────────────────────────────────────┐
  │            News Delivery Service (Port 8080)            │  <-- THIS SERVICE
  │   • Stateless Authentication & RBAC Filter Chain        │
  │   • Public & Protected REST Controllers                 │
  │   • AI Summarisation via Spring AI (DeepSeek LLM)       │
  └────────────┬───────────────────────────────┬────────────┘
               │ (reads & writes)              │ Inter-service REST
               ▼                               ▼
  ┌─────────────────────────┐     ┌─────────────────────────┐
  │  PostgreSQL (Neon Cloud)│     │  News Ingestion Service │
  │    (Shared Data Layer)  │     │       (Port 8081)       │
  └─────────────────────────┘     └─────────────────────────┘
```

The News Delivery Service serves as the user-facing application boundary:
- **Stateless Authentication:** Manages user registration and login, generating cryptographically verified JWT tokens.
- **Role Enforcement:** Enforces endpoint access across `READER`, `CONTRIBUTOR`, and `ADMIN` tiers.
- **Inter-Service Orchestration:** When an administrator triggers news ingestion (`POST /api/articles/fetch`), this service issues an HTTP REST call to the internal [News Ingestion Service](https://github.com/priyagupta35/news-ingestion-service).
- **Intelligent Summarisation:** Uses Spring AI's `ChatClient` abstraction to summarize lengthy technology articles on demand.

---

## 🤖 AI Summarisation (Spring AI + DeepSeek)

Through the `AiSummaryController` (`GET /api/articles/{id}/summary`), clients can request an executive summary of any article:

```
Article Title + Content
          ↓
AiSummaryService.summariseArticle()
          ↓
ChatClient.prompt()
          ↓
Ollama (DeepSeek-r1:1.5b Model)
          ↓
Concise 2–3 sentence executive briefing returned to client
```

---

## 🔌 Complete API Specification

### 1. Authentication (`/api/auth`)
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new account (`username`, `email`, `password`, `role`) | Public |
| `POST` | `/api/auth/login` | Authenticate with credentials and receive a JWT Bearer token | Public |

### 2. Articles (`/api/articles`)
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/articles` | Retrieve all technology articles | Public |
| `GET` | `/api/articles/{id}` | Retrieve specific article by database ID | Public |
| `GET` | `/api/articles/approved` | Retrieve all verified approved articles | Public |
| `GET` | `/api/articles/category/{categoryId}` | Filter articles by category | Public |
| `GET` | `/api/articles/{id}/summary` | Generate real-time AI summary with DeepSeek | Public |
| `POST` | `/api/articles` | Manually submit an article | Authenticated |
| `POST` | `/api/articles/fetch` | Triggers News Ingestion Service via REST | `ROLE_ADMIN` |
| `DELETE` | `/api/articles/{id}` | Permanently remove an article | `ROLE_ADMIN` |

### 3. Community Insights (`/api/community-posts`)
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/community-posts` | Retrieve all approved community insights | Public |
| `GET` | `/api/community-posts/{id}` | Retrieve specific post by ID | Public |
| `POST` | `/api/community-posts` | Submit a tech post (Enters `PENDING` state) | `ROLE_CONTRIBUTOR`, `ROLE_ADMIN` |
| `PUT` | `/api/community-posts/{id}/status?status=APPROVED` | Approve or reject submission | `ROLE_ADMIN` |

### 4. Categories (`/api/categories`)
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/categories` | Retrieve all available topics | Public |
| `POST` | `/api/categories` | Create a new category | Authenticated |

---

## 🔐 Security & JWT Authentication Flow

1. **Incoming Request:** Client sends HTTP request with header: `Authorization: Bearer <token>`.
2. **`JwtFilter`:** Intercepts request before `UsernamePasswordAuthenticationFilter`.
3. **Validation:** Checks HMAC token signature and expiration date against configured `jwt.secret`.
4. **Context Injection:** Extracts user email and role (`ROLE_READER`, `ROLE_CONTRIBUTOR`, `ROLE_ADMIN`) and sets authentication in `SecurityContextHolder`.
5. **Authorization Decision:** Spring Security allows request or responds with `401 Unauthorized` / `403 Forbidden`.

---

## ⚙️ Local Setup & Configuration

### Prerequisites
- **Java 17+**
- **Maven 3.8+** (or included `./mvnw`)
- **PostgreSQL** database (Local or Neon Serverless)
- *(Optional)* **Ollama** running `deepseek-r1:1.5b` locally (`ollama run deepseek-r1:1.5b`)

### 1. Clone the Repository
```bash
git clone https://github.com/priyagupta35/news-delivery-service.git
cd news-delivery-service
```

### 2. Configure `application.properties`
Copy example configuration:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Update properties with your environment credentials:
```properties
spring.application.name=news-delivery-service
server.port=${PORT:8080}

# PostgreSQL Database
spring.datasource.url=jdbc:postgresql://your-neon-host/neondb?sslmode=require
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# JWT Secret
jwt.secret=your_jwt_secret_key_minimum_32_characters
jwt.expiration=86400000

# Inter-service Ingestion URL
ingestion.service.url=http://localhost:8081

# Spring AI (Ollama + DeepSeek)
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=deepseek-r1:1.5b
```

### 3. Run the Service
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Service starts at `http://localhost:8080`.

### 4. Build and Run with Docker
```bash
docker build -t news-delivery-service .
docker run -p 8080:8080 \
  -e PORT=8080 \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://your-neon-host/neondb?sslmode=require" \
  -e SPRING_DATASOURCE_USERNAME="your_username" \
  -e SPRING_DATASOURCE_PASSWORD="your_password" \
  -e JWT_SECRET="your_jwt_secret_min_32_chars" \
  news-delivery-service
```

---

## 📁 Connected Repositories in the Ecosystem

| Repository | Architecture & Purpose |
| :--- | :--- |
| **[techpulse](https://github.com/priyagupta35/techpulse)** | The monolithic foundation codebase and original MySQL schema. |
| **[news-ingestion-service](https://github.com/priyagupta35/news-ingestion-service)** | Automated scheduled background worker scraping NewsAPI every 30 mins into PostgreSQL. |
| **[techpulse-docker](https://github.com/priyagupta35/techpulse-docker)** | Complete Docker Compose configuration running MySQL and all microservices locally. |

---

## 👩‍💻 Author

**Priya Gupta**  
*B.Tech in Computer Science & Business Systems (CSBS)*  
*Netaji Subhash Engineering College, Kolkata*

- **GitHub:** [@priyagupta35](https://github.com/priyagupta35)
- **LinkedIn:** [Priya Gupta](https://www.linkedin.com/in/priya-gupta-b4a79528a/)
- **Email:** [priyagupta.csbs@gmail.com](mailto:priyagupta.csbs@gmail.com)
