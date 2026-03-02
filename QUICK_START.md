# TradeBridge 26668 - Quick Start Guide

## Project Renamed Successfully ✅
- **Old Name**: SupplyGate 26514
- **New Name**: TradeBridge 26668
- **All security features removed** (JWT, 2FA, email services)
- **Focused on assessment requirements only**

## Prerequisites
- Java 17+
- PostgreSQL 13+
- Maven 3.6+

## Setup Steps

### 1. Create Database
```sql
CREATE DATABASE tradebridge_db;
```

### 2. Configure Database (if needed)
Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tradebridge_db
    username: postgres
    password: YOUR_PASSWORD
```

### 3. Build Project
```bash
mvn clean install
```

### 4. Run Application
```bash
mvn spring-boot:run
```

### 5. Access Swagger UI
Open browser: http://localhost:8081/swagger-ui.html

## Testing the Implementation

### Test Location Saving
```bash
POST http://localhost:8081/api/locations
Content-Type: application/json

{
  "structureCode": "01",
  "structureName": "Kigali",
  "structureType": "PROVINCE"
}
```

### Test Pagination & Sorting
```bash
GET http://localhost:8081/api/users?page=0&size=10&sortBy=creationDate&direction=DESC
```

### Test existBy() Method
```bash
GET http://localhost:8081/api/users/exists/email/test@example.com
```

### Test Province Query
```bash
GET http://localhost:8081/api/users/province/code/01
GET http://localhost:8081/api/users/province/name/Kigali
```

## Assessment Criteria Checklist

- [x] **ERD with 5 tables** - User, Location, Store, Product, Review
- [x] **Location saving** - POST /api/locations
- [x] **Sorting** - All GET endpoints support sortBy & direction params
- [x] **Pagination** - All GET endpoints support page & size params
- [x] **Many-to-Many** - Explained in IMPLEMENTATION_GUIDE.md
- [x] **One-to-Many** - Store→Products, Product→Reviews, Location→Users
- [x] **One-to-One** - User↔Store
- [x] **existBy()** - Implemented in all repositories
- [x] **Province queries** - By code and by name

## Key Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/locations | Save location |
| GET | /api/locations/provinces | Get all provinces |
| GET | /api/locations/exists/{code} | Check location exists |
| POST | /api/users | Save user |
| GET | /api/users | Get users (paginated & sorted) |
| GET | /api/users/exists/email/{email} | Check user exists |
| GET | /api/users/province/code/{code} | Get users by province code |
| GET | /api/users/province/name/{name} | Get users by province name |
| POST | /api/stores | Save store |
| GET | /api/stores | Get stores (paginated) |
| POST | /api/products | Save product |
| GET | /api/products | Get products (paginated & sorted) |
| POST | /api/reviews | Save review |
| GET | /api/reviews | Get reviews (paginated) |

## Documentation Files

1. **IMPLEMENTATION_GUIDE.md** - Detailed explanation of all implementations (READ THIS FOR ASSESSMENT)
2. **MIGRATION_SUMMARY.md** - What changed from SupplyGate to TradeBridge
3. **README.md** - Project overview

## Troubleshooting

### Port Already in Use
Change port in `application.yml`:
```yaml
server:
  port: 8082
```

### Database Connection Error
- Ensure PostgreSQL is running
- Check username/password in application.yml
- Verify database exists: `\l` in psql

### Build Errors
```bash
mvn clean
mvn install -U
```

## Project Structure
```
trade_bridge_26668/
├── model/          (5 entities)
├── repository/     (5 repositories with custom queries)
├── service/        (5 services)
├── controller/     (5 REST controllers)
└── TradeBridge26668Application.java
```

## What Was Removed
- All security configurations (SecurityConfig, JWTFilter)
- Authentication & authorization logic
- JWT token generation/validation
- Two-factor authentication
- Password reset functionality
- Email services
- User verification workflow
- Message & notification systems
- Dashboard & global search features
- Image upload functionality
- All security-related dependencies

## What Remains (Assessment Focus)
- 5 core entities with relationships
- Location saving implementation
- Pagination & sorting
- existBy() methods
- Province query methods
- Clean REST API endpoints
- Swagger documentation

## Success Indicators
✅ Application starts without errors
✅ Swagger UI accessible at http://localhost:8081/swagger-ui.html
✅ All endpoints visible in Swagger
✅ Database tables auto-created (check with `\dt` in psql)
✅ Can create locations, users, stores, products, reviews
✅ Pagination works with page/size parameters
✅ Sorting works with sortBy/direction parameters
✅ Province queries return correct results

## Need Help?
Refer to **IMPLEMENTATION_GUIDE.md** for:
- Detailed explanation of each implementation
- Logic behind relationships
- How pagination/sorting works
- Query method explanations
- Example requests and responses
