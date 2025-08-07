# KRB Bank - Local Development Setup Guide

## Overview

KRB Bank is a full-stack banking application built with Spring Boot backend and React TypeScript frontend. This guide provides comprehensive instructions for setting up the application locally for development.

## Architecture

- **Backend**: Spring Boot 3.2.0 with Java 17, H2 database, Spring Security with BCrypt
- **Frontend**: React TypeScript with Vite, Tailwind CSS, and shadcn/ui components
- **Database**: H2 file-based database with automatic demo data initialization
- **Authentication**: Session-based authentication with BCrypt password encoding

## Prerequisites

Before setting up the application, ensure you have the following installed:

### Required Software
- **Java 17** or higher
- **Maven 3.6+** for backend dependency management
- **Node.js 18+** for frontend development
- **npm 8+** or **yarn** for frontend package management
- **Git** for version control

### Verify Installation
```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Node.js version
node -version

# Check npm version
npm -version
```

## Project Structure

```
krb/
├── backend/                 # Spring Boot backend application
│   ├── src/main/java/      # Java source code
│   ├── src/main/resources/ # Configuration files
│   ├── pom.xml            # Maven dependencies
│   └── data/              # H2 database files (auto-created)
├── krb-frontend/          # React TypeScript frontend
│   ├── src/               # React source code
│   ├── package.json       # npm dependencies
│   └── .env              # Environment variables
└── SETUP.md              # This setup guide
```

## Backend Setup

### 1. Navigate to Backend Directory
```bash
cd backend
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Configure Application Properties
The backend is pre-configured with the following settings in `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# H2 Database Configuration
spring.datasource.url=jdbc:h2:file:./data/krb_db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

# H2 Console (for development)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Security Configuration
spring.security.user.name=admin
spring.security.user.password=admin123
```

### 4. Start Backend Application
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080` and automatically:
- Create the H2 database file at `./data/krb_db`
- Initialize demo users with BCrypt-encoded passwords
- Enable H2 console at `http://localhost:8080/h2-console`

### 5. Verify Backend Setup
- Check console output for "Started KrbBackendApplication" message
- Verify demo users creation in logs:
  ```
  Default users created:
  Admin - Username: admin, Password: admin123
  Employee - Username: employee1, Password: emp123
  ```

## Frontend Setup

### 1. Navigate to Frontend Directory
```bash
cd krb-frontend
```

### 2. Install Dependencies
```bash
npm install
```

### 3. Configure Environment Variables
Create or verify the `.env` file contains:

```env
VITE_API_BASE_URL=http://localhost:8080
```

### 4. Start Frontend Application
```bash
npm run dev
```

The frontend will start on `http://localhost:5173` with hot-reload enabled.

### 5. Verify Frontend Setup
- Open browser to `http://localhost:5173`
- Verify the KRB Bank login page loads correctly
- Check browser console for any errors

## Database Access

### H2 Console Access
1. Navigate to `http://localhost:8080/h2-console`
2. Use the following connection settings:
   - **JDBC URL**: `jdbc:h2:file:./data/krb_db`
   - **User Name**: `sa`
   - **Password**: `password`
3. Click "Connect"

### Demo Data Verification
Execute the following SQL queries to verify demo users:

```sql
-- View all employees
SELECT * FROM EMPLOYEES;

-- Check specific demo users
SELECT id, username, first_name, last_name, email, role, created_at 
FROM EMPLOYEES 
WHERE username IN ('admin', 'employee1');
```

Expected results:
- **admin**: ID=1, Role=ADMIN, Email=admin@krb.com
- **employee1**: ID=2, Role=EMPLOYEE, Email=employee1@krb.com

## Demo Credentials

The application comes with pre-configured demo users:

### Admin User
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: ADMIN
- **Email**: admin@krb.com

### Employee User
- **Username**: `employee1`
- **Password**: `emp123`
- **Role**: EMPLOYEE
- **Email**: employee1@krb.com

## Testing the Setup

### 1. Complete Application Test
1. Ensure both backend (port 8080) and frontend (port 5173) are running
2. Navigate to `http://localhost:5173`
3. Test login with demo credentials:
   - Try `admin` / `admin123`
   - Try `employee1` / `emp123`
4. Verify successful authentication and user dashboard access

### 2. API Endpoint Testing
Test the authentication endpoint directly:

```bash
# Test login endpoint
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"employee1","password":"emp123"}'
```

Expected response: User data with role and profile information (password excluded).

## Development Workflow

### Starting Development Session
1. Start backend: `cd backend && mvn spring-boot:run`
2. Start frontend: `cd krb-frontend && npm run dev`
3. Access application at `http://localhost:5173`

### Making Changes
- **Backend changes**: Spring Boot auto-reloads on file changes
- **Frontend changes**: Vite provides hot-reload for instant updates
- **Database changes**: H2 console available for direct database access

### Stopping Applications
- **Backend**: Press `Ctrl+C` in the Maven terminal
- **Frontend**: Press `Ctrl+C` in the npm terminal

## Troubleshooting

### Common Issues and Solutions

#### 1. "Invalid username or password" Error
**Symptoms**: Login fails with demo credentials
**Causes**: 
- Backend not running or not accessible
- Database initialization failed
- Password encoding mismatch

**Solutions**:
1. Verify backend is running on port 8080
2. Check backend logs for demo user creation messages
3. Verify H2 database contains users with encrypted passwords
4. Restart backend application to reinitialize database

#### 2. Frontend Cannot Connect to Backend
**Symptoms**: Network errors, CORS issues, API calls failing
**Solutions**:
1. Verify backend is running on `http://localhost:8080`
2. Check `.env` file has correct `VITE_API_BASE_URL=http://localhost:8080`
3. Verify CORS configuration in `AuthController.java` includes `http://localhost:5173`
4. Check browser network tab for specific error details

#### 3. Port Already in Use
**Symptoms**: "Port 8080 already in use" or "Port 5173 already in use"
**Solutions**:
1. Kill existing processes:
   ```bash
   # Find and kill process on port 8080
   lsof -ti:8080 | xargs kill -9
   
   # Find and kill process on port 5173
   lsof -ti:5173 | xargs kill -9
   ```
2. Or use different ports by modifying configuration files

#### 4. Database Connection Issues
**Symptoms**: H2 console connection fails, database errors in logs
**Solutions**:
1. Ensure `data/` directory exists and is writable
2. Delete `data/krb_db.*` files and restart backend to recreate database
3. Verify H2 console settings match `application.properties`

#### 5. Maven Build Failures
**Symptoms**: Dependencies not downloading, compilation errors
**Solutions**:
1. Clear Maven cache: `mvn clean`
2. Force dependency update: `mvn clean install -U`
3. Verify Java 17 is being used: `mvn -version`

#### 6. npm Install Issues
**Symptoms**: Package installation failures, version conflicts
**Solutions**:
1. Clear npm cache: `npm cache clean --force`
2. Delete `node_modules` and reinstall: `rm -rf node_modules && npm install`
3. Use specific Node.js version with nvm if available

### Debug Mode

#### Backend Debug Logging
Add to `application.properties` for detailed logging:
```properties
logging.level.com.krb.backend=DEBUG
logging.level.org.springframework.security=DEBUG
```

#### Frontend Debug Mode
Check browser developer tools:
1. **Console tab**: JavaScript errors and API call logs
2. **Network tab**: HTTP requests and responses
3. **Application tab**: Local storage and session data

## Key Components

### Backend Components
- **AuthController**: Handles login/logout endpoints
- **EmployeeService**: User management and authentication logic
- **DataInitializer**: Creates demo users on application startup
- **SecurityConfig**: Spring Security configuration
- **Employee Entity**: User data model with BCrypt password encoding

### Frontend Components
- **Login Component**: Authentication form with validation
- **API Service**: HTTP client for backend communication
- **Environment Configuration**: Vite environment variables

### Database Schema
- **EMPLOYEES table**: User accounts with encrypted passwords
- **CUSTOMERS table**: Customer data linked to employees
- **Automatic relationships**: Foreign key constraints and data integrity

## Security Notes

- Passwords are encrypted using BCrypt with strength 10
- Demo credentials are for development only
- H2 console should be disabled in production
- CORS is configured for local development ports only
- Session-based authentication with secure cookie handling

## Next Steps

After successful setup:
1. Explore the application features and user interface
2. Review the codebase structure and architecture
3. Set up your preferred IDE with Java and TypeScript support
4. Configure debugging and testing environments
5. Review security configurations before production deployment

## Support

If you encounter issues not covered in this guide:
1. Check application logs for specific error messages
2. Verify all prerequisites are correctly installed
3. Ensure ports 8080 and 5173 are available
4. Review browser developer tools for frontend issues
5. Test API endpoints directly using curl or Postman

For additional help, refer to the project documentation or contact the development team.
