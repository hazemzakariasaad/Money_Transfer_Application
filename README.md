# BackendBankMasr Application

BackendBankMasr is a secure and high-performance banking system designed to handle high volumes of transactions and user activities. The system prioritizes security, performance, and scalability, ensuring robust operation even under high load.

## Features

- **Security**: Implemented using JWT for authentication with HTTPS support to ensure data is transmitted securely.
- **Rate Limiting**: Configured at the gateway level to prevent service overload and enhance security against DDoS attacks.
- **Single Device Session**: Ensures that users can only be logged in from one device at a time, enhancing security.
- **High Performance**: Optimized with Redis caching to handle high volumes of data efficiently and maintain fast response times.
- **Scalability**: Designed to scale seamlessly with increasing load, ensuring consistent performance.
- **Email Verification**: Requires users to verify their email during the registration process, enhancing account security.

## Technology Stack

- **Spring Boot**: For creating stand-alone, production-grade Spring-based applications easily.
- **Spring Security**: For authentication and security.
- **Redis**: Used for caching and session storage to enhance performance.
- **PostgreSQL**: As the primary database for storing user and transaction data.
- **JWT**: For handling authentication and ensuring only one active session per user.
- **Spring Cloud Gateway**: For rate limiting and routing configurations.

## Architecture

The application uses a gateway-based architecture where all services are routed through a Spring Cloud Gateway, ensuring centralized management of rate limiting and routing. The backend is structured around core banking functionalities with separate services for handling user management and transaction processing.

### Rate Limiting

Rate limiting is implemented at the API gateway to ensure equitable resource usage and protect against abuse and traffic spikes.

### Security

Security is enforced through JWT, where tokens are generated and validated using a secret key. Tokens are stored in Redis, allowing for efficient validation and single-session enforcement by invalidating tokens on new logins.

## Setup and Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourgithub/backendBankmasr.git
### Navigate to the project directory:
cd backendBankmasr

### Install dependencies:
mvn install


