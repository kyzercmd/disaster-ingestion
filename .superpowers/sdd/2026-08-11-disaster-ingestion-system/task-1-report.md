# Task 1 Report: Backend Scaffold + Domain Model

## Summary of Accomplishments

- **Maven Project Config (`backend/pom.xml`)**: Scaffolded Spring Boot 3.2.5 project with Java 17, `spring-boot-starter-web`, `commons-csv:1.10.0`, and `spring-boot-starter-test`.
- **Application Class (`DisasterIngestionApplication.java`)**: Created under package `com.bd.disaster`.
- **Application Properties (`application.properties`)**: Configured server port (8080), disaster CSV path (`data/disaster_events.csv`), and thread pool size (4).
- **CSV Data File (`backend/data/disaster_events.csv`)**: Initialized with exact CSV header layout (`event_id,hazard_type,start_date,end_date,division,district,upazila,affected_people,deaths,economic_loss_bdt,economic_loss_usd,duration_days,severity_category,source_url,notes`).
- **Domain Model (`DisasterEvent.java`)**: Implemented getters and setters for all 15 fields.
- **Custom Exceptions**: Implemented base `DisasterDataException` and specialized exceptions:
  - `InvalidEventException`
  - `CsvParseException`
  - `StorageException`
- **Unit Tests (`DisasterEventTest.java`)**: Created tests for domain model getters, setters, and default null values for optional fields.
- **Git Commit**: Committed all changes with message `"feat: scaffold backend domain model and exceptions"`.

## Verification & Execution Details

- **Maven Wrapper**: Attempted `mvn wrapper:wrapper -Dmaven=3.9.6`. Skipped since `mvn` is not installed on system path (will be added in Task 7 as per requirements).
- **Test Execution**: Attempted running `mvn test -Dtest=DisasterEventTest`. `mvn` command not found on environment path.
- **Commit Hash**: `fed882e861b063b7041139a499a968e0aa466515`

## Status & Concerns

- **Status**: DONE_WITH_CONCERNS
- **Concerns**: Maven and Java binaries are not installed in the current environment PATH, so tests could not be executed directly during Task 1. Maven wrapper generation is deferred to Task 7 per project instructions.
