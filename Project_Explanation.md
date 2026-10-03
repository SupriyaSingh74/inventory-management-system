# 📦 Inventory Management System — Complete Project Explanation

## 1. Project Introduction

This project is a backend REST API application built with **Java 21 and Spring Boot 3.5.5**. It manages the complete flow of inventory-related business operations:

- Categories
- Suppliers
- Products
- Customers
- Purchases
- Sales
- Inventory / Stock
- Stock History
- Dashboard Analytics

The project also includes:

- JWT Authentication
- Spring Security
- ADMIN / STAFF Role-Based Authorization
- Request Validation
- Global Exception Handling
- Pagination and Sorting
- Search APIs
- Swagger / OpenAPI
- SLF4J Logging
- JPA Auditing
- DTO-based API design
- Spring Data JPA / Hibernate
- MySQL

---

# 2. Business Problem

In a manual inventory process, product stock, suppliers, purchases, customers, and sales can become difficult to track.

This system provides a centralized backend through which authorized users can:

- Manage products
- Manage categories
- Manage suppliers
- Manage customers
- Record purchases
- Record sales
- Track stock
- Maintain stock movement history
- View dashboard statistics

The goal is to provide a secure, modular and maintainable backend application.

---

# 3. Main Objectives

- Provide REST APIs for inventory operations.
- Maintain product, category, supplier and customer data.
- Track purchase and sales transactions.
- Maintain stock quantity and minimum-stock information.
- Record stock-in, stock-out and stock-adjustment operations.
- Secure APIs using JWT.
- Apply ADMIN and STAFF authorization.
- Provide pagination, sorting and search.
- Provide centralized exception handling.
- Document APIs with Swagger.
- Add application logging.
- Track created and updated timestamps.

---

# 4. Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Backend programming |
| Spring Boot 3.5.5 | Application framework |
| Spring Web | REST APIs |
| Spring Security | Authentication and authorization |
| JJWT 0.12.6 | JWT generation and validation |
| Spring Data JPA | Data access |
| Hibernate | ORM |
| MySQL | Database |
| Maven | Build and dependency management |
| Lombok | Boilerplate reduction |
| ModelMapper 3.2.1 | DTO ↔ Entity mapping |
| Springdoc OpenAPI 2.8.9 | Swagger documentation |
| SLF4J | Logging |

---

# 5. Architecture

The project follows a layered architecture:

```text
Client / Postman / Swagger
          |
          v
   Security / JWT Filter
          |
          v
   Controller Layer
          |
          v
      DTO Layer
          |
          v
    Service Layer
          |
          v
  Repository Layer
          |
          v
     JPA / Hibernate
          |
          v
     MySQL Database
```

## Controller Layer

Handles HTTP requests and responses.

## DTO Layer

Defines API request and response structures.

## Service Layer

Contains business logic.

## Repository Layer

Communicates with the database through Spring Data JPA.

## Entity Layer

Maps Java objects to database tables.

## Security Layer

Handles JWT authentication and role-based authorization.

---

# 6. Project Package Structure

```text
src/main/java/com/inventory
│
├── config
│   ├── AppConfig.java
│   ├── AuditingConfig.java
│   └── OpenApiConfig.java
│
├── controller
│   ├── AuthController.java
│   ├── CategoryController.java
│   ├── CustomerController.java
│   ├── DashboardController.java
│   ├── ProductController.java
│   ├── PurchaseController.java
│   ├── SaleController.java
│   ├── StockHistoryController.java
│   └── SupplierController.java
│
├── dto
│   ├── auth
│   └── module DTOs
│
├── entity
│   ├── BaseEntity.java
│   ├── User.java
│   ├── Category.java
│   ├── Supplier.java
│   ├── Product.java
│   ├── Customer.java
│   ├── Purchase.java
│   ├── PurchaseItem.java
│   ├── Sale.java
│   ├── SaleItem.java
│   └── StockHistory.java
│
├── enums
├── exception
├── payload
├── repository
├── security
├── service
└── util
```

---

# 7. Functional Modules

## Authentication

Provides:

- User registration
- User login
- Password encryption
- JWT generation
- JWT validation

## Category

Provides:

- Create
- Read
- Update
- Delete

## Supplier

Provides:

- Create
- Read
- Update
- Delete
- Search

## Product

Provides:

- Create
- Read
- Update
- Delete
- SKU validation
- Category mapping
- Supplier mapping
- Pagination
- Sorting
- Search

## Customer

Provides:

- Create
- Read
- Update
- Delete
- Search

## Purchase

Provides:

- Purchase creation
- Purchase items
- Invoice lookup
- Pagination
- Inventory integration

## Sales

Provides:

- Sale creation
- Sale items
- Invoice lookup
- Pagination
- Inventory reduction

## Inventory / Stock

Provides:

- Stock In
- Stock Out
- Stock Adjustment
- Stock validation
- Stock History

## Dashboard

Provides:

- Total products
- Total categories
- Total suppliers
- Total customers
- Total purchases
- Total sales
- Low-stock count
- Out-of-stock count
- Today's purchase amount
- Today's sales amount
- Today's purchase count
- Today's sales count

---

# 8. Authentication and JWT Flow

## Registration

```text
POST /api/auth/register
        |
        v
Check Email
        |
        v
Encode Password using BCrypt
        |
        v
Save User
```

## Login

```text
POST /api/auth/login
        |
        v
AuthenticationManager
        |
        v
CustomUserDetailsService
        |
        v
UserRepository
        |
        v
BCrypt Password Verification
        |
        v
JwtService
        |
        v
JWT Token
```

The client then sends:

```http
Authorization: Bearer <JWT_TOKEN>
```

for protected APIs.

---

# 9. JWT Request Flow

```text
Client Request
      |
      v
Authorization Header
      |
      v
JwtAuthenticationFilter
      |
      v
Extract Bearer Token
      |
      v
JwtService Validation
      |
      v
Load UserDetails
      |
      v
Set Authentication
      |
      v
SecurityContext
      |
      v
Protected Controller
```

---

# 10. Role-Based Authorization

The project contains:

```text
ADMIN
STAFF
```

Method-level authorization is implemented using Spring Security.

Example:

```java
@PreAuthorize("hasRole('ADMIN')")
```

For both roles:

```java
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
```

Typical access:

| Module | ADMIN | STAFF |
|---|:---:|:---:|
| Category Create/Update/Delete | ✅ | ❌ |
| Category Read | ✅ | ✅ |
| Supplier Create/Update/Delete | ✅ | ❌ |
| Supplier Read | ✅ | ✅ |
| Product Create/Update/Delete | ✅ | ❌ |
| Product Read | ✅ | ✅ |
| Customer Create/Update/Delete | ✅ | ❌ |
| Customer Read | ✅ | ✅ |
| Purchase Create | ✅ | ❌ |
| Purchase Read | ✅ | ✅ |
| Sales | ✅ | ✅ |
| Dashboard | ✅ | ✅ |

The exact access rules are controlled by the annotations in the final controller code.

---

# 11. Product Module Flow

Product creation follows this process:

```text
Product Request
      |
      v
Validate SKU
      |
      v
Check Category
      |
      v
Check Supplier
      |
      v
Map DTO → Entity
      |
      v
Save Product
      |
      v
Map Entity → DTO
      |
      v
Return Response
```

Important validations:

- Duplicate SKU is rejected.
- Category must exist.
- Supplier must exist.

---

# 12. Product Pagination, Sorting and Search

Spring Data `Pageable` is used for pagination.

Example parameters:

```text
page = 0
size = 10
sortBy = productName
sortDir = asc
```

Search uses case-insensitive product-name matching.

This prevents the API from returning an unnecessarily large number of records at once.

---

# 13. Purchase Flow

Purchase creation follows:

```text
Purchase Request
      |
      v
Validate Supplier
      |
      v
Validate Products
      |
      v
Create Purchase Items
      |
      v
Calculate Total
      |
      v
Update Inventory
      |
      v
Save Purchase
      |
      v
Return Purchase Response
```

Purchase APIs support:

- Create purchase
- Get purchases
- Get purchase by ID
- Get purchase by invoice number
- Pagination

---

# 14. Sales Flow

Sales creation follows:

```text
Sale Request
      |
      v
Validate Customer
      |
      v
Validate Products
      |
      v
Calculate Item Totals
      |
      v
Calculate Sale Total
      |
      v
Decrease Inventory
      |
      v
Save Sale
      |
      v
Return Sale Response
```

Sales APIs support:

- Create sale
- Get sales
- Get sale by ID
- Get sale by invoice number
- Pagination

---

# 15. Inventory / Stock Flow

## Stock In

```text
Previous Stock
      +
Added Quantity
      =
Current Stock
```

A `STOCK_IN` history record is created.

## Stock Out

```text
Check Available Stock
      |
      v
Previous Stock
      -
Quantity
      =
Current Stock
```

If requested quantity is greater than available stock, the operation is rejected.

A `STOCK_OUT` history record is created.

## Stock Adjustment

```text
Current Stock
      |
      v
Requested Quantity
      |
      v
Replace Current Stock
```

An `ADJUSTMENT` history record is created.

---

# 16. Stock History

The Stock History module records inventory movements.

Important information includes:

- Product
- Movement type
- Quantity
- Previous stock
- Current stock
- Remarks
- Created time

Movement types include:

```text
STOCK_IN
STOCK_OUT
ADJUSTMENT
```

This provides an audit trail for stock changes.

---

# 17. Dashboard

The dashboard aggregates information from different repositories.

It provides:

```text
Total Products
Total Categories
Total Suppliers
Total Customers
Total Purchases
Total Sales
Low Stock Products
Out of Stock Products
Today's Purchase Amount
Today's Sales Amount
Today's Purchase Count
Today's Sales Count
```

The dashboard is useful for getting a quick overview of the business state.

---

# 18. DTO Pattern

DTO means **Data Transfer Object**.

Instead of exposing JPA entities directly through APIs, the project uses DTO classes.

Example:

```text
Client
  |
  v
ProductDto
  |
  v
ProductService
  |
  v
Product Entity
  |
  v
Database
```

Benefits:

- Prevents direct exposure of entity structure.
- Controls API data.
- Reduces coupling.
- Supports validation.
- Makes request/response design cleaner.

ModelMapper is used for DTO ↔ Entity conversion.

---

# 19. Exception Handling

The project uses centralized exception handling.

Important custom exceptions include:

```text
ResourceNotFoundException
DuplicateResourceException
```

Examples:

```text
Product not found
Category not found
Supplier not found
Duplicate SKU
Duplicate category
```

GlobalExceptionHandler converts exceptions into consistent API responses.

This avoids writing repetitive try/catch blocks inside every controller.

---

# 20. Validation

The project uses Jakarta Bean Validation.

Validation is applied to request DTOs.

Examples of validation concepts:

```java
@NotBlank
@Email
@Positive
@Min
```

Invalid input is handled centrally and returned as a validation error response.

---

# 21. Logging

SLF4J is used for application logging.

Example:

```java
logger.info("Creating Product : {}", dto.getProductName());

logger.info("Product Created Successfully : {}", saved.getProductId());

logger.warn("Deleting Product Id : {}", id);

logger.error("Product Not Found : {}", id);
```

Logging helps with:

- Debugging
- Monitoring
- Tracking business operations
- Identifying failures

Passwords and JWT tokens should never be logged.

---

# 22. JPA Auditing

`BaseEntity` provides common audit fields:

```text
createdAt
updatedAt
```

`AuditingConfig` enables Spring Data JPA auditing.

Typical annotations:

```java
@CreatedDate
private LocalDateTime createdAt;

@LastModifiedDate
private LocalDateTime updatedAt;
```

This helps track when records were created and modified.

---

# 23. Swagger / OpenAPI

Swagger provides interactive API documentation.

Open:

```text
http://localhost:8080/swagger-ui/index.html
```

or:

```text
http://localhost:8080/swagger-ui.html
```

JWT can be supplied using Swagger's **Authorize** button:

```text
Bearer <JWT_TOKEN>
```

After authorization, protected APIs can be tested directly from Swagger UI.

---

# 24. Database

Database:

```text
inventory_db
```

The application uses:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Hibernate updates the schema based on entity mappings.

For production, database migrations such as Flyway or Liquibase are preferable to relying on `ddl-auto=update`.

---

# 25. Important API Endpoints

## Authentication

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Register user |
| POST | `/api/auth/login` | Login |

## Category

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/categories` | Create |
| GET | `/api/categories` | Get all |
| GET | `/api/categories/{id}` | Get by ID |
| PUT | `/api/categories/{id}` | Update |
| DELETE | `/api/categories/{id}` | Delete |

## Product

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/products` | Create |
| GET | `/api/products` | Get products |
| GET | `/api/products/{id}` | Get by ID |
| PUT | `/api/products/{id}` | Update |
| DELETE | `/api/products/{id}` | Delete |
| GET | `/api/products/search` | Search |

## Supplier

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/suppliers` | Create |
| GET | `/api/suppliers` | Get all |
| GET | `/api/suppliers/{id}` | Get by ID |
| PUT | `/api/suppliers/{id}` | Update |
| DELETE | `/api/suppliers/{id}` | Delete |
| GET | `/api/suppliers/search` | Search |

## Customer

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/customers` | Create |
| GET | `/api/customers` | Get all |
| GET | `/api/customers/{id}` | Get by ID |
| PUT | `/api/customers/{id}` | Update |
| DELETE | `/api/customers/{id}` | Delete |
| GET | `/api/customers/search` | Search |

## Purchase

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/purchases` | Create purchase |
| GET | `/api/purchases` | List purchases |
| GET | `/api/purchases/{id}` | Get by ID |
| GET | `/api/purchases/invoice/{invoiceNo}` | Get by invoice |

## Sales

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/sales` | Create sale |
| GET | `/api/sales` | List sales |
| GET | `/api/sales/{id}` | Get by ID |
| GET | `/api/sales/invoice/{invoiceNo}` | Get by invoice |

## Stock

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/stocks/in` | Stock in |
| POST | `/api/stocks/out` | Stock out |
| POST | `/api/stocks/adjust` | Adjust stock |
| GET | `/api/stocks/history/{productId}` | Stock history |

## Dashboard

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/dashboard` | Dashboard analytics |

---

# 26. How to Run the Project

## Requirements

Install:

- Java 21
- MySQL
- Maven or Maven Wrapper
- IntelliJ IDEA / Eclipse / VS Code

## Step 1 — Create Database

```sql
CREATE DATABASE inventory_db;
```

## Step 2 — Configure Database

Open:

```text
src/main/resources/application.properties
```

Update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

Do not commit real credentials or JWT secrets to GitHub.

## Step 3 — Run

Windows:

```bat
mvnw.cmd spring-boot:run
```

Or run the Spring Boot main application class from the IDE.

## Step 4 — Swagger

```text
http://localhost:8080/swagger-ui/index.html
```

---

# 27. Recommended Testing Sequence

1. Register user.
2. Login.
3. Copy JWT.
4. Authorize Swagger/Postman.
5. Create category.
6. Create supplier.
7. Create product.
8. Test product pagination.
9. Test product sorting.
10. Test product search.
11. Create customer.
12. Test stock-in.
13. Test stock-out.
14. Test stock adjustment.
15. Create purchase.
16. Verify inventory update.
17. Create sale.
18. Verify stock reduction.
19. Check stock history.
20. Check dashboard.
21. Test ADMIN/STAFF permissions.
22. Test invalid token.
23. Test validation errors.
24. Test duplicate SKU/category.

---

# 28. Interview — 2 Minute Explanation

> I developed an Inventory Management System using Java 21 and Spring Boot 3.5.5. It is a RESTful backend application for managing products, categories, suppliers, customers, purchases, sales and stock history. I followed a layered architecture with Controller, DTO, Service, Repository and Entity layers. MySQL is used for persistence with Spring Data JPA and Hibernate. For security, I implemented Spring Security with JWT-based stateless authentication and ADMIN/STAFF role-based authorization. The project supports pagination, sorting, search, stock-in, stock-out, stock adjustment, purchase and sales workflows, and dashboard analytics. I also implemented global exception handling, validation, Swagger/OpenAPI documentation, SLF4J logging and JPA auditing. The main objective was to build a realistic, modular and secure backend application following clean architecture and REST API practices.

---

# 29. Common Interview Questions

### Q1. Why did you use Spring Boot?

Spring Boot reduces configuration and makes it easier to develop production-style REST applications.

### Q2. Why did you use JPA/Hibernate?

JPA provides a persistence abstraction and Hibernate handles ORM between Java entities and relational database tables.

### Q3. Why DTO?

DTOs separate API request/response models from database entities and control which data is exposed.

### Q4. Why JWT?

JWT provides stateless authentication. The client sends the signed token with protected requests.

### Q5. Why BCrypt?

BCrypt securely hashes passwords using an adaptive one-way hashing algorithm.

### Q6. How does JWT authentication work?

The user logs in, the server validates credentials, creates a JWT, and the client sends that JWT in the Authorization header for later requests.

### Q7. Why use `STATELESS` sessions?

Because the server does not need to maintain a login session. Authentication is represented by the JWT on each request.

### Q8. How do you implement role-based authorization?

Using Spring Security method security and annotations such as:

```java
@PreAuthorize("hasRole('ADMIN')")
```

### Q9. How do you prevent negative stock?

Before stock-out, the system checks whether the requested quantity is less than or equal to the available stock.

### Q10. What happens when a sale is created?

The sale is validated, totals are calculated, the sale is saved, and inventory is decreased for each sold product.

### Q11. What is Stock History?

It is an audit trail of stock movements such as stock-in, stock-out and adjustment.

### Q12. Why pagination?

To avoid loading a very large dataset into a single response and to improve API performance.

### Q13. Why GlobalExceptionHandler?

It centralizes error handling and provides consistent error responses.

### Q14. Why logging?

To monitor important operations and troubleshoot application failures.

### Q15. Why Swagger?

To document and interactively test REST APIs.

---

# 30. Production Improvements

Before using the application in a real production environment:

- Move DB credentials to environment variables.
- Move JWT secret to secure configuration.
- Use HTTPS.
- Never log passwords or JWT tokens.
- Use Flyway/Liquibase for database migrations.
- Add comprehensive unit and integration tests.
- Review CORS configuration.
- Consider refresh-token/revocation requirements.
- Add monitoring and alerting.
- Review audit-field mappings before production.

---

# 31. Resume-Ready Project Description

**Inventory Management System**

Developed a secure RESTful backend using Java 21 and Spring Boot 3.5.5 for managing products, categories, suppliers, customers, inventory, purchases, sales and stock history. Implemented JWT authentication, ADMIN/STAFF role-based authorization, Spring Data JPA/Hibernate with MySQL, pagination, sorting, search, stock movement tracking, dashboard analytics, global exception handling, Swagger/OpenAPI documentation, SLF4J logging and JPA auditing.

---

# 32. Final Project Flow

```text
Register / Login
       ↓
JWT Token
       ↓
JWT Authentication Filter
       ↓
Authentication / Authorization
       ↓
Controller
       ↓
DTO Validation
       ↓
Service Business Logic
       ↓
Repository
       ↓
Hibernate / JPA
       ↓
MySQL
       ↓
Response DTO
       ↓
Client
```

---

# 33. Final Project Status

- ✅ Authentication and JWT
- ✅ Spring Security
- ✅ ADMIN / STAFF authorization
- ✅ Category module
- ✅ Supplier module
- ✅ Product module
- ✅ Customer module
- ✅ Purchase module
- ✅ Sales module
- ✅ Inventory / Stock module
- ✅ Stock History
- ✅ Dashboard
- ✅ Global Exception Handling
- ✅ Validation
- ✅ Pagination
- ✅ Sorting
- ✅ Search
- ✅ Swagger/OpenAPI
- ✅ SLF4J Logging
- ✅ JPA Auditing
- ✅ DTO architecture

---

# 34. Developer

**Supriya Singh**

Java Backend Developer  
Java • Spring Boot • REST API • Spring Security • JWT • MySQL
