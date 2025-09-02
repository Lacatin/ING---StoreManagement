# ING Homework - Store Management Application

A simple Spring Boot application for managing products in a store. This project demonstrates RESTful APIs, HTTP basic authentication, role-based access, and exception handling using Spring Security.

---

## Table of Contents

- [Features](#features)  
- [Technologies](#technologies)
- [Testing](#technologies)
- [Future Developments](#future-developments) 

---

## Features

- Create, read, update, and delete products  
- Unique product names  
- Role-based access:
  - `USER` role: can view products  
  - `ADMIN` role: can create, update, and delete products  
- Global exception handling with structured error responses (`ErrorDto`)  
- Integration with MapStruct for entity-to-DTO mapping  
- CSRF protection compatible with Postman and browser clients  

---

## Technologies

- Java 24  
- Spring Boot 3.5.5  
- Spring Security 6.5  
- Hibernate / JPA  
- MapStruct 1.5.5  
- Lombok  
- In Memory H2 for easier local testing 
- JUnit 5 & Mockito  

---

## Testing
Endpoints can be tested using the [attached Postman collection](src/main/resources/ING%20-%20Store%20Management.postman_collection.json)

## Future Developments

This project is a solid foundation, but there are several improvements that could be made for production readiness:

- **Increase Test Coverage**  
  - Currently, unit tests cover only the `ProductService`.  
  - Future work should include tests for controllers, security configuration, exception handling, and edge cases.  
  - Consider using integration tests with an in-memory or real database to validate full application flows.  

- **Replace H2 In-Memory Database**  
  - For production, H2 should be replaced with a persistent database (e.g., PostgreSQL, MySQL).  
  - This will allow data to persist across application restarts and support multiple environments.  

- **Implement Proper User Authentication & Management**  
  - Currently, authentication uses in-memory users with static credentials.  
  - Future development should implement a `User` entity and repository to store users in the database.  
  - Roles should be managed dynamically, allowing creation and modification of users and roles at runtime.  
