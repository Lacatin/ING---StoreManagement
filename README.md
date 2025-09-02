# ING Homework - Store Management Application

A simple Spring Boot application for managing products in a store. This project demonstrates RESTful APIs, HTTP basic authentication, role-based access, and exception handling using Spring Security.

---

## Table of Contents

- [Features](#features)  
- [Technologies](#technologies)
- [Testing](#technologies)  

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
