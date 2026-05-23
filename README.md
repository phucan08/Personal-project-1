# Auto Parts E-commerce Management System

## 1. Project Overview

Auto Parts E-commerce Management System is a web-based application for managing and selling automotive parts. The system allows customers to browse products, search for products, add products to cart, place orders, view order history, and review purchased products.

The system also provides an admin dashboard where administrators can manage products, categories, orders, and users.

This project was built as a personal project to practice Java Spring Boot, MySQL, MVC architecture, authentication, authorization, shopping cart management, order processing, product review, and basic password security using BCrypt.

---

## 2. Purpose of the Project

The main purpose of this project is to simulate a small e-commerce system for automotive parts.

This project helps demonstrate:

- How to build a Java Spring Boot web application
- How to design and connect entities using JPA/Hibernate
- How to implement CRUD operations
- How to manage user login/register
- How to separate user and admin roles
- How to build a shopping cart and order checkout system
- How to handle errors with friendly messages
- How to hash passwords using BCrypt
- How to organize a project using layered architecture

---

## 3. Main Features

### 3.1 Guest Features

Guests can:

- View product list
- View product details
- Search products by product name or product code
- Register a new account
- Login to the system

---

### 3.2 User Features

Users can:

- Register account
- Login
- Logout
- View products
- View product details
- Search products
- Add products to cart
- View cart
- Update product quantity in cart
- Remove product from cart
- Checkout
- View order history
- View order details
- Review purchased products
- Rate products from 1 to 5 stars

---

### 3.3 Admin Features

Admins can:

- Login
- Logout
- View admin dashboard
- Manage products
- Add new products
- Edit products
- Delete products
- Manage categories
- Add categories
- Delete categories
- View all orders
- Update order status
- View registered users

---

## 4. Technologies Used

### Backend

- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- Maven

### Frontend

- Thymeleaf
- HTML
- CSS

### Database

- MySQL

### Security

- Session-based authentication
- Role-based authorization
- BCrypt password hashing
- Custom AuthFilter
- Custom AdminFilter

---

## 5. Project Architecture

This project follows a layered architecture:

```text
Controller Layer
↓
Service Layer
↓
Repository Layer
↓
Database
```

### Controller Layer

The controller layer handles HTTP requests from users.

Examples:

```text
AuthController.java
HomeController.java
ProductController.java
CategoryController.java
CartController.java
OrderController.java
ReviewController.java
AdminController.java
ErrorPageController.java
```

### Service Layer

The service layer contains business logic.

Examples:

```text
UserService.java
ProductService.java
CategoryService.java
CartService.java
OrderService.java
ReviewService.java
```

### Repository Layer

The repository layer communicates directly with the database using Spring Data JPA.

Examples:

```text
UserRepository.java
ProductRepository.java
CategoryRepository.java
CartRepository.java
CartItemRepository.java
OrderRepository.java
OrderItemRepository.java
PaymentRepository.java
ReviewRepository.java
```

### Entity Layer

The entity layer represents database tables.

Examples:

```text
User.java
Product.java
Category.java
Cart.java
CartItem.java
Order.java
OrderItem.java
Payment.java
Review.java
```

---

## 6. Project Structure

```text
auto-parts-ecommerce/
│
├── pom.xml
├── README.md
├── .gitignore
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── autoparts/
│   │   │               │
│   │   │               ├── AutoPartsApplication.java
│   │   │               │
│   │   │               ├── controller/
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── HomeController.java
│   │   │               │   ├── ProductController.java
│   │   │               │   ├── CategoryController.java
│   │   │               │   ├── CartController.java
│   │   │               │   ├── OrderController.java
│   │   │               │   ├── ReviewController.java
│   │   │               │   ├── AdminController.java
│   │   │               │   └── ErrorPageController.java
│   │   │               │
│   │   │               ├── entity/
│   │   │               │   ├── User.java
│   │   │               │   ├── Product.java
│   │   │               │   ├── Category.java
│   │   │               │   ├── Cart.java
│   │   │               │   ├── CartItem.java
│   │   │               │   ├── Order.java
│   │   │               │   ├── OrderItem.java
│   │   │               │   ├── Payment.java
│   │   │               │   └── Review.java
│   │   │               │
│   │   │               ├── repository/
│   │   │               │   ├── UserRepository.java
│   │   │               │   ├── ProductRepository.java
│   │   │               │   ├── CategoryRepository.java
│   │   │               │   ├── CartRepository.java
│   │   │               │   ├── CartItemRepository.java
│   │   │               │   ├── OrderRepository.java
│   │   │               │   ├── OrderItemRepository.java
│   │   │               │   ├── PaymentRepository.java
│   │   │               │   └── ReviewRepository.java
│   │   │               │
│   │   │               ├── service/
│   │   │               │   ├── UserService.java
│   │   │               │   ├── ProductService.java
│   │   │               │   ├── CategoryService.java
│   │   │               │   ├── CartService.java
│   │   │               │   ├── OrderService.java
│   │   │               │   └── ReviewService.java
│   │   │               │
│   │   │               ├── dto/
│   │   │               │   ├── LoginRequest.java
│   │   │               │   └── RegisterRequest.java
│   │   │               │
│   │   │               ├── config/
│   │   │               │   ├── FilterConfig.java
│   │   │               │   └── PasswordConfig.java
│   │   │               │
│   │   │               ├── filter/
│   │   │               │   ├── AuthFilter.java
│   │   │               │   └── AdminFilter.java
│   │   │               │
│   │   │               └── exception/
│   │   │                   └── GlobalExceptionHandler.java
│   │   │
│   │   └── resources/
│   │       │
│   │       ├── application.properties
│   │       │
│   │       ├── static/
│   │       │   └── css/
│   │       │       ├── style.css
│   │       │       └── login.css
│   │       │
│   │       └── templates/
│   │           │
│   │           ├── auth/
│   │           │   ├── login.html
│   │           │   └── register.html
│   │           │
│   │           ├── product/
│   │           │   ├── product-list.html
│   │           │   ├── product-detail.html
│   │           │   └── product-form.html
│   │           │
│   │           ├── category/
│   │           │   ├── category-list.html
│   │           │   └── category-form.html
│   │           │
│   │           ├── cart/
│   │           │   └── cart.html
│   │           │
│   │           ├── order/
│   │           │   ├── checkout.html
│   │           │   ├── order-history.html
│   │           │   └── order-detail.html
│   │           │
│   │           ├── review/
│   │           │   └── review-form.html
│   │           │
│   │           ├── admin/
│   │           │   ├── dashboard.html
│   │           │   └── manage-users.html
│   │           │
│   │           └── error/
│   │               ├── 403.html
│   │               ├── 404.html
│   │               └── 500.html
│   │
│   └── test/
│       └── java/
│           └── com/example/autoparts/
│               └── AutoPartsApplicationTests.java
```

---

## 7. Database Design Overview

The system contains the following main entities:

| Entity | Description |
|---|---|
| User | Stores user account information |
| Product | Stores automotive part information |
| Category | Stores product categories |
| Cart | Stores a user's shopping cart |
| CartItem | Stores products inside a cart |
| Order | Stores customer order information |
| OrderItem | Stores products inside an order |
| Payment | Stores payment information |
| Review | Stores customer reviews and ratings |

---

## 8. Main Relationships

```text
User 1 - 1 Cart
Cart 1 - N CartItem
Product 1 - N CartItem

Category 1 - N Product

User 1 - N Order
Order 1 - N OrderItem
Product 1 - N OrderItem

Order 1 - 1 Payment

User 1 - N Review
Product 1 - N Review
```

Explanation:

- One user has one cart.
- One cart can contain many cart items.
- One product can appear in many cart items.
- One category can contain many products.
- One user can place many orders.
- One order can contain many order items.
- One product can appear in many order items.
- One order has one payment.
- One user can write many reviews.
- One product can have many reviews.

---

## 9. Important Business Rules

### Authentication Rules

- Guests can view products.
- Guests must login before using cart, checkout, orders, and reviews.
- Users can access customer features.
- Admins can access admin features.
- Normal users cannot access admin pages.

### Product Rules

- Product code must be unique.
- Product code cannot be empty.
- Product name cannot be empty.
- Price must be greater than or equal to 0.
- Quantity must be greater than or equal to 0.
- Product availability is based on quantity.
- If quantity is 0, the product is out of stock.

### Cart Rules

- Only logged-in users can access cart.
- Users can add available products to cart.
- Users cannot add more quantity than available stock.
- Users can update item quantity.
- Users can remove items from cart.

### Order Rules

- User can checkout only if cart is not empty.
- Shipping address is required.
- Payment method is required.
- Order is created from cart items.
- Product stock is reduced after checkout.
- Cart is cleared after checkout.
- User can view only their own orders.
- Admin can view all orders.
- Admin can update order status.

### Review Rules

- User can review only purchased products.
- Rating must be from 1 to 5.
- Comment cannot be empty.
- One user can review one product only once.
- Reviews are displayed on the product detail page.

---

## 10. Security Features

This project includes basic security features:

- Passwords are hashed using BCrypt.
- Passwords are not stored as plain text.
- Session is used to track logged-in users.
- AuthFilter protects pages that require login.
- AdminFilter protects admin-only pages.
- Friendly error pages are used.
- Detailed SQL/database errors are not displayed to users.
- Session tracking is configured to use cookies instead of showing `jsessionid` in the URL.

---

## 11. How to Run the Project

### Step 1: Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/auto-parts-ecommerce.git
```

Replace `YOUR_USERNAME` with your GitHub username.

### Step 2: Open the project

Open the project using one of these IDEs:

- VS Code
- IntelliJ IDEA
- Eclipse

### Step 3: Create MySQL database

Open MySQL and run:

```sql
CREATE DATABASE auto_parts_ecommerce;
```

### Step 4: Configure database connection

Open this file:

```text
src/main/resources/application.properties
```

Update your MySQL username and password:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/auto_parts_ecommerce
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

If your MySQL root account has no password, use:

```properties
spring.datasource.password=
```

### Step 5: Run the project

Run this file:

```text
src/main/java/com/example/autoparts/AutoPartsApplication.java
```

Then open:

```text
http://localhost:8080/
```

The system will redirect to the login page.

---

## 12. Example application.properties

```properties
spring.application.name=auto-parts-ecommerce

# Database connection
spring.datasource.url=jdbc:mysql://localhost:3306/auto_parts_ecommerce
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Thymeleaf
spring.thymeleaf.cache=false

# Server
server.port=8080

# Error page
server.error.whitelabel.enabled=false
server.error.path=/error

# Use cookie for session
server.servlet.session.tracking-modes=cookie
```

---

## 13. Default Admin Account

When the application starts, it automatically creates a default admin account if it does not already exist.

```text
Email: admin@gmail.com
Password: admin123
```

After successful login, admin will be redirected to:

```text
/admin/dashboard
```

---

## 14. User Flow

### Customer Flow

```text
Register
→ Login
→ View products
→ View product detail
→ Add product to cart
→ View cart
→ Update quantity
→ Checkout
→ View order history
→ View order detail
→ Review purchased product
```

### Admin Flow

```text
Login
→ Admin dashboard
→ Manage products
→ Manage categories
→ Manage orders
→ Update order status
→ View users
```

---

## 15. Important URLs

| URL | Description |
|---|---|
| `/` | Redirect to login/dashboard/products depending on session |
| `/login` | Login page |
| `/register` | Register page |
| `/products` | Product list |
| `/products/{id}` | Product detail |
| `/products/add` | Add product, admin only |
| `/products/edit/{id}` | Edit product, admin only |
| `/products/delete/{id}` | Delete product, admin only |
| `/categories` | Manage categories, admin only |
| `/cart` | User cart |
| `/orders` | User order history or admin order management |
| `/orders/checkout` | Checkout page |
| `/reviews/add/{productId}` | Add product review |
| `/admin/dashboard` | Admin dashboard |
| `/admin/manage-users` | Manage users |
| `/error/403` | Access denied page |

---

## 16. Testing Checklist

### Authentication

- [ ] Register new user
- [ ] Login as user
- [ ] Login as admin
- [ ] Logout
- [ ] Password is stored as BCrypt hash
- [ ] User cannot access admin dashboard
- [ ] Guest cannot access cart
- [ ] Guest cannot access orders
- [ ] Guest cannot access reviews

### Product and Category

- [ ] Admin can add product
- [ ] Admin can edit product
- [ ] Admin can delete product
- [ ] Admin can add category
- [ ] Admin can delete category
- [ ] Product can be assigned to category
- [ ] Product search works
- [ ] Product detail page works
- [ ] Product availability changes based on quantity

### Cart

- [ ] User can add product to cart
- [ ] User can view cart
- [ ] User can update cart item quantity
- [ ] User cannot exceed available stock
- [ ] User can remove item from cart
- [ ] Cart total is calculated correctly

### Order

- [ ] User can checkout
- [ ] Order is created after checkout
- [ ] Order items are saved
- [ ] Payment record is created
- [ ] Product stock is reduced after checkout
- [ ] Cart is cleared after checkout
- [ ] User can view order history
- [ ] User can view order detail
- [ ] Admin can view all orders
- [ ] Admin can update order status

### Review

- [ ] User can review purchased product
- [ ] User cannot review product that they have not purchased
- [ ] User cannot review the same product twice
- [ ] Rating must be from 1 to 5
- [ ] Comment cannot be empty
- [ ] Review appears on product detail page

### Error Handling

- [ ] Normal user accessing admin page shows 403 page
- [ ] Invalid URL shows 404 page
- [ ] Server error shows 500 page
- [ ] SQL/database error details are not shown to user
- [ ] Whitelabel Error Page is disabled

---

## 17. GitHub Upload Checklist

Before pushing this project to GitHub:

- [ ] `README.md` is added
- [ ] `.gitignore` is added
- [ ] `target/` folder is not uploaded
- [ ] `.env` file is not uploaded
- [ ] Local database files are not uploaded
- [ ] Real passwords are not uploaded
- [ ] Project can run from a fresh clone
- [ ] Database setup instruction is written clearly
- [ ] Demo admin account is included
- [ ] Screenshots can be added later if needed

---

## 18. Git Commands

Initialize Git:

```bash
git init
```

Add files:

```bash
git add .
```

Commit:

```bash
git commit -m "Initial commit - Auto Parts E-commerce Management System"
```

Set main branch:

```bash
git branch -M main
```

Add remote repository:

```bash
git remote add origin https://github.com/YOUR_USERNAME/auto-parts-ecommerce.git
```

Push to GitHub:

```bash
git push -u origin main
```

---

## 19. Future Improvements

The following features can be added in the future:

- Product image upload
- Product filter by category
- Product pagination
- Product sorting by price
- User profile page
- Change password feature
- Order cancellation by user
- Email confirmation after checkout
- Admin sales statistics
- Unit testing for service layer
- Better UI design using Bootstrap or Tailwind CSS
- Deployment to an online server

---

## 20. What I Learned

Through this project, I practiced:

- Building a Spring Boot MVC application
- Designing relational database entities
- Using Spring Data JPA
- Creating CRUD features
- Building login and register functions
- Implementing role-based access control
- Managing cart and order logic
- Handling product stock after checkout
- Creating review and rating features
- Hashing passwords using BCrypt
- Handling errors with user-friendly pages
- Organizing a project using clean folder structure

---

## 21. Author

Created by: Nguyen Phuc An

This project was developed as a personal project for learning and internship portfolio purposes.
