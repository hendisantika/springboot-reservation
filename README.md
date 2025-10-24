# Spring Boot Reservation System

A meeting room reservation system built with Spring Boot 3.5.7, PostgreSQL, Spring Security, and Thymeleaf.

## Features

- Meeting room reservation management
- User authentication and authorization with Spring Security
- Role-based access control (USER and ADMIN roles)
- RESTful API endpoints
- Thymeleaf templating for server-side rendering
- PostgreSQL database with Flyway migrations
- Custom validation annotations for reservation time slots
- Time slot validation (30-minute intervals)

## Technology Stack

- **Java**: 21
- **Spring Boot**: 3.5.7
- **Spring Data JPA**: For database operations
- **Spring Security 6**: For authentication and authorization
- **Thymeleaf**: Template engine for web views
- **PostgreSQL**: 17.x (Database)
- **Flyway**: Database migration tool
- **Lombok**: To reduce boilerplate code
- **Maven**: Build tool

## Prerequisites

- JDK 21 or higher
- Maven 3.6.3 or higher
- PostgreSQL 12 or higher

## Database Setup

1. Install and start PostgreSQL

2. Create the database and user:

```bash
# Connect to PostgreSQL
psql

# Create user and database
CREATE USER postgres WITH PASSWORD 'postgres' SUPERUSER;
CREATE DATABASE reservation OWNER postgres;

# Connect to reservation database and create schema
\c reservation
CREATE SCHEMA IF NOT EXISTS reservation;
```

## Configuration

The application is configured in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/reservation?currentSchema=reservation
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.database=POSTGRESQL
spring.flyway.enabled=true
spring.flyway.schemas=reservation
```

You can modify these settings to match your PostgreSQL configuration.

## Build and Run

### Build the Application

```bash
# Clean and build the project
mvn clean package

# Or skip tests
mvn clean package -DskipTests
```

### Run the Application

```bash
# Run using Maven
mvn spring-boot:run

# Or run the JAR file directly
java -jar target/springboot-reservation-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## Default Test Users

The application comes with pre-configured test users (password for all users is `password`):

| Username | Password | Role  | Full Name      |
|----------|----------|-------|----------------|
| naruto   | password | USER  | Uzumaki Naruto |
| sasuke   | password | USER  | Uchiha Sasuke  |
| sakura   | password | USER  | Haruno Sakura  |
| kakashi  | password | ADMIN | Hatake Kakashi |

## Pre-configured Meeting Rooms

The system includes 7 meeting rooms:

1. Konohagakure
2. Sunagakure
3. Kirigakure
4. Kusagakure
5. Kumogakure
6. Tsucikage
7. Raikage

## API Endpoints

### Public Endpoints

- `GET /loginForm` - Login page
- `POST /login` - Login processing
- `GET /js/**` - Static JavaScript resources
- `GET /css/**` - Static CSS resources

### Authenticated Endpoints (requires login)

- `GET /rooms` - List available meeting rooms for reservation
- Room reservation endpoints (secured)

## Database Schema

The application uses Flyway for database migrations. The schema includes:

### Tables

- **users**: User accounts with authentication details
- **meeting_room**: Available meeting rooms
- **reservable_room**: Room availability by date
- **reservation**: User reservations

### Relationships

- Reservations are linked to users and reservable rooms
- Reservable rooms reference meeting rooms
- All tables use proper foreign key constraints

## Custom Validation

The application includes custom validation annotations:

- `@EndTimeMustBeAfterStartTime`: Ensures reservation end time is after start time
- `@ThirtyMinutesUnit`: Validates time slots are in 30-minute intervals

## Project Structure

```
src/main/java/com/hendisantika/springbootreservation/
├── annotation/          # Custom validation annotations
├── config/             # Spring Security configuration
├── controller/         # REST controllers
├── domain/             # JPA entities
├── exception/          # Custom exceptions
├── repository/         # Spring Data repositories
└── service/            # Business logic services

src/main/resources/
├── db/migration/       # Flyway database migrations
├── templates/          # Thymeleaf HTML templates
└── application.properties
```

## Development

### Running Tests

```bash
mvn test
```

### Code Quality

The project uses:

- Lombok for reducing boilerplate code
- Spring Boot DevTools for hot reloading during development
- JPA validation for data integrity

## Security Configuration

The application uses Spring Security 6 with:

- Form-based authentication
- BCrypt password encoding
- Method-level security annotations
- CSRF protection enabled

## Database Migration

Flyway automatically runs migrations on application startup:

1. **V1__20052019_Init_Table.sql**: Creates initial database schema
2. **V2__20052019_Insert_Data.sql**: Inserts test data and creates stored procedures

## Troubleshooting

### Database Connection Issues

If you encounter database connection errors:

1. Verify PostgreSQL is running:
   ```bash
   pg_isready -h localhost -p 5432
   ```

2. Check credentials in `application.properties`

3. Ensure the database and schema exist:
   ```bash
   psql -U postgres -d reservation -c "\dt reservation.*"
   ```

### Build Issues

If Lombok annotations are not working:

- Ensure your IDE has Lombok plugin installed
- Run `mvn clean compile` to regenerate sources

## License

This is a demo project for educational purposes.

## Author

- **Name**: Hendi Santika
- **Email**: hendisantika@gmail.com
- **Telegram**: @hendisantika34

## Acknowledgments

Built with Spring Boot and modern Java technologies for demonstrating enterprise application development patterns.
