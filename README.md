# Web Task Manager

A web task manager, storing tasks in a postgreSQL database.

CRUD operations on tasks are dynamic and do not require a page reload, the content is updated on the user side.
That is achieved using a combination of AJAX requests and jQuery DOM operations

For HTTP requests that edit the database, the CSRF token is used

### Features
* Creating tasks
* Reading tasks
* Updating tasks
* Deleting tasks
### Technologies
* Java
* Spring boot
* Spring web
* Spring security
* Spring MVC
* Maven
* HTML, CSS, Bootstrap, JavaScript
* jQuery, AJAX
### Launching with Docker
The project contains all the needed files to launch the program as a docker container.
If you have docker installed on your machine, you need to head to the src/main/docker folder and run:
* docker build -t itsalexandermedvedev:task-manager .
* docker compose up
  
A postgreSQL database will be set up automatically,
and it will store information about the tasks even after shutting down the container
### Launching without Docker
To launch the application without docker, you need to have a local or an embedded database, like H2.
To connect to it, you need to modify the application.properties file:
* spring.datasource.url=YOUR_DB_URL
* spring.datasource.username=YOUR__DB_USERNAME
* spring.datasource.password=YOUR_DB_PASSWORD
* spring.datasource.driver-class-name=YOUR_DB_DRIVER_CLASSNAME
  
But the application is very easily accessible via docker

### Authorization page
To enter the application use:
* username: **user**
* password: **pass**
