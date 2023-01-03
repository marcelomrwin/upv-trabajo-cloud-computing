# executor Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/executor-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- Camel Minio ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/minio.html)): Store and retrieve objects from Minio Storage Service using Minio SDK
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC

## Image Requirements
Image must have python3
sudo dnf install imagemagick
sudo apt install imagemagick

Before fun, create a file or a link to a .env file.
```shell
ln -s ../env/dev/.env $(pwd)/.env
```

```shell
./mvnw clean quarkus:dev -Ddebug=false
```


## Generate docker image

### AARCH64
```shell
./mvnw package -DskipTests -DskipScan -U
docker buildx build --push --platform linux/amd64,linux/arm64 --tag quay.io/marcelosales/executor -f src/main/docker/Dockerfile.jvm .
```

### Deprecated
```
docker buildx build --push --platform linux/amd64,linux/arm64 --tag marcelodsales/executor -f src/main/docker/Dockerfile.jvm .
```

### AMD64
```
docker build --tag quay.io/marcelosales/executor:0.0.5 -f src/main/docker/Dockerfile.jvm .
```