# Unified Disaster Data Ingestion System — Bangladesh

Prototype web application for creating and maintaining a unified hazard-agnostic
disaster event dataset for Bangladesh (floods, cyclones, droughts, riverbank erosion).

## Quick Start

### Backend (Java 17 + Spring Boot 3)

```bash
cd backend
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:/usr/share/maven/bin:$PATH
./mvnw spring-boot:run
# API available at http://localhost:8080
```

### Frontend (React 18 + Vite + TypeScript)

```bash
cd frontend
source ~/.nvm/nvm.sh && nvm use 20
npm install
npm run dev
# UI available at http://localhost:5173
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | /api/events | Add a single event (JSON body) |
| POST | /api/events/upload | Upload CSV file (multipart/form-data, field name: `file`) |
| GET | /api/events | Download disaster_events.csv |
| GET | /api/events/summary | Summary statistics (JSON) |

## CSV Schema

| Column | Type | Required | Notes |
|--------|------|----------|-------|
| event_id | string | Yes | Unique, e.g. BD-FLOOD-2024-001 |
| hazard_type | string | Yes | flood \| cyclone \| drought \| river_erosion |
| start_date | date | Yes | YYYY-MM-DD |
| end_date | date | Yes | YYYY-MM-DD, >= start_date |
| division | string | Yes | One of 8 Bangladesh divisions |
| district | string | Yes | Free text |
| upazila | string | Yes | Free text |
| affected_people | integer | Yes | >= 0 |
| deaths | integer | Yes | >= 0 |
| economic_loss_bdt | numeric | Yes | >= 0, total loss in BDT |
| economic_loss_usd | numeric | Derived | = BDT / 110 if not provided |
| duration_days | integer | Derived | = (end - start).days + 1 |
| severity_category | string | Optional | low \| medium \| high \| extreme |
| source_url | string | Yes | URL to source report (must start with http) |
| notes | string | Optional | Free text |

## Architecture

- **Java 17 + Spring Boot 3** — REST API, OOP design, multi-threaded CSV validation
- **React 18 + TypeScript + Vite** — Government-portal style UI (Bangladesh green #006A4E)
- **CSV flat file** — `backend/data/disaster_events.csv` (no database required)

## OOP Design

| Pillar | Implementation |
|--------|----------------|
| Encapsulation | `DisasterEvent` — private fields with getters/setters |
| Abstraction | `EventRepository`, `EventValidationService`, `EventValidator` interfaces |
| Inheritance | `DisasterDataException` → `InvalidEventException`, `CsvParseException`, `StorageException` |
| Polymorphism | `BasicEventValidator` and `StrictEventValidator` both implement `EventValidator` |

## Multi-Threading

CSV upload uses `ExecutorService.invokeAll()` with a fixed pool of 4 threads.
Each CSV row is a `Callable<RowResult>` that validates and checks for duplicates.
`InterruptedException` and `ExecutionException` are handled per-row.

## Exception Handling

- `InvalidEventException` (400 Bad Request)
- `CsvParseException` (400 Bad Request) 
- `StorageException` (500 Internal Server Error)
- All unhandled exceptions → 500 with generic message
- `GlobalExceptionHandler` (`@RestControllerAdvice`) maps all exceptions to JSON `{"error": "..."}`
