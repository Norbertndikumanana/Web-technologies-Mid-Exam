# Trade Bridge - Spring Boot E-Commerce Platform

## Project Overview
Trade Bridge is a RESTful API service demonstrating comprehensive database relationships and JPA functionalities for an e-commerce platform. The system manages users, stores, products, reviews, and Rwanda's complete administrative structure.

## Tech Stack
- **Framework:** Spring Boot 3.x
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA
- **Validation:** Spring Validation
- **Documentation:** Springdoc OpenAPI
- **Build Tool:** Maven

## Quick Start

### Prerequisites
- Java 17+
- PostgreSQL 13+
- Maven 3.6+

### Database Configuration
Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tradebridge
    username: your_username
    password: your_password
```

### Run Application
```bash
mvn clean install
mvn spring-boot:run
```

### Access API Documentation
- Swagger UI: http://localhost:8081/swagger-ui.html

---

## Assessment Criteria Implementation

### 1. Entity Relationship Diagram (ERD) - 5 Tables (3 Marks)

![ERD Diagram](ERD%20Mid_term.png)

#### Tables Overview:
1. **administrative_structure** - Rwanda's location hierarchy
2. **users** - User accounts (Clients, Suppliers, Industry Workers)
3. **stores** - Store information
4. **products** - Products sold in stores
5. **reviews** - Product reviews by users

#### Relationships Logic:

**Self-Referencing (Location ↔ Location):**
- A location can have a parent location
- Creates hierarchy: Province → District → Sector → Cell → Village
- Example: Gahogo Village → Nyarucyamo Cell → Nyamabuye Sector → Muhanga District → Southern Province

**Many-to-One (Users ↔ Location):**
- Many users can belong to one location
- Tracks user's geographical location

**One-to-One (User ↔ Store):**
- One user can own one store
- One store belongs to one user

**One-to-Many (Store ↔ Products):**
- One store can have many products
- Each product belongs to one store

**One-to-Many (Product ↔ Reviews):**
- One product can have many reviews
- Each review is for one product

**One-to-Many (User ↔ Reviews):**
- One user can write many reviews
- Each review is written by one user

---

### 2. Implementation of Saving Location (2 Marks)

#### How Data is Stored:

**Controller Layer** (`LocationController.java`):
```java
@PostMapping
public ResponseEntity<ApiResponse<Location>> saveLocation(@RequestBody Location location) {
    Location saved = locationService.saveLocation(location);
    return ResponseEntity.ok(new ApiResponse<>("Location saved successfully", saved));
}
```
- Receives JSON via POST to `/api/locations`
- Converts JSON to Location entity using `@RequestBody`
- Returns success message with saved data

**Service Layer** (`LocationService.java`):
```java
public Location saveLocation(Location location) {
    return locationRepository.save(location);
}
```
- Business logic layer
- Delegates to repository

**Repository Layer** (`LocationRepository.java`):
```java
public interface LocationRepository extends JpaRepository<Location, UUID>
```
- Spring Data JPA automatically generates SQL INSERT
- Saves to `administrative_structure` table

#### How Relationships are Handled:

**Self-Referencing Relationship** (`Location.java`):
```java
@ManyToOne
@JoinColumn(name="parent_id")
@JsonIgnore
private Location parent;
```
- `parent_id` foreign key references `structure_id` in same table
- `@JsonIgnore` prevents circular reference during serialization
- JPA automatically handles parent relationship
- Maintains referential integrity through foreign key constraint

**Example API Call:**
```bash
POST /api/locations
{
  "structureCode": "GHG",
  "structureName": "Gahogo",
  "structureType": "VILLAGE",
  "parent": {
    "structureId": "aaa04b00-e96e-40cb-8c7e-697d1d5c1cac"
  }
}
```

---

### 3. Sorting & Pagination Implementation (5 Marks)

#### Sorting Implementation:

**Controller** (`UserController.java`):
```java
@GetMapping
public ResponseEntity<Page<User>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "creationDate") String sortBy,
        @RequestParam(defaultValue = "DESC") String direction) {
    
    Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC") 
        ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    return ResponseEntity.ok(userService.getAllUsers(pageable));
}
```

**How Sorting Works:**
- Uses `Sort` class from Spring Data JPA
- `sortBy` parameter specifies field (e.g., "firstName", "creationDate")
- `direction` parameter specifies ASC or DESC
- `Sort.by(sortDirection, sortBy)` creates Sort object

**Example API Calls:**
```bash
GET /api/users?sortBy=firstName&direction=ASC
GET /api/products?sortBy=price&direction=DESC
```

**Generated SQL:**
```sql
SELECT * FROM users ORDER BY creation_date DESC;
```

#### Pagination Implementation:

**How Pagination Works:**
- Uses `Pageable` interface from Spring Data JPA
- `PageRequest.of(page, size, sort)` creates pagination request
- `page`: Page number (0-indexed)
- `size`: Number of records per page
- Returns `Page<T>` object with metadata

**Example API Call:**
```bash
GET /api/users?page=0&size=10&sortBy=creationDate&direction=DESC
```

**Response Structure:**
```json
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalPages": 5,
  "totalElements": 50,
  "first": true,
  "last": false
}
```

#### How Pagination Improves Performance:

**Without Pagination:**
```sql
SELECT * FROM users;  -- Returns ALL 10,000 users
```
- Loads all data into memory
- Slow response time
- High memory usage
- Poor user experience

**With Pagination:**
```sql
SELECT * FROM users ORDER BY creation_date DESC LIMIT 10 OFFSET 0;
```
- Only fetches 10 records at a time
- Faster query execution
- Reduced memory consumption
- Better database performance
- Improved user experience

**Performance Benefits:**
1. **Reduced Database Load** - Only queries needed records
2. **Lower Memory Usage** - Doesn't load entire dataset
3. **Faster Response Time** - Smaller data transfer
4. **Better Scalability** - Handles large datasets efficiently

**Repository Implementation:**
```java
@NonNull
Page<User> findAll(@NonNull Pageable pageable);
```
- Spring Data JPA automatically implements pagination
- No custom SQL needed

---

### 4. Many-to-Many Relationship (3 Marks)

**Note:** The current implementation uses One-to-One (User ↔ Store) instead of Many-to-Many. However, the system demonstrates understanding of relationships through multiple One-to-Many implementations.

**Current Implementation (One-to-One):**

**Store Entity:**
```java
@OneToOne
@JoinColumn(name = "user_id", unique = true)
private User user;
```

**User Entity:**
```java
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
private Store store;
```

**How it works:**
- One user can own one store
- `user_id` in stores table is a foreign key with UNIQUE constraint
- `mappedBy` indicates User is the inverse side
- Bidirectional relationship

**To implement Many-to-Many (User ↔ Store):**
Would require a join table `user_store` with columns:
- `user_id` (FK to users)
- `store_id` (FK to stores)
- Composite primary key (user_id, store_id)

---

### 5. One-to-Many Relationship (2 Marks)

#### Implementation 1: Store ↔ Products

**Store Entity:**
```java
@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JsonManagedReference("store-product")
private List<Product> products;
```

**Product Entity:**
```java
@ManyToOne
@JoinColumn(name = "store_id")
@JsonBackReference("store-product")
private Store store;
```

**Explanation:**
- One store can have many products
- `store_id` foreign key in products table references `store_id` in stores table
- `mappedBy = "store"` indicates Product owns the relationship
- `cascade = CascadeType.ALL` - operations on Store cascade to Products
- `@JsonManagedReference` and `@JsonBackReference` prevent circular serialization

**Foreign Key Usage:**
```sql
ALTER TABLE products 
ADD CONSTRAINT fk_store 
FOREIGN KEY (store_id) REFERENCES stores(store_id);
```

#### Implementation 2: Product ↔ Reviews

**Product Entity:**
```java
@OneToMany(mappedBy = "product")
@JsonManagedReference("product-review")
private List<Review> reviews;
```

**Review Entity:**
```java
@ManyToOne
@JoinColumn(name = "product_id")
@JsonBackReference("product-review")
private Product product;
```

**Explanation:**
- One product can have many reviews
- `product_id` foreign key in reviews table
- Maintains referential integrity

#### Implementation 3: User ↔ Reviews

**User Entity:**
```java
@OneToMany(mappedBy = "user")
@JsonManagedReference("user-review")
private List<Review> review;
```

**Review Entity:**
```java
@ManyToOne
@JoinColumn(name = "user_id")
@JsonBackReference("user-review")
private User user;
```

**Explanation:**
- One user can write many reviews
- `user_id` foreign key in reviews table

---

### 6. One-to-One Relationship (2 Marks)

#### Implementation: User ↔ Store

**User Entity (Owner Side):**
```java
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
@JsonManagedReference("user-store")
private Store store;
```

**Store Entity (Owned Side):**
```java
@OneToOne
@JoinColumn(name = "user_id", unique = true)
@JsonBackReference("user-store")
private User user;
```

**How Entities are Connected:**

1. **Foreign Key:** `user_id` in stores table references `user_id` in users table
2. **Unique Constraint:** `user_id` has UNIQUE constraint ensuring one-to-one
3. **Bidirectional:** Both entities can navigate to each other
4. **Cascade:** Operations on User cascade to Store
5. **Ownership:** Store owns the relationship (has @JoinColumn)

**Database Schema:**
```sql
CREATE TABLE stores (
    store_id UUID PRIMARY KEY,
    store_name VARCHAR(255),
    user_id UUID UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

**Example Usage:**
```java
// Create user
User user = new User();
user.setUsername("kalisa_patrick");

// Create store
Store store = new Store();
store.setStoreName("KGL Electronics");
store.setUser(user);

// Save store (user is saved automatically due to cascade)
storeRepository.save(store);
```

**API Example:**
```bash
POST /api/stores
{
  "storeName": "KGL Electronics",
  "storeDescription": "Quality electronics",
  "user": {
    "userId": "2bb87465-430b-4ebc-bb97-4d7918d59624"
  }
}
```

---

### 7. existBy() Method Implementation (2 Marks)

#### Repository Layer:

**UserRepository:**
```java
boolean existsByEmail(String email);
```

**How it Works:**
- Spring Data JPA automatically generates implementation
- Method naming convention: `existsBy` + `FieldName`
- Returns `true` if record exists, `false` otherwise
- No custom SQL needed

**Generated SQL:**
```sql
SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END 
FROM users u 
WHERE u.email = ?
```

#### Service Layer:

```java
public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
}
```

#### Controller Layer:

```java
@GetMapping("/exists/email/{email}")
public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
    return ResponseEntity.ok(userService.existsByEmail(email));
}
```

**API Usage:**
```bash
GET /api/users/exists/email/kalisa.patrick@example.com
```

**Response:**
```json
true  // if email exists
false // if email doesn't exist
```

#### How Existence Checking Works:

**Performance Benefits:**
- **Efficient Query:** Uses `COUNT(*)` instead of fetching full records
- **Fast Execution:** Only checks existence, doesn't retrieve data
- **Boolean Result:** Returns true/false immediately
- **Database Optimization:** Database can use indexes for quick lookup

**Use Cases:**
1. **Validation:** Check if email already exists before registration
2. **Duplicate Prevention:** Avoid creating duplicate records
3. **Conditional Logic:** Execute different logic based on existence

#### Other existBy() Implementations:

**StoreRepository:**
```java
boolean existsByStoreName(String storeName);
```
API: `GET /api/stores/exists/{storeName}`

**ProductRepository:**
```java
boolean existsByProductName(String productName);
```
API: `GET /api/products/exists/{productName}`

**LocationRepository:**
```java
boolean existsByStructureCode(String code);
```
API: `GET /api/locations/exists/{code}`

#### Comparison with findBy():

**existsBy():**
```sql
SELECT COUNT(*) > 0 FROM users WHERE email = ?
```
- Returns: boolean
- Fast, lightweight

**findBy():**
```sql
SELECT * FROM users WHERE email = ?
```
- Returns: User object
- Slower, retrieves all data

**Conclusion:** Use `existsBy()` when you only need to check existence, not retrieve data.

---

## API Endpoints

### Users
- `POST /api/users` - Create user
- `GET /api/users` - Get all users (paginated & sorted)
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/exists/email/{email}` - Check if email exists
- `GET /api/users/province/code/{code}` - Get users by province code
- `GET /api/users/province/name/{name}` - Get users by province name

### Stores
- `POST /api/stores` - Create store
- `GET /api/stores` - Get all stores (paginated & sorted)
- `GET /api/stores/{id}` - Get store by ID
- `GET /api/stores/exists/{storeName}` - Check if store exists

### Products
- `POST /api/products` - Create product
- `GET /api/products` - Get all products (paginated & sorted)
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/exists/{productName}` - Check if product exists

### Reviews
- `POST /api/reviews` - Create review
- `GET /api/reviews` - Get all reviews (paginated & sorted)
- `GET /api/reviews/{id}` - Get review by ID

### Locations
- `POST /api/locations` - Create location
- `GET /api/locations` - Get all locations
- `GET /api/locations/{id}` - Get location by ID
- `GET /api/locations/provinces` - Get all provinces
- `GET /api/locations/exists/{code}` - Check if location exists

---

## Data Initialization

The application automatically populates Rwanda's complete administrative structure on startup:
- 5 Provinces
- 30 Districts
- 416 Sectors
- 2 Cells per sector
- 2-4 Villages per cell
- 15 Sample users with Rwandan names

---

## Example Usage

### Create User
```bash
POST /api/users
{
  "userType": "SUPPLIER",
  "username": "kalisa_patrick",
  "firstName": "Kalisa",
  "lastName": "Patrick",
  "email": "kalisa.patrick@example.com",
  "phoneNumber": "0788555666",
  "companyName": "Kalisa Enterprises Ltd",
  "location": {
    "structureId": "700cf1d3-1229-4e61-8654-ddb14d544060"
  }
}
```

### Create Store
```bash
POST /api/stores
{
  "storeName": "KGL Electronics",
  "storeDescription": "Quality electronics in Kigali",
  "user": {
    "userId": "2bb87465-430b-4ebc-bb97-4d7918d59624"
  }
}
```

### Create Product
```bash
POST /api/products
{
  "productName": "Samsung Galaxy S24",
  "productDescription": "Latest flagship smartphone",
  "price": 850000,
  "store": {
    "storeId": "fd4212f7-042f-4840-8278-5e3a91e2cf57"
  }
}
```

### Create Review
```bash
POST /api/reviews
{
  "message": "Byiza cyane! Great product",
  "user": {
    "userId": "04102f20-2ae2-4c22-ad36-82e0b1a02482"
  },
  "product": {
    "productId": "fd4212f7-042f-4840-8278-5e3a91e2cf57"
  }
}
```

---

## Project Structure
```
trade_bridge_26668/
├── src/main/java/org/example/trade_bridge_26668/
│   ├── controller/      # REST Controllers
│   ├── model/          # Entity classes
│   ├── repository/     # JPA Repositories
│   ├── service/        # Business logic
│   └── dto/            # Data Transfer Objects
├── src/main/resources/
│   └── application.yml # Configuration
└── pom.xml            # Maven dependencies
```

---

## Author
**Student Name:** [Your Name]
**Student ID:** 26668
**Course:** Web Technologies
**Semester:** JAN 2026

---

## License
This project is for educational purposes.
