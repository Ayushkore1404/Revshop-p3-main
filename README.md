 🛍️ RevShop — E-Commerce Microservices Platform

RevShop is a production-ready e-commerce platform built using a microservices architecture with Spring Boot 3, Spring Cloud, and Angular 17. It simulates a real-world online shopping experience supporting two types of users — Buyers and Sellers — each with dedicated workflows, dashboards, and role-based access.

---

 🧰 Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.2, Spring Cloud 2023 |
| Security | Spring Security, JWT (JJWT 0.12.3) |
| Database | MySQL 8.0, Spring Data JPA, Hibernate |
| Microservices | Eureka, Config Server, API Gateway, OpenFeign |
| Resilience | Resilience4j Circuit Breaker |
| API Docs | SpringDoc OpenAPI / Swagger UI |
| Frontend | Angular 17, TypeScript, Angular Material Icons |
| DevOps | Docker, Docker Compose, Nginx |

---

 🏗️ Architecture Overview
```
┌─────────────────────────────────────────────┐
│            Angular 17 Frontend              │
└───────────────────┬─────────────────────────┘
                    │ HTTP/REST
                    ▼
┌─────────────────────────────────────────────┐
│              API Gateway :8080              │
│   JWT Validation · Routing · Circuit Breaker│
└──┬──────┬──────┬──────┬──────┬─────────────┘
   ▼      ▼      ▼      ▼      ▼      ▼
:8081  :8082  :8083  :8084  :8085  :8086
User  Product  Cart  Order  Pay  Notification

        Eureka Server :8761
        Config Server :8888
```

---

 🗂️ Microservices Breakdown

 1. API Gateway (Port: 8080)
The single entry point for all incoming client requests. Responsibilities include:
- JWT token validation before forwarding requests to downstream services
- Dynamic routing to appropriate microservices
- CORS configuration for frontend communication
- Circuit breaking with Resilience4j to handle service failures gracefully
- Injects `X-User-Id` and `X-User-Role` headers so downstream services don't need to re-validate tokens

 2. User Service (Port: 8081)
Handles all authentication and user management operations.
- User registration with role selection (BUYER / SELLER)
- Login with JWT token generation (24-hour expiry)
- Profile view and update
- Password encoding with BCrypt
- Role-based access control using Spring Security

 3. Product Service (Port: 8082)
Manages the complete product catalog including inventory.
- Full CRUD operations for product listings (Sellers only)
- Category management
- Product search and filtering with pagination
- Customer reviews and star ratings
- Low stock alerts for sellers
- Soft delete for products

 4. Cart Service (Port: 8083)
Handles shopping cart and wishlist functionality.
- Add, update, and remove items from cart
- Persistent cart tied to user account
- Wishlist management — save products for later
- Cart summary with total price calculation

 5. Order Service (Port: 8084)
Manages the complete order lifecycle.
- Place orders from cart items
- Order tracking by order number
- Order cancellation by buyer
- Sellers can view and update order statuses
- Revenue and sales stats for sellers
- Communicates with Product Service (via Feign) to reduce stock
- Communicates with Notification Service (via Feign) to send emails

 6. Payment Service (Port: 8085)
Handles payment processing.
- Initiate payment for an order
- View payment history for buyers
- Payment status tracking
- Refund processing

 7. Notification Service (Port: 8086)
Handles all email and in-app notifications.
- Sends order confirmation emails
- Triggered by Order Service via OpenFeign
- Stores notification history per user
- Configurable SMTP settings

 8. Eureka Server (Port: 8761)
Service registry for all microservices.
- All services auto-register on startup
- Enables load balancing and service discovery
- Dashboard available at http://localhost:8761

 9. Config Server (Port: 8888)
Centralized configuration management.
- Serves configuration files for all microservices
- Each service fetches its config on startup
- Supports per-environment configuration

---

 ✨ Features

 Buyer
- Register and login with JWT authentication
- Browse products with search and category filters
- View detailed product pages with reviews and ratings
- Add products to cart or wishlist
- 3-step checkout flow
- Place and track orders in real time
- View payment history

 Seller
- Register as a seller and manage profile
- Create, update, and delete product listings
- Upload product images and set categories
- Monitor low stock inventory alerts
- View and manage incoming orders
- Update order status (Processing → Shipped → Delivered)
- View revenue stats and sales analytics on dashboard

---

📁 Project Structure
```
revshop/
├── pom.xml                         # Parent Maven POM
├── docker-compose.yml              # Full stack Docker setup
├── init.sql                        # Database initialization
├── schema.sql                      # Complete SQL schemas + seed data
├── config-server/                  # Spring Cloud Config Server :8888
├── eureka-server/                  # Eureka Service Discovery   :8761
├── api-gateway/                    # Spring Cloud Gateway       :8080
├── user-service/                   # Auth + User management     :8081
├── product-service/                # Product catalog + Reviews  :8082
├── cart-service/                   # Cart + Wishlist            :8083
├── order-service/                  # Order processing           :8084
├── payment-service/                # Payment processing         :8085
├── notification-service/           # Email notifications        :8086
└── revshop-frontend/               # Angular 17 SPA
    └── src/app/
        ├── components/             # Navbar, product card, star rating
        ├── pages/                  # All page components
        ├── services/               # API service layer
        ├── guards/                 # Auth route guards
        ├── interceptors/           # JWT HTTP interceptor
        └── models/                 # TypeScript interfaces
```

---

🔑 API Reference

Authentication
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/auth/register` | Register buyer/seller | ❌ |
| POST | `/api/auth/login` | Login, get JWT token | ❌ |
| GET | `/api/auth/profile` | Get own profile | ✅ |
| PUT | `/api/auth/profile` | Update profile | ✅ |

 Products
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/api/products` | List all (paginated) | ❌ |
| GET | `/api/products/{id}` | Product details | ❌ |
| GET | `/api/products/search` | Filter + search | ❌ |
| POST | `/api/products` | Create product | SELLER |
| PUT | `/api/products/{id}` | Update product | SELLER |
| DELETE | `/api/products/{id}` | Soft delete | SELLER |
| GET | `/api/products/seller` | Seller's products | SELLER |
| GET | `/api/products/seller/low-stock` | Low stock list | SELLER |
| POST | `/api/products/{id}/reviews` | Add review | BUYER |
| POST | `/api/products/{id}/rating` | Rate product | BUYER |

 Cart
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/cart` | Get cart |
| POST | `/api/cart/items` | Add item |
| PUT | `/api/cart/items/{id}` | Update quantity |
| DELETE | `/api/cart/items/{id}` | Remove item |
| DELETE | `/api/cart` | Clear cart |
| GET | `/api/cart/wishlist` | Get wishlist |
| POST | `/api/cart/wishlist` | Add to wishlist |
| DELETE | `/api/cart/wishlist/{pid}` | Remove from wishlist |

 Orders
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/orders` | Place order |
| GET | `/api/orders` | My orders (buyer) |
| GET | `/api/orders/{id}` | Order details |
| GET | `/api/orders/track/{orderNum}` | Track by order number |
| PUT | `/api/orders/{id}/cancel` | Cancel order |
| GET | `/api/orders/seller` | Seller's orders |
| PUT | `/api/orders/seller/{id}/status` | Update order status |
| GET | `/api/orders/seller/stats` | Revenue and stats |

 Payments
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/payments` | Initiate payment |
| GET | `/api/payments/order/{id}` | Payment by order |
| GET | `/api/payments/my` | Buyer payment history |
| POST | `/api/payments/{id}/refund` | Refund payment |

---

 🔒 Security

- JWT tokens signed with HMAC-SHA256 and expire after 24 hours
- API Gateway validates every token before routing requests
- Downstream services read `X-User-Id` and `X-User-Role` headers — no re-validation needed
- Passwords encrypted with BCrypt
- Role-based access: BUYER and SELLER roles with separate permissions
- CORS configured to allow only the Angular frontend origin

---

 🚀 Quick Start

 Option A — Docker Compose (Recommended)
```bash
git clone https://github.com/Ayushkore1404/Revshop-p3-main.git
cd Revshop-p3-main
docker-compose up --build
```

| URL | Description |
|---|---|
| http://localhost:4200 | Angular Frontend |
| http://localhost:8080 | API Gateway |
| http://localhost:8761 | Eureka Dashboard |
| http://localhost:8888 | Config Server |

 Option B — Run Locally
```bash
# Prerequisites: Java 17, Maven, Node 20, MySQL 8

# 1. Start MySQL and initialize database
mysql -u root -p < init.sql

# 2. Start services in this order
cd config-server        && mvn spring-boot:run &
cd eureka-server        && mvn spring-boot:run &
cd user-service         && mvn spring-boot:run &
cd product-service      && mvn spring-boot:run &
cd cart-service         && mvn spring-boot:run &
cd order-service        && mvn spring-boot:run &
cd payment-service      && mvn spring-boot:run &
cd notification-service && mvn spring-boot:run &
cd api-gateway          && mvn spring-boot:run &

# 3. Start Angular frontend
cd revshop-frontend
npm install
npm start
```

---

 📝 Swagger UI

Each service exposes API documentation at:
```
http://localhost:8081/swagger-ui.html   # user-service
http://localhost:8082/swagger-ui.html   # product-service
http://localhost:8083/swagger-ui.html   # cart-service
http://localhost:8084/swagger-ui.html   # order-service
http://localhost:8085/swagger-ui.html   # payment-service
```

---

 🐳 Docker Services

| Service | Port | Image |
|---|---|---|
| mysql | 3306 | mysql:8.0 |
| config-server | 8888 | revshop/config |
| eureka-server | 8761 | revshop/eureka |
| api-gateway | 8080 | revshop/gateway |
| user-service | 8081 | revshop/user |
| product-service | 8082 | revshop/product |
| cart-service | 8083 | revshop/cart |
| order-service | 8084 | revshop/order |
| payment-service | 8085 | revshop/payment |
| notification-service | 8086 | revshop/notification |
| angular-frontend | 4200 | revshop/frontend |

---

 🛠️ Development Notes

1. Startup Order: MySQL → Config Server → Eureka → Business Services → API Gateway → Frontend
2. User ID Header: API Gateway injects `X-User-Id` from JWT claims into every forwarded request
3. Email Notifications: Configure SMTP credentials in `notification-service.yml` or set `SPRING_MAIL_*` environment variables
4. Stock Management: Order Service calls Product Service via Feign. If Product Service is down, circuit breaker activates fallback
5. Seed Data: After startup, POST to `/api/categories` to create categories, then use a seller account to add products

---

 👨‍💻 Author

Ayush Kore
[GitHub](https://github.com/Ayushkore1404)
