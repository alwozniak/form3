# Payments API

Solution for a recruitment exercise. For details on API design please check `solution.pdf` file.

## Deployment

### Prerequisites

Make sure that the following software is installed on a deployment machine: 
* Java SDK in version 8 or above 
* Apache Maven, recommended version: `3.3.9`
* PostgreSQL server, version `9.5` or above

### Deployment process 

1. Create an empty Postgres database on your localhost or another host you have access to. 
   Make your local user an owner of this database.
1. Rename `src/main/resources/application-template.properties` to `application.properties` and provide valued for missing 
   datasource variables, using database name, username and password you used in the previous step. 
   For example, if your database is named `form3_db` and is created on your local Postgres server, and is owned by 
   `postgres_user` whose password is `postgres_password`:
    ```text
    spring.datasource.url = jdbc:postgresql://localhost:5432/form3_db
    spring.datasource.username = postgres_user 
    spring.datasource.password = postgres_password
    ``` 
1. In your terminal, navigate to the root folder of this repository.
1. Run `mvn clean package` to run API's test suite and create a deployable package file.
1. Run `mvn spring-boot:run` to start the server.
