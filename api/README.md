Before fun, create a file or a link to a .env file.

```shell
ln -s ../env/dev/.env $(pwd)/.env
```

```shell
mvn clean -am -Ddebug=false quarkus:dev
```

## Generate docker image

### ARCH64
```shell
./mvnw package -DskipTests -DskipScan -U

docker buildx build --push --platform linux/amd64,linux/arm64 --tag quay.io/marcelosales/api:0.0.6 -f src/main/docker/Dockerfile.jvm .
```

### Outdated
```
docker buildx build --push --platform linux/amd64,linux/arm64 --tag marcelodsales/api -f src/main/docker/Dockerfile.jvm .
```

### AMD64
```
docker build --tag quay.io/marcelosales/api:0.0.6 -f src/main/docker/Dockerfile.jvm .
```

## Execution through docker compose
When running docker-compose _uat_ you need to create a targeting rule for the keycloak. This is because keycloak
performs validation on the issuer, the URL that created the token. The api-app client creates the token pointing to the
URL http://localhost and the api-backend client points to the same instance, but since it is an internal service, we use
the name of the container. This causes a token issued with iss http://localhost to be validated against the
url http://keycloak. This token cannot be validated and therefore it is necessary that both the api-app client and the
api-backend point to the same URL. 

### Creating _"proxy"_ in linux
edit `/etc/hosts` and insert the following code
```shell
127.0.0.1 keycloak
```

### Creating _"proxy"_ in windows
edit the `C:\Windows\System32\drivers\etc\hosts` and insert the following code
```shell
127.0.0.1 keycloak
```