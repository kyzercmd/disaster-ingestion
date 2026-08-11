# Unified Disaster Data Ingestion System (Bangladesh) — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a full-stack web application that lets users add, upload, validate, and download disaster event records for Bangladesh in a unified, hazard-agnostic CSV schema ready for ML.

**Architecture:** Java Spring Boot REST API stores validated records in a flat CSV file (`disaster_events.csv`). A React (Vite + TypeScript) frontend communicates with the API over HTTP. CSV upload rows are validated in parallel via a fixed thread pool on the backend.

**Tech Stack:** Java 17, Spring Boot 3, Maven; React 18, Vite, TypeScript, Axios; CSV stored at `backend/data/disaster_events.csv`.

## Global Constraints

- Java 17 minimum; Spring Boot 3.x; Maven wrapper (`./mvnw`) must work without global Maven install.
- React 18 + TypeScript; Vite 5; Node >= 18.
- All backend packages under `com.bd.disaster`.
- REST base path: `/api`; CORS must allow `http://localhost:5173`.
- CSV column order (header): `event_id,hazard_type,start_date,end_date,division,district,upazila,affected_people,deaths,economic_loss_bdt,economic_loss_usd,duration_days,severity_category,source_url,notes`.
- `event_id` must be globally unique across the CSV; reject duplicates.
- Dates: ISO format `YYYY-MM-DD`; `end_date` >= `start_date`.
- `affected_people`, `deaths` >= 0 (integer); `economic_loss_bdt`, `economic_loss_usd` >= 0 (numeric).
- `division` must be one of exactly 8 values: Barishal, Chattogram, Dhaka, Khulna, Mymensingh, Rajshahi, Rangpur, Sylhet.
- `hazard_type` must be one of: `flood`, `cyclone`, `drought`, `river_erosion`.
- `duration_days` = `(end_date - start_date).days + 1` (computed, not user-supplied).
- `economic_loss_usd` = `economic_loss_bdt / 110` when not supplied (computed).
- Multi-threaded upload: fixed thread pool of 4 threads; each CSV row is a `Callable<ValidationResult>`.
- UI style: government-portal look — white/light-grey background, Bangladesh green (#006A4E) primary, red (#F42A41) accent.
- Frontend dev server: `npm run dev` on port 5173.
- Backend dev server: `./mvnw spring-boot:run` on port 8080.

---

See the full plan in the artifact for all 7 tasks with complete code.
