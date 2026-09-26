# Document Processing Pipeline

A Spring Boot backend that lets users upload PDF documents, automatically extracts their text, generates an AI-powered summary and category, and tracks the entire process through a clear status pipeline — all handled asynchronously so the upload response is instant.

🔗 **Live API (Swagger UI):** https://document-processing-pipeline-xord.onrender.com/swagger-ui/index.html

> Note: hosted on Render's free tier, so the app sleeps after 15 minutes of inactivity — the first request may take 30-50 seconds to wake it up.

## What it does

1. User uploads a PDF via `POST /documents/upload`
2. The file is validated (must be a PDF, under 5MB, not empty) and saved
3. The API responds immediately with a `QUEUED` status — no waiting
4. In the background, the app:
   - Extracts text from the PDF using Apache PDFBox
   - Sends the text to OpenAI for a summary and category (INVOICE / RESUME / CONTRACT / OTHER)
   - If the AI call fails for any reason, falls back to simple keyword-based categorization instead of crashing
5. User checks progress anytime via `GET /documents/{id}` — status moves through `QUEUED → PROCESSING → COMPLETED` (or `FAILED` if something goes wrong)

## Tech stack

- **Backend:** Java 17, Spring Boot 3.5, Spring Web, Spring Data JPA
- **Database:** PostgreSQL
- **PDF processing:** Apache PDFBox
- **AI integration:** OpenAI API, with a keyword-matching fallback
- **Testing:** JUnit 5, Mockito
- **API docs:** springdoc-openapi (Swagger UI)
- **Deployment:** Docker, Render (web service + managed PostgreSQL)

## Architecture

Layered design: **Controller → Service → Repository**, with DTOs separating API responses from internal entities, and a centralized `@RestControllerAdvice` exception handler for consistent error responses (`FileValidationException`, `DocumentNotFoundException`, `AIProcessingException`, etc).

Processing runs on a separate thread via Spring's `@Async`, so uploads never block on PDF extraction or the AI call.

## Known limitation

Uploaded files are stored on local disk, which doesn't persist across restarts on Render's free tier (ephemeral storage). The extracted text, summary, and status are all saved to PostgreSQL and persist correctly — only the original file itself may be lost on a restart. A production version would use cloud object storage (e.g. AWS S3) instead.

## Running locally

1. Clone the repo and set up a local PostgreSQL database
2. Create a `.env` file with:
DB_PASSWORD=your_postgres_password
OPENAI_API_KEY=your_openai_key
3. Run via IntelliJ or `mvn spring-boot:run`
4. API available at `http://localhost:8081`, Swagger UI at `http://localhost:8081/swagger-ui/index.html`

## What I learned building this

Beyond the core Spring Boot/JPA work, this project involved real debugging: fixing PostgreSQL authentication issues caused by password drift across projects, containerizing the app with Docker for deployment, and resolving a JDBC URL format mismatch between Render's PostgreSQL connection string and what Spring Boot expects.