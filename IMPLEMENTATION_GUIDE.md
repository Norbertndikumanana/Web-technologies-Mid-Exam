# TradeBridge 26668 - Implementation Documentation

## Project Overview
TradeBridge is a Spring Boot application demonstrating database relationships and JPA functionalities as per practical assessment requirements.

---

## 1. Entity Relationship Diagram (ERD) - 5 Tables

### Tables:
1. **users** - Stores user information
2. **administrative_structure** (Location) - Stores hierarchical location data
3. **stores** - Stores store/business information
4. **products** - Stores product catalog
5. **reviews** - Stores product reviews

### Relationships Explained:

#### One-to-One Relationship: User ↔ Store
- **Logic**: Each user can own only ONE store, and each store belongs to ONE user
- **Implementation**: 
  - Store entity has `@OneToOne` with `@JoinColumn(name = "user_id")`
  - User entity has `@OneToOne(mappedBy = "user")`
- **Foreign Key**: `user_id` in stores table references users table

#### One-to-Many Relationship: Location → Users
- **Logic**: One location (province/district) can have MANY users, but each user belongs to ONE location
- **Implementation**:
  - Location entity has `@OneToMany(mappedBy = "location")`
  - User entity has `@ManyToOne` with `@JoinColumn(name = "location_id")`
- **Foreign Key**: `location_id` in users table references administrative_structure table

#### One-to-Many Relationship: Store → Products
- **Logic**: One store can have MANY products, but each product belongs to ONE store
- **Implementation**:
  - Store entity has `@OneToMany(mappedBy = "store")`
  - Product entity has `@ManyToOne` with `@JoinColumn(name = "store_id")`
- **Foreign Key**: `store_id` in products table references stores table

#### One-to-Many Relationship: Product → Reviews
- **Logic**: One product can have MANY reviews, but each review is for ONE product
- **Implementation**:
  - Product entity has `@OneToMany(mappedBy = "product")`
  - Review entity has `@ManyToOne` with `@JoinColumn(name = "product_id")`
- **Foreign Key**: `product_id` in reviews table references products table

#### Many-to-One Relationship: User ← Reviews
- **Logic**: One user can write MANY reviews, but each review is written by ONE user
- **Implementation**:
  - User entity has `@OneToMany(mappedBy = "user")`
  - Review entity has `@ManyToOne` with `@JoinColumn(name = "user_id")`
- **Foreign Key**: `user_id` in reviews table references users table

---

## 2. Implementation of Saving Location

### How Location Data is Stored:

**File**: `LocationService.java`

```java
public Location saveLocation(Location location) {
    return locationRepository.save(location);
}
```

**Endpoint**: `POST /api/locations`

### Logic Explanation:
1. Location entity uses `@GeneratedValue(strategy = GenerationType.UUID)` for auto-generated IDs
2. Location has a self-referencing relationship with `parent` field for hierarchical structure (Province → District → Sector → Cell → Village)
3. When saving a location:
   - structureCode: Unique code for the location
   - structureName: Name of the location
   - structureType: Enum (PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE)
   - parent: Reference to parent location (null for provinces)
4. The `@OneToMany` relationship with User is managed through `cascade = CascadeType.ALL`, meaning when a location is saved, related users are also persisted

### Example Request:
```json
{
  "structureCode": "01",
  "structureName": "Kigali",
  "structureType": "PROVINCE",
  "parent": null
}
```

---

## 3. Implementation of Sorting Functionality

### How Sorting is Implemented:

**File**: `UserController.java`

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

### Logic Explanation:
1. **Sort Class**: Spring Data JPA's `Sort` class is used to define sorting criteria
2. **Sort.Direction**: Specifies ASC (ascending) or DESC (descending) order
3. **sortBy Parameter**: Specifies which field to sort by (e.g., "creationDate", "username", "email")
4. **PageRequest.of()**: Creates a Pageable object with page number, size, and Sort object
5. **Repository Method**: `findAll(Pageable pageable)` automatically applies sorting

### Example Requests:
- Sort by creation date descending: `GET /api/users?sortBy=creationDate&direction=DESC`
- Sort by username ascending: `GET /api/users?sortBy=username&direction=ASC`
- Sort products by price: `GET /api/products?sortBy=productPrice&direction=ASC`

---

## 4. Implementation of Pagination

### How Pagination Works:

**File**: `UserService.java` and `UserController.java`

```java
// Controller
public ResponseEntity<Page<User>> getAllUsers(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return ResponseEntity.ok(userService.getAllUsers(pageable));
}

// Service
public Page<User> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
}
```

### Logic Explanation:
1. **Pageable Interface**: Spring Data JPA interface that contains pagination information
2. **PageRequest.of(page, size)**: Creates a Pageable object
   - `page`: Zero-based page index (0 = first page)
   - `size`: Number of records per page
3. **Page<T> Return Type**: Contains:
   - `content`: List of entities for current page
   - `totalElements`: Total number of records
   - `totalPages`: Total number of pages
   - `number`: Current page number
   - `size`: Page size
4. **Performance Improvement**: 
   - Only loads required records from database (LIMIT and OFFSET in SQL)
   - Reduces memory usage by not loading all records at once
   - Faster response times for large datasets

### Example Requests:
- First page (10 records): `GET /api/users?page=0&size=10`
- Second page (20 records): `GET /api/users?page=1&size=20`
- Combined with sorting: `GET /api/users?page=0&size=10&sortBy=email&direction=ASC`

---

## 5. Implementation of Many-to-Many Relationship

### Note on Current Implementation:
The current ERD shows primarily One-to-Many and Many-to-One relationships. However, the Review entity demonstrates a pattern that could be extended to Many-to-Many:

**Current Pattern**: User ← Review → Product
- A user can review many products
- A product can be reviewed by many users
- Review acts as a join table with additional fields (message, creationDate)

### How to Implement Pure Many-to-Many:

If we wanted a pure Many-to-Many (e.g., User ↔ Store for favorites):

```java
// In User entity
@ManyToMany
@JoinTable(
    name = "user_favorite_stores",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "store_id")
)
private List<Store> favoriteStores;

// In Store entity
@ManyToMany(mappedBy = "favoriteStores")
private List<User> followers;
```

### Logic Explanation:
1. **@ManyToMany**: Defines many-to-many relationship
2. **@JoinTable**: Specifies the join table name and columns
3. **Join Table**: Automatically created with two foreign keys
   - `user_id` references users table
   - `store_id` references stores table
4. **mappedBy**: Indicates the owning side of the relationship

---

## 6. Implementation of One-to-Many Relationship

### Example: Store → Products

**File**: `Store.java` and `Product.java`

```java
// Store.java (One side)
@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
@JsonManagedReference("store-product")
private List<Product> product;

// Product.java (Many side)
@ManyToOne
@JoinColumn(name = "store_id")
@JsonBackReference("store-product")
private Store store;
```

### Logic Explanation:
1. **@OneToMany**: Placed on the "one" side (Store)
   - `mappedBy = "store"`: References the field name in Product entity
   - `cascade = CascadeType.ALL`: Operations on Store cascade to Products
2. **@ManyToOne**: Placed on the "many" side (Product)
   - `@JoinColumn(name = "store_id")`: Creates foreign key column
3. **Foreign Key Usage**: 
   - `store_id` column in products table stores the UUID of the store
   - Database enforces referential integrity
   - Prevents orphaned products (products without a store)
4. **Cascade Operations**: When a store is deleted, all its products are also deleted

---

## 7. Implementation of One-to-One Relationship

### Example: User ↔ Store

**File**: `User.java` and `Store.java`

```java
// User.java (Non-owning side)
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
@JsonManagedReference("user-store")
private Store store;

// Store.java (Owning side)
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
@JsonBackReference("user-store")
private User user;
```

### Logic Explanation:
1. **Owning Side (Store)**: 
   - Has `@JoinColumn(name = "user_id")` annotation
   - Stores the foreign key in its table
   - Controls the relationship
2. **Non-owning Side (User)**:
   - Has `mappedBy = "user"` attribute
   - References the field name in Store entity
3. **How Entities are Connected**:
   - Store table has `user_id` column (foreign key)
   - Each store can reference only ONE user
   - Each user can have only ONE store
   - Database constraint ensures uniqueness
4. **Lazy Loading**: `fetch = FetchType.LAZY` means store is loaded only when accessed, improving performance

---

## 8. Implementation of existBy() Method

### How Existence Checking Works:

**Files**: `UserRepository.java`, `LocationRepository.java`, `StoreRepository.java`, `ProductRepository.java`

```java
// UserRepository.java
boolean existsByEmail(String email);

// LocationRepository.java
boolean existsByStructureCode(String code);

// StoreRepository.java
boolean existsByStoreName(String storeName);

// ProductRepository.java
boolean existsByProductName(String productName);
```

### Logic Explanation:
1. **Spring Data JPA Query Derivation**: 
   - Method name follows pattern: `existsBy<FieldName>`
   - Spring automatically generates SQL query
2. **Generated SQL**: 
   ```sql
   SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END 
   FROM users u WHERE u.email = ?
   ```
3. **Return Type**: boolean (true if exists, false if not)
4. **Performance**: 
   - More efficient than `findBy()` methods
   - Only checks existence, doesn't load entire entity
   - Uses COUNT query instead of SELECT *
5. **Use Cases**:
   - Validation before creating new records
   - Checking for duplicates
   - Preventing constraint violations

### Example Usage:
```java
// In UserService
public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
}

// In Controller
@GetMapping("/exists/email/{email}")
public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
    return ResponseEntity.ok(userService.existsByEmail(email));
}
```

**Endpoint**: `GET /api/users/exists/email/test@example.com`
**Response**: `true` or `false`

---

## 9. Retrieve Users by Province Code or Province Name

### Implementation:

**File**: `UserRepository.java`

```java
// By Province Code
@Query("SELECT u FROM User u WHERE u.location.structureCode = :provinceCode AND u.location.structureType = 'PROVINCE'")
List<User> findUsersByProvinceCode(@Param("provinceCode") String provinceCode);

// By Province Name
@Query("SELECT u FROM User u WHERE u.location.structureName = :provinceName AND u.location.structureType = 'PROVINCE'")
List<User> findUsersByProvinceName(@Param("provinceName") String provinceName);
```

### Query Logic Explanation:

#### By Province Code:
1. **@Query Annotation**: Defines custom JPQL query
2. **JPQL Syntax**: 
   - `SELECT u FROM User u`: Select users
   - `WHERE u.location.structureCode = :provinceCode`: Filter by location code
   - `AND u.location.structureType = 'PROVINCE'`: Ensure it's a province level
3. **@Param**: Binds method parameter to query parameter
4. **Navigation**: `u.location.structureCode` navigates through the relationship

#### By Province Name:
- Same logic as province code, but filters by `structureName` instead

### Service Layer:

**File**: `UserService.java`

```java
public List<User> getUsersByProvinceCode(String provinceCode) {
    return userRepository.findUsersByProvinceCode(provinceCode);
}

public List<User> getUsersByProvinceName(String provinceName) {
    return userRepository.findUsersByProvinceName(provinceName);
}
```

### Controller Endpoints:

**File**: `UserController.java`

```java
@GetMapping("/province/code/{provinceCode}")
public ResponseEntity<List<User>> getUsersByProvinceCode(@PathVariable String provinceCode) {
    return ResponseEntity.ok(userService.getUsersByProvinceCode(provinceCode));
}

@GetMapping("/province/name/{provinceName}")
public ResponseEntity<List<User>> getUsersByProvinceName(@PathVariable String provinceName) {
    return ResponseEntity.ok(userService.getUsersByProvinceName(provinceName));
}
```

### Example Requests:
- By code: `GET /api/users/province/code/01` (Returns all users in province with code "01")
- By name: `GET /api/users/province/name/Kigali` (Returns all users in Kigali province)

### Generated SQL:
```sql
SELECT u.* FROM users u 
INNER JOIN administrative_structure l ON u.location_id = l.structure_id 
WHERE l.structure_code = '01' AND l.structure_type = 'PROVINCE';
```

---

## API Endpoints Summary

### Location Endpoints:
- `POST /api/locations` - Save location
- `GET /api/locations` - Get all locations
- `GET /api/locations/{id}` - Get location by ID
- `GET /api/locations/provinces` - Get all provinces
- `GET /api/locations/exists/{code}` - Check if location exists

### User Endpoints:
- `POST /api/users` - Save user
- `GET /api/users?page=0&size=10&sortBy=creationDate&direction=DESC` - Get users with pagination and sorting
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/exists/email/{email}` - Check if user exists
- `GET /api/users/province/code/{provinceCode}` - Get users by province code
- `GET /api/users/province/name/{provinceName}` - Get users by province name

### Store Endpoints:
- `POST /api/stores` - Save store
- `GET /api/stores?page=0&size=10&sortBy=creationDate&direction=DESC` - Get stores with pagination
- `GET /api/stores/{id}` - Get store by ID
- `GET /api/stores/exists/{storeName}` - Check if store exists

### Product Endpoints:
- `POST /api/products` - Save product
- `GET /api/products?page=0&size=10&sortBy=productPrice&direction=ASC` - Get products with pagination
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/exists/{productName}` - Check if product exists

### Review Endpoints:
- `POST /api/reviews` - Save review
- `GET /api/reviews?page=0&size=10&sortBy=creationDate&direction=DESC` - Get reviews with pagination
- `GET /api/reviews/{id}` - Get review by ID

---

## Database Configuration

**File**: `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tradebridge_db
    username: postgres
    password: P@ssw0rd2025
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

- Database: PostgreSQL
- Database Name: tradebridge_db
- Port: 5432
- Hibernate DDL: update (auto-creates/updates tables)
- Show SQL: true (logs SQL queries for debugging)

---

## Running the Application

1. Ensure PostgreSQL is running
2. Create database: `CREATE DATABASE tradebridge_db;`
3. Run: `mvn spring-boot:run`
4. Access Swagger UI: http://localhost:8081/swagger-ui.html
5. Test endpoints using Swagger or Postman

---

## Assessment Criteria Coverage

✅ **ERD with 5 tables**: User, Location, Store, Product, Review
✅ **Location saving**: LocationService.saveLocation()
✅ **Sorting**: Implemented using Sort and Pageable
✅ **Pagination**: Implemented using PageRequest and Page<T>
✅ **Many-to-Many**: Explained with example implementation
✅ **One-to-Many**: Store → Products, Product → Reviews, Location → Users
✅ **One-to-One**: User ↔ Store
✅ **existBy()**: Implemented in all repositories
✅ **Province queries**: findUsersByProvinceCode() and findUsersByProvinceName()
