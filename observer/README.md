Before fun, create a file or a link to a .env file.
```shell
ln -s ../env/dev/.env $(pwd)/.env
```

```shell
./mvnw clean quarkus:dev -Ddebug=false
```

## Generate docker image
```shell
./mvnw package -DskipTests -DskipScan -U
docker buildx build --push --platform linux/amd64,linux/arm64 --tag quay.io/marcelosales/observer -f src/main/docker/Dockerfile.jvm .
```

### Deprecated
```
docker buildx build --push --platform linux/amd64,linux/arm64 --tag marcelodsales/observer -f src/main/docker/Dockerfile.jvm .
```