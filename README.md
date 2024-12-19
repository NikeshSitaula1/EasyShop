# 🛒 EasyShop Backend API 🛒

## Project Overview

EasyShop is an e-commerce application designed to provide users with a seamless online shopping experience. Built using Java, Spring Boot, and MySQL, this backend API handles user authentication, product management, shopping cart operations, and user profiles. The project represents **Version 2** of EasyShop, focusing on fixing existing bugs, adding new features, and enhancing functionality.

---

# Table of Contents

- [Application Structure](#Application-Structure)
- [Features](#Features)
- [Phases Overview](#Phases-Overview)
- [Getting Started](#Getting-Started)
- [Interesting Code](#Interesting-Code)
- [License](#License)

---

## Application Structure

- **User Management:** Provides secure registration and login functionality with JWT authentication and role-based access.
- **Product Management:** Offers endpoints for product CRUD operations and enhanced search capabilities.
- **Shopping Cart:** Allows users to manage shopping cart items and calculates totals for checkout.
- **User Profile:** Supports viewing and updating user profiles.
- **Database Integration:** Uses MySQL for persistent data storage, with a `create_database.sql` script for setup.

---

## Features

- **Authentication:**
    - Secure registration and login using JWT.
    - Role-based access for users and administrators.
- **Product Operations:**
    - Search and filter products by category, price, and color.
    - CRUD operations for products and categories (admin-only).
- **Shopping Cart:**
    - Add, update, view, and clear items in the cart.
    - Persistent cart data tied to user accounts.
- **User Profile:**
    - View and update user profile details.
- **Bug Fixes and Enhancements:**
    - Improved search accuracy for products.
    - Prevented duplicate products during updates.

---

## Phases Overview

### Phase 1 - CategoriesController
- Implemented RESTful endpoints for managing product categories:
    - `GET /categories`: Fetch all categories.
    - `GET /categories/{id}`: Fetch a category by ID.
    - `POST /categories`: Add a new category (admin-only).
    - `PUT /categories/{id}`: Update a category (admin-only).
    - `DELETE /categories/{id}`: Delete a category (admin-only).

### Phase 2 - Bug Fixes
- **Bug 1:** Fixed incorrect product search results by optimizing SQL queries and handling query parameters correctly.
  ![Bug1](https://raw.githubusercontent.com/NikeshSitaula1/EasyShop/refs/heads/main/images/bug%201.png)

- **Bug 2:** Resolved duplication issues during product updates by ensuring existing products are updated instead of creating new entries.
  ![Bug2](https://raw.githubusercontent.com/NikeshSitaula1/EasyShop/refs/heads/main/images/bug%202.png)

### Phase 3 - Shopping Cart
- Developed the shopping cart feature, enabling users to:
    - Add products to their cart.
    - View current cart items, including totals and line items.
    - Update product quantities.
    - Clear the cart.

### Phase 4 - User Profile
- Added functionality to manage user profiles:
    - `GET /profile`: Fetch user profile details.
    - `PUT /profile`: Update profile information.

---

## Getting Started

### Prerequisites
- Java Development Kit (JDK)
- MySQL Database Server
- Postman or another API testing tool
- Git

### Installation
1. Clone the repository into your working directory:
   ```bash
   git clone <repository-url>
   cd <project-folder>
   ```
2. Set up the MySQL database:
    - Import `create_database.sql` from capstone-starter folder into MySQL Workbench and execute it to create the `easyshop` database.
    - The database includes three users:
        - **Admin:** Username: `admin`, Password: `password`
        - **Users:** Username: `user`, Password: `password` | Username: `george`, Password: `password`
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Copy all files from capstone-starter folder and paste it in your repository. Run the files from an IDE. That will be your Backend
5. Run the files from capstone-client-web-application folder from an IDE. That will be your frontend.
6. Use Postman to test API functionality or access the provided front-end.

### Authentication
- **Login:**
   ```http
   POST http://localhost:8080/login
   ```
  Request Body:
   ```json
   {
      "username": "admin",
      "password": "password"
   }
- Do the same for 2 more users named, `user` and `george`.
- Send, copy the tokens they provide and paste it on the Authorization as Bearer tokens.

---

## Interesting Code

![InterestingCode](https://raw.githubusercontent.com/NikeshSitaula1/EasyShop/refs/heads/main/images/intersting%20code.png)

This method further gives an evidence of how one can combine authentication with the operations 
of the DAO. It obtains user data in an acceptable manner by using the Principal of the logged in
user and takes the profile out of the database. In java, Principal is an interface that 
provides authenticated user information.

---

## License

This project is licensed under the MIT License.

---