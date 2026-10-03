Inventory Management System

A backend REST API for managing products, categories, suppliers, customers, purchases, sales, and inventory operations.

The application is developed using Java 21 and Spring Boot 3.5.5. It includes JWT authentication, role-based authorization, inventory tracking, purchase and sales management, validation, exception handling, pagination, search, and Swagger/OpenAPI documentation.

Tech Stack

Java 21

Spring Boot 3.5.5

Spring Web

Spring Security

JWT (JJWT 0.12.6)

Spring Data JPA

Hibernate

MySQL

Maven

Lombok

ModelMapper 3.2.1

Springdoc OpenAPI 2.8.9

SLF4J

Features

User registration and login

JWT-based authentication

Role-based authorization with ADMIN and STAFF

Product management

Category management

Supplier management

Customer management

Stock management

Stock movement history

Purchase management

Sales management

Dashboard statistics

Product, supplier, and customer search

Pagination and sorting

Request validation

Global exception handling

JPA auditing

Swagger/OpenAPI documentation

Application logging

Architecture

The project follows a layered architecture to keep the API, business logic, and database operations separated.

Client / Swagger / Postman
|
v
Controller
|
v
Service
|
v
Repository
|
v
MySQL

Project Layers

Controller - Handles HTTP requests and API responses.

Service - Contains business logic and application workflows.

Repository - Handles database operations using Spring Data JPA.

Entity - Represents database tables.

DTO - Defines request and response objects.

Security - Handles JWT authentication and authorization.

Exception - Contains custom exceptions and global exception handling.

Config - Contains application, auditing, and OpenAPI configuration.

Project Structure
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

Authentication

Authentication is implemented using Spring Security and JWT.

Register
POST /api/auth/register


Example request:

{
"fullName": "Admin User",
"email": "admin@example.com",
"password": "admin123",
"role": "ADMIN"
}

Login
POST /api/auth/login


Example request:

{
"email": "admin@example.com",
"password": "admin123"
}


After successful authentication, the API returns a JWT token.

For protected endpoints, send the token in the request header:

Authorization: Bearer <JWT_TOKEN>

Authentication Flow
Login Request
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
Password Verification
|
v
JwtService
|
v
JWT Token


For subsequent requests, JwtAuthenticationFilter validates the token and sets the authenticated user in the Spring Security context.

Authorization

The application supports two roles:

ADMIN

STAFF

Example of an ADMIN-only endpoint:

@PreAuthorize("hasRole('ADMIN')")


Endpoints available to both roles can use:

@PreAuthorize("hasAnyRole('ADMIN','STAFF')")


Authorization is applied at the controller method level based on the operation.

API Endpoints
Authentication
Method	Endpoint	Description
POST	/api/auth/register	Register user
POST	/api/auth/login	Login and receive JWT
Categories
Method	Endpoint	Description
POST	/api/categories	Create category
GET	/api/categories	Get all categories
GET	/api/categories/{id}	Get category
PUT	/api/categories/{id}	Update category
DELETE	/api/categories/{id}	Delete category
Products
Method	Endpoint	Description
POST	/api/products	Create product
GET	/api/products	Get products with pagination and sorting
GET	/api/products/{id}	Get product by ID
PUT	/api/products/{id}	Update product
DELETE	/api/products/{id}	Delete product
GET	/api/products/search	Search products
Suppliers
Method	Endpoint	Description
POST	/api/suppliers	Create supplier
GET	/api/suppliers	Get suppliers
GET	/api/suppliers/{id}	Get supplier by ID
PUT	/api/suppliers/{id}	Update supplier
DELETE	/api/suppliers/{id}	Delete supplier
GET	/api/suppliers/search	Search suppliers
Customers
Method	Endpoint	Description
POST	/api/customers	Create customer
GET	/api/customers	Get customers
GET	/api/customers/{id}	Get customer by ID
PUT	/api/customers/{id}	Update customer
DELETE	/api/customers/{id}	Delete customer
GET	/api/customers/search	Search customers
Purchases
Method	Endpoint	Description
POST	/api/purchases	Create purchase
GET	/api/purchases	Get purchases with pagination
GET	/api/purchases/{id}	Get purchase by ID
GET	/api/purchases/invoice/{invoiceNo}	Get purchase by invoice number
Sales
Method	Endpoint	Description
POST	/api/sales	Create sale
GET	/api/sales	Get sales with pagination
GET	/api/sales/{id}	Get sale by ID
GET	/api/sales/invoice/{invoiceNo}	Get sale by invoice number
Stock
Method	Endpoint	Description
POST	/api/stocks/in	Add stock
POST	/api/stocks/out	Remove stock
POST	/api/stocks/adjust	Adjust stock quantity
GET	/api/stocks/history/{productId}	Get product stock history
Dashboard
Method	Endpoint	Description
GET	/api/dashboard	Get dashboard statistics
Inventory Management

The application keeps track of the current stock quantity for each product.

Stock In

When stock is added:

Previous Quantity
+
Added Quantity
|
v
Updated Product Quantity
|
v
Stock History

Stock Out

Before removing stock, the available quantity is checked.

Available Quantity
|
v
Validate Requested Quantity
|
v
Reduce Product Quantity
|
v
Save Stock History


If the requested quantity is greater than the available stock, the operation is rejected.

Stock Adjustment

Stock adjustment replaces the current quantity with the requested quantity and records the change in stock history.

Purchase Flow

A purchase follows this process:

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

Sales Flow

A sale validates the customer and products before calculating the total and updating inventory.

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
Reduce Inventory
|
v
Save Sale

Dashboard

The dashboard provides information such as:

Total products

Total categories

Total suppliers

Total customers

Total purchases

Total sales

Low-stock products

Out-of-stock products

Today's purchase amount

Today's sales amount

Today's purchase count

Today's sales count

Exception Handling

The application uses centralized exception handling for common API errors.

Custom exceptions include:

ResourceNotFoundException

DuplicateResourceException

The global exception handler also handles validation errors, authentication errors, invalid JWTs, and other application-level errors.

This keeps the controllers focused on request handling while providing consistent error responses.

Logging

SLF4J is used for application logging.

Examples:

logger.info("Creating Product : {}", dto.getProductName());

logger.warn("Deleting Product Id : {}", id);

logger.error("Product Not Found : {}", id);


Logging is used for important operations such as creating, updating, deleting, searching, and stock-related operations.

Sensitive information such as passwords and JWT tokens should not be logged.

JPA Auditing

The project uses Spring Data JPA auditing for common entity audit fields.

createdAt
updatedAt


These fields are managed through BaseEntity and the application's auditing configuration.

Swagger / OpenAPI

API documentation is available through Swagger UI.

After starting the application, open:

http://localhost:8080/swagger-ui/index.html


You can use the Authorize button to provide:

Bearer <JWT_TOKEN>


Protected endpoints can then be tested directly from Swagger UI.

Database Configuration

The application uses MySQL.

Create the database:

CREATE DATABASE inventory_db;


Update the database configuration in:

src/main/resources/application.properties


Example:

spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD


Hibernate is currently configured to update the database schema:

spring.jpa.hibernate.ddl-auto=update


Do not commit actual database credentials or JWT secrets to the repository.

Running the Application
Prerequisites

Java 21

MySQL

Maven or Maven Wrapper

IntelliJ IDEA, Eclipse, or VS Code

Start with Maven Wrapper

Linux/macOS:

./mvnw spring-boot:run


Windows:

mvnw.cmd spring-boot:run


The application can also be started by running:

InventoryManagementSystemApplication.java


Once the application is running, Swagger UI is available at:

http://localhost:8080/swagger-ui/index.html

Testing Flow

A typical API testing flow is:

Register a user.

Login and get the JWT token.

Authorize Swagger or Postman using the token.

Create a category.

Create a supplier.

Create a product.

Test product search, pagination, and sorting.

Test stock in, stock out, and stock adjustment.

Create a customer.

Create a purchase.

Create a sale.

Check stock history.

Check dashboard data.

Test ADMIN and STAFF authorization.

Test validation and invalid JWT scenarios.

Resume Summary

Developed a RESTful backend using Java 21 and Spring Boot.

Implemented JWT authentication with Spring Security.

Added role-based authorization for ADMIN and STAFF users.

Implemented CRUD operations for inventory-related modules.

Developed stock-in, stock-out, stock adjustment, and stock history functionality.

Integrated purchase and sales workflows with inventory updates.

Added pagination, sorting, and search functionality.

Implemented DTO-based request and response handling.

Added global exception handling and request validation.

Integrated Swagger/OpenAPI for API documentation.

Added application logging and JPA auditing.

Security

Before pushing the project to GitHub:

Do not commit MySQL passwords.

Do not commit production JWT secrets.

Use environment variables for sensitive configuration.

Do not log passwords or JWT tokens.

Do not commit files containing credentials or secrets.

Use HTTPS when deploying the application in production.

Developer

Supriya Singh

Java Backend Developer
Spring Boot | REST API | MySQL | Spring Security | JWT