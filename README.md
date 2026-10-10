# Spring Boot Rate Limiter

A simple rate-limiting application built with Java and Spring Boot that demonstrates **Fixed Window** and **Sliding Window** algorithms to control API request frequency.

## Features

* Fixed Window Rate Limiting
* Sliding Window Rate Limiting
* REST APIs using Spring Boot
* HTTP `429 Too Many Requests` response when the limit is exceeded
* Unit testing with JUnit 5

## Tech Stack

* Java 21
* Spring Boot
* Spring Web
* Maven
* JUnit 5
* Postman

## API Endpoints

| Method | Endpoint                 | Description                 |
| ------ | ------------------------ | --------------------------- |
| GET    | `/api/ratelimit/fixed`   | Fixed Window rate limiter   |
| GET    | `/api/ratelimit/sliding` | Sliding Window rate limiter |

## Rate Limit Configuration

* Maximum requests: 5
* Time window: 10 seconds
* Successful request: `200 OK`
* Limit exceeded: `429 Too Many Requests`

These limits are configured for demonstration purposes.

## Getting Started

### Prerequisites

* Java 21 or compatible JDK
* Git
* Maven, or the included Maven Wrapper

### Run the Application

Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/spring-boot-rate-limiter.git
cd spring-boot-rate-limiter
```

Run the application:

```bash
./mvnw spring-boot:run
```

The application runs at:

`http://localhost:8080`

## Testing

Run the automated tests:

```bash
./mvnw clean test
```

You can also test the API endpoints using Postman.

Send multiple requests to either endpoint. The first five requests should be allowed within the applicable window; subsequent requests should receive `429 Too Many Requests` until the rate limit permits another request.

<img width="1680" height="1050" alt="Screenshot 2026-10-10 at 10 35 11 PM" src="https://github.com/user-attachments/assets/0a29fc3b-8240-49b4-8404-b5a77b16c252" />
<img width="1680" height="1050" alt="Screenshot 2026-10-10 at 10 35 04 PM" src="https://github.com/user-attachments/assets/aa2dcd84-ba27-461e-a409-87d08075bea2" />



## Project Structure

```text
src/
├── main/
│   ├── java/com/sinku/ratelimiter/
│   │   ├── controller/
│   │   └── service/
│   └── resources/
└── test/
    └── java/com/sinku/ratelimiter/
```

## Learning Objectives

* Understand API rate limiting
* Compare Fixed Window and Sliding Window algorithms
* Implement REST endpoints in Spring Boot
* Test API behavior using Postman and JUnit


## GOAL

This project is available for learning and educational purposes.
