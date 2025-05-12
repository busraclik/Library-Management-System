# Library Management System
Library Management System with Spring Boot

## Project Overview
This project aims to develop a comprehensive **Library Management System** using **Spring Boot 3**, **Java 21**, and a **PostgreSQL** database. The system enables librarians to manage books, users, and borrowing/returning processes. It also includes user authentication, authorization, API documentation, and testing.

## Technology Stack
- **Backend**: Spring Boot 3, Java 21
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT for authentication and authorization
- **Build Tool**: Maven
- **Testing**: Spring Boot Test (Unit and Integration Tests)
- **API Documentation**: Swagger/OpenAPI
- **Version Control**: Git
- **Containerization**: Docker, Docker Compose

## Features
### 1. Book Management
- **Add a Book**: Librarians can add books with details like title, author, ISBN, publication date, and genre. Data validation is implemented.
- **View Book Details**: Both librarians and patrons can view detailed information about a book.
- **Search for Books**: Books can be searched by title, author, ISBN, or genre, with pagination for results.
- **Update Book Information**: Librarians can update existing book information.
- **Delete a Book**: Librarians can delete books from the system.

### 2. User Management
- **Register a User**: Users (librarians or patrons) can register, providing personal information. Role management is implemented.
- **View User Details**: Librarians can view user information.
- **Update User Information**: Librarians can update user details.
- **Delete a User**: Librarians can delete users from the system.
- **User Authentication and Authorization**: Implemented using Spring Security and JWT with role-based access control.

### 3. Borrowing and Returning
- **Borrow a Book**: Patrons can borrow available books. Borrowing dates and due dates are tracked.
- **Return a Book**: Patrons can return borrowed books. Availability and borrowing records are updated.
- **View Borrowing History**: Users can view their borrowing history, while librarians can view all users' histories.
- **Manage Overdue Books**: The system tracks overdue books, and librarians can generate overdue book list.

## API Endpoints
The system exposes RESTful API endpoints for each feature, accessible via HTTP methods (GET, POST, PUT, DELETE). You can find the API documentation in Swagger/OpenAPI format.


## Running the Application

### Prerequisites
- **Docker** (for containerization)
- **Docker Compose** (to manage both the database and application containers)

The application is containerized. Docker Compose will automatically build and run the application, along with the PostgreSQL database.

### Instructions
1. **Clone the repository**:
   ```bash
   git clone git@github.com:busraclik/Library-Management-System.git
   cd Library-Management-System

    Build and run using Docker Compose:
    Since both the PostgreSQL database and the Spring Boot application are containerized, you can use the following command to build and start both containers:

    docker-compose up --build

    This will start:

        The PostgreSQL database on port 5432.

        The Spring Boot application on port 8080.

    Access the Application:

        The backend application will be available at http://localhost:8080.

        The H2 database console (for testing purposes) can be accessed at http://localhost:8080/h2-console.

    PostgreSQL Configuration:
    The PostgreSQL database is configured as follows:

        Database: library_db

        Username: busra

        Password: busra123

    The Spring Boot application will connect to this database using the following URL:

        jdbc:postgresql://db:5432/library_db

    Spring Boot Configuration:
    The application is configured to use environment variables for connecting to the PostgreSQL database:

        SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/library_db

        SPRING_DATASOURCE_USERNAME: busra

        SPRING_DATASOURCE_PASSWORD: busra123

Testing

Unit and integration tests are written using Spring Boot Test. The application will use PostgreSQL for the production environment, while H2 will be used for in-memory testing during development.
API Documentation

After running the application, you can explore the available API endpoints through Swagger. Visit http://localhost:8080/swagger-ui.html to view the API documentation.
Postman Collection

## Postman Collection

You can also explore and test the API with the pre-configured Postman collection:

[Library Management System Postman Collection](https://www.postman.com/busraclik/library-management-system/collection/zo0ama1/library-management-project?action=share&creator=28859079)

## NOTE: Data Initialization

To populate the system with sample data, the DataInitializer class is implemented as a CommandLineRunner. This class automatically runs when the application starts and adds some initial data to the database.
Sample Data:

    Users: It creates two users:

        Librarian User: A librarian with administrative privileges.

        Patron User: A user who can borrow books.

    Books: Ten sample books are added to the database, including titles like The Silent Patient, Educated, The Midnight Library, etc.

    Borrow Records: A sample borrowing record is created for the patron, who borrows a book.

This initializer will only run once when the application is first started if there are no existing users or books in the database.