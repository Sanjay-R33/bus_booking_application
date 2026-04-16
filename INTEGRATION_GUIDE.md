# Bus Booking Application - Integration Guide

## Project Overview
A complete bus booking system with a Java Spring Boot backend and React Vite frontend.

## Architecture

### Backend (Java Spring Boot)
- **Port**: 8080
- **API Base**: `http://localhost:8080/api`
- **Database**: MySQL (localhost:3306)
- **Database Name**: `bus_system`

### Frontend (React + Vite)
- **Port**: 5173 (default Vite)
- **Base URL**: `http://localhost:5173`

## Prerequisites

### Backend Requirements
- Java 17 or higher
- Maven 3.6+
- MySQL Server running on localhost:3306

### Frontend Requirements
- Node.js 16+
- npm 7+

## Setup Instructions

### 1. Database Setup

Create the MySQL database:
```sql
CREATE DATABASE bus_system;
USE bus_system;
```

The application will automatically create tables based on JPA entities (configuration: `spring.jpa.hibernate.ddl-auto=update`).

### 2. Backend Setup & Running

Navigate to the backend directory:
```bash
cd bus_booking_application/demo
```

Run the Spring Boot application:
```bash
mvn spring-boot:run
```

Or if using Maven wrapper:
```bash
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

Check if API is working:
```
GET http://localhost:8080/api/buses/search?source=Delhi&destination=Mumbai
```

### 3. Frontend Setup & Running

Navigate to the frontend directory:
```bash
cd bus_booking_application/frontend
```

Install dependencies (already done):
```bash
npm install
```

Run the development server:
```bash
npm run dev
```

The frontend will typically run on `http://localhost:5173`

## API Endpoints

### Authentication
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - Login user

### Buses
- `GET /api/buses/search?source=X&destination=Y&travelDate=Z` - Search buses
- `GET /api/buses/{busId}/seats` - Get seats for a bus

### Bookings
- `POST /api/bookings` - Create booking
- `GET /api/bookings/me` - Get user's bookings (requires X-User-Id header)
- `PUT /api/bookings/{bookingId}/cancel` - Cancel booking

## Configuration Files

### Backend Configuration
- `demo/src/main/resources/application.properties` - Database, mail, and server settings
- `server.servlet.context-path=/api` - Creates /api prefix for all endpoints

### Frontend Configuration
- `frontend/src/services/api.js` - Backend API base URL and all API calls

### CORS Configuration
- `demo/src/main/java/bus_application/demo/config/CorsConfig.java` - Allows frontend to communicate with backend

## Features

### User Features
- User Registration
- User Login
- Search Buses by Source, Destination, and Travel Date
- View Available Seats for a Bus
- Book Seats
- View My Bookings
- Cancel Bookings

### Data Management
- User Management with Email Notifications
- Bus Management
- Seat Management
- Booking Management with Status Tracking

## Testing the Integration

1. **Start MySQL Server** - Ensure MySQL is running on localhost:3306
2. **Start Backend** - Run `mvn spring-boot:run` from demo folder
3. **Start Frontend** - Run `npm run dev` from frontend folder
4. **Open Browser** - Navigate to `http://localhost:5173`
5. **Register/Login** - Create a new account or login
6. **Search Buses** - Search for buses between cities
7. **Make Booking** - Select seats and complete booking

## Troubleshooting

### Backend Issues

**Error: "Connection refused" for MySQL**
- Ensure MySQL is running: `mysql -u root -p`
- Check database credentials in `application.properties`

**Error: "Port 8080 already in use"**
- Change port in `application.properties`: `server.port=8081`

### Frontend Issues

**Error: "Cannot find module"**
- Run `npm install` again in frontend folder

**Error: "Backend not responding"**
- Check if backend is running on `http://localhost:8080`
- Check CORS configuration allows your frontend origin
- Check browser console for specific error messages

**Error: "Failed to fetch"**
- Verify backend API is running
- Check API_BASE_URL in `frontend/src/services/api.js`

## Development Notes

- The backend uses Lombok for reducing boilerplate code
- The frontend uses React Hooks for state management
- CORS is configured to allow requests from localhost:5173, 3000, and 5174
- Database schema is auto-generated from JPA entities
- Email notifications are configured using Gmail SMTP

## Future Enhancements

- User authentication with JWT tokens
- Payment gateway integration
- Email confirmation for bookings
- Admin dashboard
- Bus route management
- Rating and reviews system
- Search filters (price, departure time, etc.)
