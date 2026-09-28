# StayMate 🏠

StayMate is a **Rental and PG Management Platform** developed using **Spring Boot**.  
It provides a backend REST API for property management, user authentication, property search, bookings, payments, image management, and email notifications.

The project is designed to simplify the process of discovering rental properties/PGs, managing properties, making bookings, and handling payments.

---

## 🚀 Features

### 👤 User Management
- User registration and login
- JWT-based authentication
- Role-based access control
- User profile management
- Password encryption using BCrypt
- Google OAuth2 login

### 🏠 Property Management
- Create properties
- Update property details
- Delete properties
- Property ownership verification
- Admin property management
- Property image upload
- Cloudinary integration for image storage
- Property availability management

### 🔍 Property Search & Filtering
Users can search and filter properties based on available property information such as:

- Location
- City
- Property type
- Rent
- Availability
- Other supported property attributes

### 📅 Booking Management
- Create property bookings
- Specify number of rooms
- Calculate booking amount
- Booking status management
- Owner booking management
- Admin booking management
- Booking cancellation

### 💳 Payment Integration
- Razorpay payment gateway integration
- Razorpay order creation
- Payment verification
- Payment status tracking
- Booking and payment status management

### 📧 Email Notifications
Email functionality is implemented for important events such as:

- Welcome email
- Booking confirmation
- Payment confirmation
- Booking cancellation

### 🗺️ Property Location
Properties support latitude and longitude information, allowing location-based functionality and future map integration.

---

## 🛠️ Technologies Used

### Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- REST API
- JWT
- OAuth2

### Database
- MySQL

### Payment
- Razorpay

### Cloud Storage
- Cloudinary

### Email
- JavaMailSender
- Gmail SMTP

### Build Tool
- Maven

### Development Tools
- Eclipse / Spring Tools Suite
- Git
- GitHub
- Postman

---

## 🏗️ Project Architecture

The project follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
