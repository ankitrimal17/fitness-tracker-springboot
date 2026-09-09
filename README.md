# Fitness Tracker & Health Analytics

A BSc. CSIT university project that helps a user record workouts, meals, goals, and weight logs, then review a weekly health summary. The application combines a Spring Boot REST API, MySQL persistence, Java Stream analytics, and a responsive HTML/CSS/JavaScript frontend.

## Problem statement and objectives

Fitness information is often recorded in separate places or not recorded consistently. This project provides one simple application for tracking activity, nutrition, goals, and weight, while turning the recorded data into a weekly summary.

- Build a full-stack CRUD application with Spring Boot and MySQL.
- Persist health-tracking records through JPA/Hibernate.
- Provide a responsive browser interface using Fetch API.
- Calculate weekly workout, nutrition, weight, and goal analytics with Java Streams.
- Demonstrate validation, exception handling, REST APIs, and basic application monitoring.

## Features and technology stack

- User profile management; workout, meal, goal, and weight-log CRUD.
- Weekly fitness, nutrition, weight, and goal analytics.
- Loading, empty, error, confirmation, and toast states in the frontend.
- Actuator health, info, and metrics endpoints.

Java 25, Spring Boot 3.5.10, Spring Web, Spring Data JPA, Bean Validation, Actuator, Hibernate/JPA, MySQL 8, Maven Wrapper, HTML, CSS, JavaScript, and Fetch API are used. Authentication, external APIs, AI/ML, and automated browser testing are not included.

## Architecture

```text
Browser → HTML / CSS / JavaScript → Fetch API → REST Controllers
        → Services → Spring Data JPA Repositories → Hibernate / JPA → MySQL
```

Controllers expose HTTP endpoints, services contain application logic, repositories access data, and entities represent database tables. Constructor injection is used for dependencies.

Analytics flow:

```text
Repositories → AnalyticsService → Java Streams → WeeklyAnalyticsResponse
             → AnalyticsController → JSON API → Analytics UI
```

Monitoring flow:

```text
Spring Boot Actuator → /actuator/health, /actuator/info, /actuator/metrics
```

## Database entities and relationships

| Entity | Important fields | Relationship |
|---|---|---|
| `User` | `id`, `name`, `email`, `age`, `height`, `weight`, `createdAt` | Owns many tracking records. |
| `Workout` | `id`, `date`, `type`, `duration`, `caloriesBurned`, `notes`, `user` | Many workouts to one user through `user_id`. |
| `Meal` | `id`, `date`, `mealType`, `foodName`, `calories`, `protein`, `carbs`, `fat`, `user` | Many meals to one user through `user_id`. |
| `Goal` | `id`, `goalType`, `targetValue`, `currentValue`, `startDate`, `targetDate`, `status`, `user` | Many goals to one user through `user_id`. |
| `WeightLog` | `id`, `date`, `weight`, `user` | Many weight logs to one user through `user_id`. |

Child records use `@ManyToOne` and `@JoinColumn(name = "user_id")`. The frontend uses user ID `1` as its current project user. Relationships are intentionally unidirectional from a tracking record to `User`, keeping JSON responses simple and avoiding recursive serialization.

## API reference

Successful creates return **201**, reads and updates return **200**, deletes return **204**, invalid validated input returns **400**, and missing resources or user references return **404**.

| Group | Existing endpoints |
|---|---|
| Users | `GET /api/users`, `GET /api/users/{id}`, `POST /api/users`, `PUT /api/users/{id}`, `DELETE /api/users/{id}` |
| Workouts | `GET /api/workouts`, `GET /api/workouts/{id}`, `GET /api/workouts/user/{userId}`, `POST /api/workouts`, `PUT /api/workouts/{id}`, `DELETE /api/workouts/{id}` |
| Meals | `GET /api/meals`, `GET /api/meals/{id}`, `GET /api/meals/user/{userId}`, `POST /api/meals`, `PUT /api/meals/{id}`, `DELETE /api/meals/{id}` |
| Goals | `GET /api/goals`, `GET /api/goals/{id}`, `GET /api/goals/user/{userId}`, `POST /api/goals`, `PUT /api/goals/{id}`, `DELETE /api/goals/{id}` |
| Weight logs | `GET /api/weight-logs`, `GET /api/weight-logs/{id}`, `GET /api/weight-logs/user/{userId}`, `POST /api/weight-logs`, `PUT /api/weight-logs/{id}`, `DELETE /api/weight-logs/{id}` |
| Analytics | `GET /api/analytics/weekly/{userId}` |
| Utility | `GET /api/test` |
| Actuator | `GET /actuator/health`, `GET /actuator/info`, `GET /actuator/metrics` |

Child requests use an owner reference such as `"user": { "id": 1 }`.

```json
{
  "user": { "id": 1 },
  "date": "2026-09-09",
  "type": "Running",
  "duration": 30,
  "caloriesBurned": 250,
  "notes": "Morning run"
}
```

## Analytics and Java Streams

`AnalyticsService` calculates the Monday-to-Sunday week for one existing user and returns `WeeklyAnalyticsResponse`.

- `filter` selects weekly workouts, meals, and weight logs, and completed goals.
- `toList` materializes filtered records; `sorted` orders weekly weight logs by date.
- `count` calculates workout and goal totals.
- `mapToInt` with `sum` calculates workout duration, calories burned, and meal calories.
- `mapToDouble` with `average` calculates protein, carbohydrate, and fat averages.
- `findFirst` selects the starting weekly weight; `reduce` selects the latest.

Streams keep aggregate calculations concise while the controller remains focused on HTTP. When no weekly meal or weight data exists, the service returns numeric zero values rather than `NaN`, `Infinity`, or null analytics values.

## Validation and error handling

Controllers use `@Valid` for create and update bodies. Entities use `@NotBlank`, `@NotNull`, `@Email`, `@Positive`, and `@PositiveOrZero` where appropriate. This covers required names, valid email, positive age/height/weight, positive workout duration, non-negative nutrition values, and positive weight-log values.

`Goal` also validates that `targetDate` is not before `startDate`; invalid requests return HTTP 400. Services verify user references and requested records. `ResourceNotFoundException` is handled by `ApiExceptionHandler`, returning a 404 JSON message.

## Frontend and monitoring

Spring Boot serves the frontend at `http://localhost:8080/`. Dashboard, Workouts, Meals & Nutrition, Goals, Progress, Analytics, and Profile/Settings are available. Detailed pages use live REST endpoints for user 1; they include loading, empty, retryable error, confirmation, and success-toast states. The visual system is adapted from the supplied Lovable design while remaining plain HTML/CSS/JavaScript served by Spring Boot; no separate React/Vite server is required. The dashboard now loads its overview values from the existing APIs.

Actuator exposes only `health`, `info`, and `metrics`. They provide basic availability, application information, and metric discovery without exposing additional actuator endpoints.

## Project structure

```text
FitnessMCS/
├── .mvn/wrapper/
├── src/main/
│   ├── java/com/fitness/tracker/
│   │   ├── controller/
│   │   ├── dto/WeeklyAnalyticsResponse.java
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   ├── service/
│   │   └── FitnessTrackerApplication.java
│   └── resources/
│       ├── application.properties
│       ├── application-local.properties
│       └── static/
│           ├── css/
│           ├── js/app.js
│           └── index.html
├── .gitignore
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Database setup and safe configuration

1. Install Java 25 and MySQL Server, then start MySQL.
2. Create or update the ignored local file `src/main/resources/application-local.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fitness_tracker?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

`application-local.properties` is ignored by Git. Never commit passwords. `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` can override the default configuration. Hibernate uses `ddl-auto=update` for local development.

## Run and verify

From the project root in PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Open [http://localhost:8080/](http://localhost:8080/). Example read-only checks:

```powershell
Invoke-WebRequest http://localhost:8080/api/test
Invoke-WebRequest http://localhost:8080/api/users/1
Invoke-WebRequest http://localhost:8080/api/analytics/weekly/1
Invoke-WebRequest http://localhost:8080/actuator/health
```

Build and syntax checks:

```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd test
node --check src/main/resources/static/js/app.js
```

The Maven test phase succeeds, but no automated Java test classes have been added.

## Known limitations

- User ID 1 is fixed in the frontend; there is no authentication or user selection.
- The frontend remains a single Spring Boot-served vanilla JavaScript application rather than the Lovable export's standalone React/Vite development stack.
- Automated Java tests and automated browser/device testing are not included.
- A local MySQL configuration is required before startup.

## Academic relevance: CLO3/CLO4

This project demonstrates object-oriented Java entities, collections and Java Streams, REST APIs, validation, exception handling, JPA/Hibernate persistence, relational database relationships, and full-stack integration. It supports CLO3/CLO4 outcomes involving Java application development, persistence, data processing, and service-oriented web design.

## Suggested 5–10 minute demonstration

1. Start MySQL and run `./mvnw.cmd spring-boot:run`.
2. Open Dashboard, then Workouts; create, edit, and delete a temporary workout.
3. Show Meals, Goals, and Progress as live CRUD pages.
4. Open Analytics; explain the weekly totals and Java Stream operations.
5. Show Profile/Settings and `/actuator/health` reporting `UP`.
6. Explain Browser → REST → Service → Repository → MySQL, then delete any temporary demo records.
