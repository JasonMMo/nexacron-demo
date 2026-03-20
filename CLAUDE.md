# Nexacro Demo Project - Documentation for Claude Code

## Project Overview
This is a Spring Boot-based demo application that implements an Apple Reminders app clone using Nexacro framework. The project follows a phased development approach starting with MVP authentication.

## Technology Stack
- **Framework**: Spring Boot 2.5.15 (Java 8 compatible)
- **Build Tool**: Maven
- **Database**: H2 (in-memory for development)
- **Authentication**: JWT (custom implementation)
- **Architecture**: Layered architecture (Entity-Repository-Service-Controller)
- **UI Framework**: Nexacro (planned for Phase 3+)

## Project Structure
```
nexacron-demo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/nexacron/
│   │   │       ├── controller/     # REST API controllers
│   │   │       ├── dto/           # Data Transfer Objects
│   │   │       ├── model/         # JPA entities
│   │   │       ├── repository/   # JPA repositories
│   │   │       ├── service/       # Business logic
│   │   │       └── util/         # Utility classes
│   │   └── resources/
│   │       ├── application.yml       # Main configuration
│   │       ├── application-dev.yml    # Development profile
│   │       └── application-prod.yml   # Production profile (to be added)
├── test/
│   └── java/
│       └── com/example/nexacron/    # Test classes
└── pom.xml                          # Maven dependencies
```

## Key Design Patterns
1. **DTO Pattern**: Separate entities from API payloads
2. **Repository Pattern**: Clean data access layer
3. **Service Layer**: Business logic encapsulation
4. **RESTful API**: Standard HTTP methods and status codes
5. **Component Scanning**: Auto-detection of Spring components

## Code Style Guidelines

### Java Code
- Use Lombok annotations for boilerplate code (@Data, @NoArgsConstructor, etc.)
- Follow naming conventions: camelCase for variables/methods, PascalCase for classes
- Use final for immutable variables and method parameters where appropriate
- Prefer composition over inheritance
- Keep methods focused and single-purpose
- Use meaningful variable names (avoid abbreviations)

### REST API Design
- Use standard HTTP status codes:
  - 200 OK: Successful GET/PUT
  - 201 Created: Successful POST
  - 400 Bad Request: Invalid input
  - 401 Unauthorized: Authentication required
  - 404 Not Found: Resource not found
  - 500 Internal Server Error: Server error
- Use consistent endpoint naming: `/api/resource/action`
- Include proper error messages in exception responses
- Use @RequestBody for POST/PUT requests
- Use @RequestParam for query parameters

### Configuration
- Use profile-specific configurations (dev, prod)
- Externalize configuration values (application.yml)
- Use @Value for injecting configuration properties
- Provide default values for optional configurations

## Testing Approach

### Unit Testing
- Use JUnit 5 for unit tests
- Test business logic in service layer
- Mock dependencies using Mockito
- Test edge cases and error scenarios

### Integration Testing
- Test REST endpoints with @SpringBootTest
- Use TestRestTemplate for HTTP testing
- Verify database interactions
- Test authentication flows

### Testing Best Practices
- Write tests before implementing features (TDD when appropriate)
- Aim for high test coverage (80%+)
- Use descriptive test method names
- Follow AAA pattern: Arrange, Act, Assert
- Clean up test data after tests

## Authentication System

### JWT Implementation
- Simple Base64 encoding for MVP (to be replaced with proper JWT library)
- Token includes user email in payload
- Default expiration: 24 hours
- Secret key configuration through application.yml

### Password Handling
- Simple encoding for MVP (not bcrypt)
- Format: "encoded_" + plain password
- Always compare using matches() method
- Plan to upgrade to bcrypt in production

## Database Configuration

### H2 Database
- In-memory mode for development
- Console available at /h2-console
- DDL: create-drop (auto recreate on startup)
- Show SQL queries enabled for debugging

### JPA/Hibernate
- Use @Entity for database tables
- Use @Repository for data access
- Use @Transactional for service methods
- Follow naming conventions for table/column names

## Development Workflow

### Phase 1 - MVP (4 weeks)
1. Basic authentication system ✓
2. User management endpoints
3. Health check endpoints
4. Basic error handling

### Phase 2 - Core Features (6 weeks)
1. Reminder entity and CRUD operations
2. Category management
3. Basic filtering
4. REST API documentation

### Phase 3 - UI Integration (8 weeks)
1. Nexacro frontend integration
2. List views and forms
3. Client-side validation
4. Responsive design

### Phase 4 - Advanced Features (8 weeks)
1. Search functionality
2. Recurring reminders
3. Notifications
4. Sharing features

### Phase 5 - Production Ready (6 weeks)
1. Security hardening
2. Performance optimization
3. Testing coverage
4. Documentation

## Common Issues and Solutions

### 404 Errors on Endpoints
- Check if controller is in component scan path
- Verify @RequestMapping annotations
- Ensure server is running on correct port

### JWT Token Issues
- Verify token format (header.payload.signature)
- Check Bearer token prefix in Authorization header
- Validate token expiration

### Database Connection
- Confirm H2 is running
- Check datasource configuration
- Verify database schema is created

## Reference Documentation
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/2.5.15/reference/html/)
- [JPA/Hibernate Documentation](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/)
- [Maven Documentation](https://maven.apache.org/guides/index.html)
- [JWT.io](https://jwt.io/) for JWT specifications
- [Nexacro Documentation](https://www.nexacro.com/) (to be added when integrated)

## Project Notes
- Avoid Spring Security for simplicity in MVP
- Use simple implementations first, optimize later
- Keep commits small and focused
- Write tests for critical functionality
- Document API changes as they happen