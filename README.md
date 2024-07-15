# Web Task Manager

### Overview
This project is a dynamic web-based task manager that stores tasks in a PostgreSQL database. It allows users to create, read, update, and delete (CRUD) tasks seamlessly without requiring a page reload, using a combination of AJAX requests and jQuery DOM operations. User authentication is handled with JWT, ensuring secure interactions with the application. Each user has their own specific set of tasks that they can filter and sort.

### Features
- **User Authentication**: Users must register and log in with their own credentials. JWT is used for authentication, and passwords are securely hashed in the database.
- **Task Management**:
  - **Creating Tasks**: Users can create new tasks with specified categories and priorities.
  - **Reading Tasks**: Tasks are displayed in a sortable and filterable table. Tasks can be filtered by categories and priorities.
  - **Updating Tasks**: Tasks can be edited dynamically.
  - **Deleting Tasks**: Users can delete tasks as needed.
  - **Task Highlighting**: Tasks are highlighted in red if the due date is 1 day or less ahead, and in orange if the due date is between 1 and 2 days.
- **Security**: JWT tokens are used for securing HTTP requests.

### Technologies
- **Backend**: 
  - Java
  - Spring Boot
  - Spring Web
  - Spring Security
  - Spring MVC
  - Maven
- **Frontend**:
  - HTML
  - CSS
  - Bootstrap
  - JavaScript
  - jQuery
  - AJAX
- **Template Engine**: Thymeleaf
- **Database**: PostgreSQL
- **Containerization**: Docker

### Running the Application

#### Launching with Docker
The project includes all necessary files to launch the application as a Docker container, providing a convenient and consistent setup process. To do this, ensure you have Docker installed on your machine, navigate to the `src/main/docker` folder, and run the following commands:

1. **Build the Docker Image**:
   ```sh
   docker build -t itsalexandermedvedev/task-manager .
   ```

2. **Run the Docker Compose Configuration**:
   ```sh
   docker-compose up
   ```

This process sets up a PostgreSQL database automatically, which retains task information even after the container is shut down.

#### Subsequent Launches
For subsequent launches, you can simply use the following command to start the application:
```sh
docker-compose up
```
This command will start the existing containers without rebuilding them, ensuring quick and efficient startups.

#### Stopping the Application
To stop the application and its containers, use:
```sh
docker-compose down
```

#### Removing Containers and Volumes
If you need to remove the containers and associated volumes (which will also delete the stored data), you can run:
```sh
docker-compose down -v
```

#### Launching without Docker
To launch the application without Docker, you need a local or embedded database (e.g., H2). Modify the `application.properties` file to connect to your database:

```properties
spring.datasource.url=YOUR_DB_URL
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=YOUR_DB_DRIVER_CLASSNAME
```

However, using Docker is recommended for ease of setup and portability.

### Authorization
To access the application, register a new user and log in with your credentials.
