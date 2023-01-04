Before fun, create a file or a link to a .env file.
```shell
ln -s ../env/dev/.env $(pwd)/.env
```

```shell
./mvnw clean quarkus:dev -Ddebug=false
```

## Generate docker image

### Generate de bynaries of application
```
./mvnw package -DskipTests -DskipScan -U
```

#### Generate image

**In aarch64**

```shell
docker buildx build --push --platform linux/amd64,linux/arm64 --tag quay.io/marcelosales/observer -f src/main/docker/Dockerfile.jvm .
```

**For Openshift**
```
docker buildx build --push --platform linux/amd64 --tag quay.io/marcelosales/observer:0.0.6 -f src/main/docker/Dockerfile.jvm .
```

### Deprecated
```
docker buildx build --push --platform linux/amd64,linux/arm64 --tag marcelodsales/observer -f src/main/docker/Dockerfile.jvm .
```

**In amd64**
```shell
docker build --tag quay.io/marcelosales/observer:0.0.5 -f src/main/docker/Dockerfile.jvm .
```