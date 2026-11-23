# Task Manager API

A RESTful Task Management API built with Java 21, Spring Boot, and MySQL.

## Technologies Used

- Java 21
- Spring Boot 4.1.2
- Spring Data JPA
- MySQL
- Maven
- JUnit 5

## Features

- Create new tasks
- Retrieve all tasks
- Retrieve a task by ID
- Update existing tasks
- Delete tasks
- Input validation
- Global exception handling
- MySQL database integration
- Automated unit and integration testing

## Project Structure

```text
src/
├── main/
│   ├── java/task_manager/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   └── TaskManagerApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/task_manager/
```

## Database Configuration

Create a MySQL database:

```sql
CREATE DATABASE task_manager;
```

Configure these environment variables in IntelliJ:

```text
DB_USERNAME=root
DB_PASSWORD=admin1234

Replace `your_mysql_password` with your actual MySQL password.

## Running the Application

Run `TaskManagerApplication.java` from IntelliJ IDEA.

Application URL:

```text
http://localhost:8080
```

## API Endpoints

Base URL: `/api/tasks`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/tasks` | Get all tasks |
| GET | `/api/tasks/{id}` | Get task by ID |
| POST | `/api/tasks` | Create a task |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |

## Running Tests

On Windows, run:

```bash
mvnw.cmd test
```

## Author

**Muhammad Hamid Daud**
**hamidjadun92@gmail.com**

GitHub: [@hamidjadun](https://github.com/hamidjadun)