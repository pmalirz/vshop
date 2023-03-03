# VShop

## Prerequisites

- Docker
- JDK 17

## Initialize the project before you start

### Docker configuration 

Before you run the docker compose command please initialize the project:

```gradlew dockerInit```

It will download the Oracle JDBC jar for you (from the Maven Central) placing it to the `vshop-docker/infinispan/server/lib`. This
folder is dedicated to the Infinispan 3rd party libraries. Read more about the Infinispan `server`
folder https://infinispan.org/docs/stable/titles/server/server.html#server_root_directory

```docker compose up```


