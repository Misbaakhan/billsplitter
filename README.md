# Bill Splitter & Debt Tracker API

A production-grade REST API for splitting bills and tracking debts among friends, similar to Splitwise.

## Tech Stack
- Java 21 + Spring Boot
- Spring Security + JWT Authentication
- MySQL + Spring Data JPA
- Redis Caching
- Docker + Docker Compose
- GitHub Actions CI/CD

## Features
- User registration and login with JWT
- Create groups and add members
- Add expenses and split automatically
- Debt simplification algorithm (minimizes transactions)
- Redis caching for optimized performance

## Live Deployment
This API is deployed on AWS EC2 using Docker containers.

Base URL:
http://13.202.141.127:8080

Example endpoint:
POST /api/auth/register

## Running Locally with Docker

```bash
docker-compose up --build
```
## API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register user |
| POST | /api/auth/login | Login |
| POST | /api/groups | Create group |
| POST | /api/expenses | Add expense |
| GET | /api/debts/simplified/{groupId} | Get simplified debts |
| PUT | /api/debts/settle/{debtId}/group/{groupId} | Settle debt |