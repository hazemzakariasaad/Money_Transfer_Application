# 💸 Money Transfer Application

## Overview
The **Money Transfer Application** is a secure and efficient platform that enables users to register, authenticate, and seamlessly transfer money between accounts. The system supports managing **favorite recipients**, **retrieving transaction history**, and **enhancing performance** using **Redis caching**.

Built with **Spring Boot**, the application follows the **MVC architecture**, adhering to **clean code principles**, **SOLID design**, and **robust error handling** to ensure reliability and scalability.

---

## ✨ Features
### 🔐 User Management
- User **registration and authentication** with **JWT-based security**.
- Custom exception handling for issues like **incorrect credentials** or **email duplication**.
- Secure password hashing for **enhanced protection**.

### 💰 Money Transfers
- Seamlessly transfer **funds** between user accounts.
- **Prevent duplicate recipients** when managing favorite contacts.
- Real-time **validation** to ensure secure transactions.

### 📜 Transaction History
- Retrieve and view **detailed transaction records**.
- Optimize history retrieval with **efficient queries**.

### ⚡ Caching with Redis
- **Improve performance** and reduce database load by caching frequent queries.
- Store and retrieve user data quickly for **faster response times**.

### 📊 Centralized Logging
- Integrated **logging system** to track transactions and debug errors effectively.
- Maintain logs for **security audits and performance monitoring**.

---

## 🛠️ Tech Stack
| Technology   | Purpose |
|-------------|---------|
| **Java 17**  | Core backend development |
| **Spring Boot** | Application framework |
| **Hibernate/JPA** | ORM for database interaction |
| **PostgreSQL** | Database for storing users & transactions |
| **Redis** | Caching for optimized performance |
| **JWT (JSON Web Token)** | Secure authentication mechanism |
| **Swagger** | API documentation & testing |

---

## 📂 Project Structure
The application follows a **Model-View-Controller (MVC) architecture**:

```
src/
│── com.example.moneytransfer
│   ├── controllers/      # REST Controllers (UserController, MoneyTransferController)
│   ├── services/         # Business logic (AuthService, TransactionService)
│   ├── models/           # Data entities (UserEntity, TransactionEntity)
│   ├── repositories/     # JPA Repositories for database access
│   ├── exceptions/       # Custom exception handling classes
│   ├── config/           # Security & application configurations
│   ├── logging/          # Centralized logging system
│   ├── caching/          # Redis caching setup
│   ├── utils/            # Utility functions for validation & processing
```

---

## 🚀 Usage
### 1️⃣ Register a New User
Send a `POST` request to:
```
/auth/register
```
with:
```json
{
  "email": "user@example.com",
  "password": "securePassword123"
}
```

### 2️⃣ Login & Get JWT Token
Send a `POST` request to:
```
/auth/login
```
with valid credentials. A JWT token will be returned.

### 3️⃣ Transfer Money
Use:
```
/transfer
```
to send money between accounts.

### 4️⃣ Manage Favorite Recipients
- Add a recipient: `POST /recipients/add`
- View saved recipients: `GET /recipients/all`

### 5️⃣ View Transaction History
Retrieve user transactions via:
```
/transactions/history
```

---

## 🛡️ Custom Exception Handling
The application provides **structured error handling** for common scenarios:

| Exception | Description |
|-----------|------------|
| `EmailAlreadyUsedException` | Thrown when registering with an existing email. |
| `PasswordMismatchException` | Raised when passwords do not match. |
| `IncorrectCredentialsException` | Triggered when login credentials are invalid. |
| `AuthenticationFailureException` | Occurs when authentication fails. |

---

## 📝 API Documentation
Swagger is integrated for API testing and documentation.  
Visit:
```
http://localhost:8080/swagger-ui/
```
to explore and test the API.


