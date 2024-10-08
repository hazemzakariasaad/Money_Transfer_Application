# Money Transfer Application

## Overview
This project is a **Money Transfer Application** that allows users to register, log in, and transfer money between accounts. It supports managing favorite recipients, retrieving transaction history, and caching for improved performance using **Redis**. The project is built with **Spring Boot** and follows the **MVC architecture** with a focus on clean code, SOLID principles, and efficient error handling.

## Features
- **User Management**: 
  - Registration and authentication with JWT.
  - Custom exception handling for issues like incorrect credentials or email already in use.
- **Money Transfer**: 
  - Transfer funds between user accounts.
  - Add and manage favorite recipients, ensuring no duplicates.
- **Transaction History**: 
  - Retrieve and view past transactions.
- **Caching**: 
  - Redis caching for enhanced performance, reducing the load on the database.
- **Logging**: 
  - Centralized logging to track and debug application behavior.

## Project Structure
The project follows the **Model-View-Controller (MVC)** pattern:
- **Model**: Represents data entities like `UserEntity`, `TransactionEntity`.
- **View**: REST controllers expose endpoints (e.g., `UserController`, `MoneyTransferController`).
- **Controller**: Services like `AuthService` and `TransactionService` handle business logic.
- **Repository**: JPA repositories manage database interactions.

## Tech Stack
- **Java 17**
- **Spring Boot**
- **Hibernate/JPA**
- **PostgreSQL** (Database)
- **Redis** (Caching)
- **JWT** (Authentication)
- **Swagger** (API Documentation)

## Usage

- **Register a User**:  
  Use the `/auth/register` endpoint to register a new user.

- **Login**:  
  Authenticate using the `/auth/login` endpoint to obtain a JWT token.

- **Money Transfer**:  
  Use the `/transfer` endpoint to transfer money between accounts.

- **Manage Favorite Recipients**:  
  - Add recipients using the `/recipients/add` endpoint.  
  - Retrieve favorite recipients via `/recipients/all`.

- **Transaction History**:  
  View transaction history using the `/transactions/history` endpoint.

## Custom Exception Handling

This project includes custom exception classes to handle various scenarios:

- `EmailAlreadyUsedException`
- `PasswordMismatchException`
- `IncorrectCredentialsException`
- `AuthenticationFailureException`

## Logging

The application uses **Spring Boot's logging** features to record actions and errors, improving debugging and traceability.

