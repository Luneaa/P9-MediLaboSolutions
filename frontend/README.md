# MédiLabo Frontend Service

Spring Boot frontend service with Thymeleaf and Tailwind CSS.

## Prerequisites
- Java 17+
- Maven 3.6+
- Node.js (for Tailwind CSS)

## Setup

1. Install dependencies:
```bash
mvn clean install
npm install
```

2. Build Tailwind CSS:
```bash
npm run build:css
```

Or watch for changes during development:
```bash
npm run watch:css
```

3. Run the application:
```bash
mvn spring-boot:run
```

The frontend will be available at: http://localhost:8081

## Configuration

- Gateway URL: Configured in `application.properties` (default: http://localhost:8080)
- Port: 8081
- WebClient is configured to communicate with the gateway

## Development

- Thymeleaf templates are in `src/main/resources/templates/`
- Static files (CSS, JS) are in `src/main/resources/static/`
- Tailwind input file: `src/main/resources/static/css/input.css`
- Tailwind output file: `src/main/resources/static/css/output.css`
