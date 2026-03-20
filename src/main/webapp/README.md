# Nexacro Frontend Documentation

## Overview
This project includes a responsive web frontend built with vanilla JavaScript that integrates with the Spring Boot backend API to create an Apple Reminders clone experience.

## Features
- **Authentication**: Login and registration functionality
- **Reminder Management**: Create, read, update, and delete reminders
- **Category Management**: Organize reminders into categories
- **Priority System**: Set priorities (Low, Medium, High)
- **Search Functionality**: Search reminders by keyword
- **Filtering**: Filter reminders by priority
- **Responsive Design**: Works on desktop and mobile devices

## File Structure
```
src/main/webapp/
├── index.html              # Main HTML file
├── css/
│   └── styles.css         # Main stylesheet
├── js/
│   └── app.js             # JavaScript application logic
├── WEB-INF/
│   ├── web.xml            # Web application configuration
│   └── spring/
│       ├── applicationContext.xml    # Spring root context
│       └── servlet-context.xml      # Spring MVC context
```

## Key Components

### 1. Authentication System
- Login: POST `/api/auth/login`
- Signup: POST `/api/auth/signup`
- Token-based authentication stored in localStorage

### 2. Reminder Management
- Create reminder: POST `/api/reminders`
- Get reminders: GET `/api/reminders`
- Update reminder: PUT `/api/reminders/{id}`
- Delete reminder: DELETE `/api/reminders/{id}`
- Toggle completion: PATCH `/api/reminders/{id}/complete`
- Filter by priority: GET `/api/reminders/priority/{priority}`
- Search: GET `/api/reminders/search?keyword={keyword}`

### 3. Category Management
- Create category: POST `/api/categories`
- Get categories: GET `/api/categories`
- Update category: PUT `/api/categories/{id}`
- Delete category: DELETE `/api/categories/{id}`

## API Integration
The frontend uses the Fetch API to communicate with the backend. All API requests include the `X-User-Id` header for authentication.

## Styling
- Modern, clean design inspired by Apple's UI
- Responsive layout using CSS Grid and Flexbox
- Smooth transitions and hover effects
- Mobile-first responsive design

## JavaScript Architecture
The application follows a modular structure:
- `NexacroApp` class manages the entire application
- Event-driven architecture
- Separation of concerns (UI, API, state management)

## Deployment
1. Build the Spring Boot application: `mvn clean package`
2. Deploy the WAR file to a servlet container
3. The webapp directory will be served at the root context

## Testing
The frontend can be tested by:
1. Starting the Spring Boot application
2. Accessing `http://localhost:9090`
3. Testing all CRUD operations through the UI

## Notes
- The frontend uses vanilla JavaScript without any frameworks for simplicity
- All state is managed in the browser using JavaScript
- The UI updates dynamically based on API responses