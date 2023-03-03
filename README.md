# VShop

PoC Online Shop Application created to test different architectural and design styles.

## Architecture

### Infrastructure

- Infinispan 14 (connected to Oracle XE)
- Oracle 21c XE
    - Two users are created on first setup (vshop and vshopcache) -
      see [SQL init scripts](./vshop-docker/oracle/scripts/setup)
        - **vshopcache** user is dedicated to the Infinispan JDBC persistent storage
        - **vshop** user is the application user

## Prerequisites

- Docker
- JDK 17

## Initialize the project before you start

### Docker configuration

Before you run the docker compose command please initialize the project:

```shell
gradlew dockerInit
```

It will download the Oracle JDBC jar for you (from the Maven Central) placing it to
the `vshop-docker/infinispan/server/lib`. This folder is dedicated to the Infinispan 3rd party libraries. Read more
about the Infinispan `server`
folder https://infinispan.org/docs/stable/titles/server/server.html#server_root_directory

## Start the infrastructure

Go to the `vshop-docker` and run the following command

```shell
docker-compose up -d
```

It will start Oracle 21c XE instance and Infinispan 14 (connected to the Oracle DB).


