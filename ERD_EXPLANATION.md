# TradeBridge 26668 - Entity Relationship Diagram (ERD)

## Database Schema Overview

### 5 Tables (Entities)

1. **users** - User information
2. **administrative_structure** - Location hierarchy (provinces, districts, etc.)
3. **stores** - Store/business information
4. **products** - Product catalog
5. **reviews** - Product reviews by users

---

## Visual Representation

```
┌─────────────────────────────┐
│  administrative_structure   │
│  (Location)                 │
├─────────────────────────────┤
│ PK: structure_id (UUID)     │
│     structure_code          │
│     structure_name          │
│     structure_type (ENUM)   │
│ FK: parent_id               │
└──────────┬──────────────────┘
           │
           │ One-to-Many
           │ (One location has many users)
           │
           ▼
┌─────────────────────────────┐
│         users               │
├─────────────────────────────┤
│ PK: user_id (UUID)          │
│     username                │
│     first_name              │
│     last_name               │
│     email                   │
│     phone_number            │
│     company_name            │
│     user_type (ENUM)        │
│     creation_date           │
│ FK: location_id             │
└──────────┬──────────────────┘
           │
           │ One-to-One
           │ (One user has one store)
           │
           ▼
┌─────────────────────────────┐
│         stores              │
├─────────────────────────────┤
│ PK: store_id (UUID)         │
│     store_name              │
│     phone_number            │
│     store_email             │
│     creation_date           │
│ FK: user_id (UNIQUE)        │
└──────────┬──────────────────┘
           │
           │ One-to-Many
           │ (One store has many products)
           │
           ▼
┌─────────────────────────────┐
│        products             │
├─────────────────────────────┤
│ PK: product_id (UUID)       │
│     product_name            │
│     product_description     │
│     product_price           │
│     quantity                │
│     creation_date           │
│ FK: store_id                │
└──────────┬──────────────────┘
           │
           │ One-to-Many
           │ (One product has many reviews)
           │
           ▼
┌─────────────────────────────┐
│         reviews             │
├─────────────────────────────┤
│ PK: review_id (UUID)        │
│     message                 │
│     creation_date           │
│ FK: product_id              │
│ FK: user_id                 │
└─────────────────────────────┘
           ▲
           │
           │ Many-to-One
           │ (Many reviews by one user)
           │
           │
    (connects back to users)
```

---

## Relationship Details

### 1. Location → Users (One-to-Many)
- **Type**: One-to-Many
- **Description**: One location can have multiple users
- **Foreign Key**: `location_id` in `users` table
- **Cascade**: ALL (when location deleted, users are deleted)
- **Example**: Kigali province has 100 users

### 2. User ↔ Store (One-to-One)
- **Type**: One-to-One
- **Description**: Each user can own exactly one store
- **Foreign Key**: `user_id` in `stores` table (UNIQUE constraint)
- **Cascade**: ALL (when user deleted, store is deleted)
- **Example**: User "John Doe" owns "Tech Store"

### 3. Store → Products (One-to-Many)
- **Type**: One-to-Many
- **Description**: One store can have multiple products
- **Foreign Key**: `store_id` in `products` table
- **Cascade**: ALL (when store deleted, products are deleted)
- **Example**: "Tech Store" has 50 products

### 4. Product → Reviews (One-to-Many)
- **Type**: One-to-Many
- **Description**: One product can have multiple reviews
- **Foreign Key**: `product_id` in `reviews` table
- **Cascade**: ALL (when product deleted, reviews are deleted)
- **Example**: "Laptop X" has 20 reviews

### 5. User → Reviews (One-to-Many / Many-to-One)
- **Type**: One-to-Many (from User perspective), Many-to-One (from Review perspective)
- **Description**: One user can write multiple reviews
- **Foreign Key**: `user_id` in `reviews` table
- **No Cascade**: Reviews remain if user is deleted (can be changed)
- **Example**: User "Jane Smith" wrote 10 reviews

---

## Many-to-Many Pattern (Implicit)

While not explicitly defined as @ManyToMany, the Review entity creates an implicit many-to-many relationship:

```
Users ←→ Reviews ←→ Products
```

- Many users can review many products
- Many products can be reviewed by many users
- Review acts as a join table with additional attributes (message, creation_date)

**If we wanted explicit Many-to-Many** (e.g., User favorites):
```java
@ManyToMany
@JoinTable(
    name = "user_favorite_stores",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "store_id")
)
private List<Store> favoriteStores;
```

---

## Field Types

### Primary Keys
- All entities use `UUID` as primary key
- Generated automatically with `@GeneratedValue(strategy = GenerationType.UUID)`

### Enums
- **UserEnum**: INDUSTRY_WORKER, SUPPLIER, CLIENT
- **LocationEnum**: PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE

### Timestamps
- All entities have `creation_date` field
- Automatically populated with `@CreationTimestamp`

---

## Database Constraints

### Foreign Key Constraints
```sql
-- users table
ALTER TABLE users ADD CONSTRAINT fk_user_location 
    FOREIGN KEY (location_id) REFERENCES administrative_structure(structure_id);

-- stores table
ALTER TABLE stores ADD CONSTRAINT fk_store_user 
    FOREIGN KEY (user_id) REFERENCES users(user_id);
ALTER TABLE stores ADD CONSTRAINT uk_store_user UNIQUE (user_id);

-- products table
ALTER TABLE products ADD CONSTRAINT fk_product_store 
    FOREIGN KEY (store_id) REFERENCES stores(store_id);

-- reviews table
ALTER TABLE reviews ADD CONSTRAINT fk_review_product 
    FOREIGN KEY (product_id) REFERENCES products(product_id);
ALTER TABLE reviews ADD CONSTRAINT fk_review_user 
    FOREIGN KEY (user_id) REFERENCES users(user_id);
```

### Unique Constraints
- `stores.user_id` - Ensures one-to-one relationship
- `administrative_structure.structure_code` - Ensures unique location codes

---

## Sample Data Flow

### Creating a Complete Record Chain

1. **Create Location (Province)**
```json
POST /api/locations
{
  "structureCode": "01",
  "structureName": "Kigali",
  "structureType": "PROVINCE"
}
```

2. **Create User in that Location**
```json
POST /api/users
{
  "username": "johndoe",
  "firstName": "John",
  "email": "john@example.com",
  "userType": "SUPPLIER",
  "location": {
    "structureId": "location-uuid-from-step-1"
  }
}
```

3. **Create Store for that User**
```json
POST /api/stores
{
  "storeName": "Tech Store",
  "storeEmail": "store@example.com",
  "user": {
    "userId": "user-uuid-from-step-2"
  }
}
```

4. **Create Product in that Store**
```json
POST /api/products
{
  "productName": "Laptop",
  "productPrice": 999.99,
  "store": {
    "storeId": "store-uuid-from-step-3"
  }
}
```

5. **Create Review for that Product**
```json
POST /api/reviews
{
  "message": "Great product!",
  "product": {
    "productId": "product-uuid-from-step-4"
  },
  "user": {
    "userId": "user-uuid-from-step-2"
  }
}
```

---

## Query Examples

### Get all users in Kigali province
```sql
SELECT u.* FROM users u
INNER JOIN administrative_structure l ON u.location_id = l.structure_id
WHERE l.structure_name = 'Kigali' AND l.structure_type = 'PROVINCE';
```

### Get all products from a specific store
```sql
SELECT p.* FROM products p
WHERE p.store_id = 'store-uuid';
```

### Get all reviews for a product
```sql
SELECT r.*, u.username FROM reviews r
INNER JOIN users u ON r.user_id = u.user_id
WHERE r.product_id = 'product-uuid';
```

### Get store owned by a user
```sql
SELECT s.* FROM stores s
WHERE s.user_id = 'user-uuid';
```

---

## Indexing Recommendations

For better query performance:

```sql
-- Index on foreign keys
CREATE INDEX idx_users_location ON users(location_id);
CREATE INDEX idx_stores_user ON stores(user_id);
CREATE INDEX idx_products_store ON products(store_id);
CREATE INDEX idx_reviews_product ON reviews(product_id);
CREATE INDEX idx_reviews_user ON reviews(user_id);

-- Index on frequently queried fields
CREATE INDEX idx_location_code ON administrative_structure(structure_code);
CREATE INDEX idx_location_type ON administrative_structure(structure_type);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_products_price ON products(product_price);
```

---

## Relationship Summary Table

| Relationship | Type | Owner | Foreign Key | Cascade |
|--------------|------|-------|-------------|---------|
| Location → Users | One-to-Many | User | location_id | ALL |
| User ↔ Store | One-to-One | Store | user_id | ALL |
| Store → Products | One-to-Many | Product | store_id | ALL |
| Product → Reviews | One-to-Many | Review | product_id | ALL |
| User → Reviews | One-to-Many | Review | user_id | None |

---

## Assessment Alignment

This ERD demonstrates:
- ✅ **5 tables** as required
- ✅ **One-to-One** relationship (User-Store)
- ✅ **One-to-Many** relationships (Location-Users, Store-Products, Product-Reviews)
- ✅ **Many-to-One** relationships (Reviews-User, Reviews-Product)
- ✅ **Implicit Many-to-Many** through Review join entity
- ✅ **Foreign key usage** in all relationships
- ✅ **Proper cascade operations**
- ✅ **Clear entity connections**
