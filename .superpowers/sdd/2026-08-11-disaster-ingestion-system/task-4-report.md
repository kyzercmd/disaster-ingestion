# Task 4 Report: REST Controllers, CORS Configuration & Global Exception Handler

## Status
- **Status:** DONE
- **Commit Hash:** `d8fba2f9f287f2a73b663e5245de8e155bdbe30f` (`d8fba2f`)
- **Total Tests Passed:** 18 / 18

## Implementation Summary
1. **`CorsConfig.java`**: Added WebMvcConfigurer bean to map `/api/**` endpoints, allowing CORS for `http://localhost:5173` with standard HTTP methods (`GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`).
2. **`GlobalExceptionHandler.java`**: Added Spring `@RestControllerAdvice` handling custom domain exceptions (`InvalidEventException`, `CsvParseException`, `StorageException`) and general `Exception`, returning standardized JSON error responses.
3. **`EventController.java`**: Implemented REST API endpoints for disaster event management:
   - `POST /api/events` (JSON ingestion with duplicate check, validation, and derived field calculation)
   - `POST /api/events/upload` (CSV file upload via `MultipartFile`)
   - `GET /api/events` (CSV download endpoint)
   - `GET /api/events/summary` (Summary statistics endpoint)
4. **`EventControllerTest.java`**: Added unit tests using `@WebMvcTest(EventController.class)` verifying `POST /api/events` returns HTTP 201 Created and `GET /api/events/summary` returns HTTP 200 OK with expected JSON.

## Verification & Test Results
- Ran `mvn test -Dtest=EventControllerTest`: 2 tests passed.
- Ran full test suite `mvn test`: 18 tests passed across 6 test classes.
