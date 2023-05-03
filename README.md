# VShop

<img height="350" src="./docs/images/vshop-logo.jpg" title="VShop Logo" width="350"/>

Online Shop Application created for educational purpose to test different technologies, architectural and design styles.
**V**Shop stands for **V**ertically sliced. For the sake of simplicity the application is stored in the one project as a
modular monolith. Business functions are layered in separate packages (modules) and can be easily extracted to separate
microservices.

<!-- TOC -->

* [VShop](#vshop)
    * [Architecture](#architecture)
        * [Infrastructure](#infrastructure)
        * [Functional modules (Domain)](#functional-modules-domain)
    * [Prerequisites](#prerequisites)
    * [Run the app TL;DR;](#run-the-app-tldr)
    * [Initialize the infrastructure](#initialize-the-infrastructure)
        * [1. Docker configuration](#1-docker-configuration)
        * [2. Start the infrastructure](#2-start-the-infrastructure)
        * [3. Database initialization](#3-database-initialization)
    * [Run the application](#run-the-application)
    * [Accessing the application](#accessing-the-application)
        * [Example HTTP requests](#example-http-requests)

<!-- TOC -->

## Architecture

### Infrastructure

- Infinispan 14 (connected to Oracle XE)
- Oracle 21c XE
    - Two users are created on first setup (vshop and vshopcache) -
      see [SQL init scripts](./vshop-docker/oracle/scripts/setup)
        - **vshopcache** user is dedicated to the Infinispan JDBC persistent storage
        - **vshop** user is the application user

### Functional modules (Domain)

The whole application for the development simplicity is stored in the one project (and the deployment package). However,
the application is vertically sliced into the separate functional modules (domains). Modules are expressed as packages.

![vshop-packages-listing.jpg](docs/images/vshop-packages-listing.jpg)

As the project is under development and for the learning purposes the modules will be added gradually. The following
modules are already implemented:

- **products**

## Prerequisites

- Docker
- JDK 17

## Run the app TL;DR;

Running **Oracle** profiles (`JPA`, `SODA`, `JSON`):

```shell
gradlew initDocker
docker-compose up -d oracle
gradlew initDB
gradlew build
gradlew bootRun --args='--spring.profiles.active=JSON'
```

Running **MongoDB** profile (`MONGO`):

```shell
gradlew initDocker
docker-compose up -d mongo
gradlew build
gradlew bootRun --args='--spring.profiles.active=MONGO'
```

⚠️ The first build may take a little longer as the project uses the Testcontainers. Hence, the first build will download
the Docker images for the Oracle.

## Initialize the infrastructure

### 1. Docker configuration

Before you run the docker compose command please initialize the project:

```shell
gradlew initDocker
```

It will download the Oracle JDBC jar for you (from the Maven Central) placing it to
the `vshop-docker/infinispan/server/lib`. This folder is dedicated to the Infinispan 3rd party libraries. Read more
about the Infinispan `server`
folder https://infinispan.org/docs/stable/titles/server/server.html#server_root_directory

### 2. Start the infrastructure

Go to the `vshop-docker` and run the following command

```shell
docker-compose up -d
```

It will start Oracle 21c XE instance and Infinispan 14 (connected to the Oracle DB).

**Note!** that the in the current version of the project the Infinispan is not used thus it is not necessary to start
it. Thus, you can run only the following command:

```shell
docker-compose up -d oracle
```

### 3. Database initialization

Before you run the application you need to initialize the database. The following task will create all the necessary
tables for all spring profiles (JPA, SODA, JSON).

```shell
gradlew initDB
```

## Run the application

The application is configured to run with the following profiles: `JPA`, `SODA`, `JSON`, `MOGNO`.

- `JPA` profile is used to run the application with the JPA (Hibernate) persistence layer.

```shell
gradlew bootRun --args='--spring.profiles.active=JPA'
```

- `SODA` profile is used to run the application with the Oracle SODA persistence layer.

 ```shell
gradlew bootRun --args='--spring.profiles.active=SODA'
```

- `JSON` profile is used to run the application with the Oracle JSON persistence layer.

```shell
gradlew bootRun --args='--spring.profiles.active=JSON'
```

- `MONGO` profile is used to run the application with the MongoDB persistence layer.

```shell
gradlew bootRun --args='--spring.profiles.active=MONGO'
```

## Accessing the application

At the time the only way to access application is to use the Swagger UI. The Swagger UI is available under the following
URL: http://localhost:8080/swagger-ui/index.html

### Example HTTP requests

The following request will generate 1000 products and store them in the database:

```http request
POST http://localhost:8080/products/generate
Accept: application/json
Content-Type: application/json

{
  "numberOfProducts": 1000
}
```

Then you can search the products using the following request:

```http request
GET http://localhost:8080/products?textContains=whaleboat
```