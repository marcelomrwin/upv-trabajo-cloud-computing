# Máster Universitario en Computación en la Nube y de Altas Prestaciones

## Trabajo cloud computing

### Instructions to play

Clone this repo
```
git clone https://github.com/marcelomrwin/ccproject
```
===

### Test with development environment
* IDE [Intellij, Vscode, Eclipse]
* Java 17
* Apache Maven
* Container CLI [Docker, Podman]
* REST Client [Postman, Insomnia ]

**Compile all projects**
```
mvn clean install -DskipTests -DskipScan -U
```

#### Injector
```
cd injector/injector-service
./mvnw clean quarkus:dev -Ddebug=false
```

#### Executor
```
cd executor
./mvnw clean quarkus:dev -Ddebug=false
```

#### Observer
```
cd observer
./mvnw clean quarkus:dev -Ddebug=false
```

#### Api
```
cd api
./mvnw clean quarkus:dev -Ddebug=false
```

#### Api-App

Api-app needs a .env file like below:

```
PORT=8091
KEYCLOAK_BASE_URL=http://localhost:8180
API_URL_BASE=localhost:8083
API_ENDPOINT=http://localhost:8083/api
```

Then run:

```
cd api-app
nmp start
```

===

## Run in local environment with Docker Compose

Inside the /env folder are settings for files needed to provision the entire infrastructure and also to test the system using the docker images published in the quay.io repository.

If you need to generate images for each project, within the README.md file of each project there are instructions on how to generate the docker image.

The docker-compose.yaml file is configured to use images with the latest tag, this is perfectly configurable by editing the file and changing it to the desired tag.

### Provisioning infrastructure with docker compose
```
cd env/dev
```shell
docker-compose down && docker-compose pull && docker-compose --env-file .env up
```

The .env file used to provide the local infrastructure has a configuration similar to the one below:

```properties
# Infrastructure configuration
# ----------------------------#

# Postgres
POSTGRES_IMAGE=postgres:13

# Redis
REDIS_ENDPOINT=redis://localhost:6379
REDIS_PASSWORD=password

POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# Kafka
KAFKA_PORT=19092
KAFKA_SERVERS=localhost:${KAFKA_PORT}
KAFKA_RETENTION_HOURS=24
KAFKA_DEFAULT_PARTITIONS=3
KAFKA_ZOOKEEPER_URL=zookeeper:2181

# MinIO
MINIO_ROOT_USER=admin
MINIO_ROOT_PASSWORD=password
MINIO_ENDPOINT=http://localhost:9000


# PgAdmin
PGADMIN_DEFAULT_EMAIL=admin@admin.com
PGADMIN_DEFAULT_PASSWORD=password

# Keycloak
KEYCLOAK_BASE_URL=http://localhost:8180
KEYCLOAK_ENDPOINT=${KEYCLOAK_BASE_URL}/realms/cc
KEYCLOAK_DB_PORT=5432
KEYCLOAK_DB_NAME=keycloak
KEYCLOAK_DB_USER=keycloak
KEYCLOAK_DB_PASSWORD=password
KEYCLOAK_DB_HOST=postgres-keycloak
KEYCLOAK_HTTP_PORT=8180
KEYCLOAK_HTTPS_PORT=8543
KEYCLOAK_ADMIN_USER=admin
KEYCLOAK_ADMIN_PASSWORD=admin

# Services Configuration
# ----------------------#

# Executor
MONGODB_DB_USER=mongoadmin
MONGODB_DB_PASSWORD=mongopasswd
MONGODB_URL=mongodb://localhost:27017
MONGODB_DB_NAME=executordb
MONGODB_INIT_ROOT_USER=root
MONGODB_INIT_ROOT_PASSWD=root

# Injector
INJECTOR_CRON=0 * * * * ?
INJECTOR_DB_USER=postgres
INJECTOR_DB_PASSWORD=postgres
INJECTOR_DB_HOST=localhost
INJECTOR_DB_PORT=5433
INJECTOR_DB_NAME=newsdb
INJECTOR_HTTP_PORT=8082
INJECTOR_MEDIASTACK_APIKEY=<CONFIDENTIAL>
INJECTOR_MEDIASTACK_URL=http://api.mediastack.com/v1
INJECTOR_NEWSAPI_APIKEY=<CONFIDENTIAL>
INJECTOR_NEWSAPI_URL=https://newsapi.org/v2
INJECTOR_NEWSDATA_APIKEY=<CONFIDENTIAL>
INJECTOR_NEWSDATA_URL=https://newsdata.io/api/1
INJECTOR_ENDPOINT=http://localhost:${INJECTOR_HTTP_PORT}

# Observer
OBSERVER_METRICS_SCHEDULE=30s

# API
API_HTTP_PORT=8083
API_ENDPOINT=http://localhost:${API_HTTP_PORT}
API_DB_HOST=localhost
API_DB_USER=postgres
API_DB_PASSWORD=postgres
API_DB_PORT=5434
API_DB_NAME=apidb
API_KEYCLOAK_CLIENT_ID=api-backend
API_KEYCLOAK_CLIENT_SECRET=<CONFIDENTIAL>
```

### Services
* GUI - http://localhost:8091
* API - http://localhost:8083
* Keycloak - http://localhost:8180

### Support tools
* Kafka UI http://localhost:9080/
* MinIO Console http://localhost:9001/ (admin/password)
* MongoDB Express http://localhost:9081/ (admin/pass)
* PgAdmin http://localhost:9082/
* RedisInsight http://localhost:9085/
* Keycloak http://localhost:8180/ (admin/admin)

===

## Running the whole project locally using docker-compose

```shell
cd env/docker
docker-compose down && docker-compose pull && docker-compose --env-file .env up
```
To run it, you need an .env file like the one below:

```properties
# Infrastructure configuration
# ----------------------------#

# Postgres
POSTGRES_IMAGE=postgres:13

# Redis
REDIS_ENDPOINT=redis://redis:6379
REDIS_PASSWORD=password

POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# Kafka
KAFKA_PORT=9092
KAFKA_SERVERS=kafka:${KAFKA_PORT}
KAFKA_RETENTION_HOURS=24
KAFKA_DEFAULT_PARTITIONS=3
KAFKA_ZOOKEEPER_URL=zookeeper:2181

# MinIO
MINIO_ROOT_USER=admin
MINIO_ROOT_PASSWORD=password
MINIO_ENDPOINT=http://minio:9000


# PgAdmin
PGADMIN_DEFAULT_EMAIL=admin@admin.com
PGADMIN_DEFAULT_PASSWORD=password

# Keycloak
KEYCLOAK_ENDPOINT=http://keycloak:8180/realms/cc
KEYCLOAK_DB_PORT=5432
KEYCLOAK_DB_NAME=keycloak
KEYCLOAK_DB_USER=keycloak
KEYCLOAK_DB_PASSWORD=password
KEYCLOAK_DB_HOST=postgres-keycloak
KEYCLOAK_HTTP_PORT=8180
KEYCLOAK_HTTPS_PORT=8543
KEYCLOAK_BASE_URL=http://keycloak:8180
KEYCLOAK_ADMIN_USER=admin
KEYCLOAK_ADMIN_PASSWORD=admin

# Services Configuration
# ----------------------#

# Executor
MONGODB_DB_USER=mongoadmin
MONGODB_DB_PASSWORD=mongopasswd
MONGODB_URL=mongodb://mongodb-executor:27017
MONGODB_DB_NAME=executordb
MONGODB_INIT_ROOT_USER=root
MONGODB_INIT_ROOT_PASSWD=root

# Injector
INJECTOR_CRON=0 * * * * ?
INJECTOR_DB_USER=postgres
INJECTOR_DB_PASSWORD=postgres
INJECTOR_DB_HOST=postgres-injector
INJECTOR_DB_PORT=5432
INJECTOR_DB_NAME=newsdb
INJECTOR_HTTP_PORT=8082
INJECTOR_MEDIASTACK_APIKEY=<CONFIDENTIAL>
INJECTOR_MEDIASTACK_URL=http://api.mediastack.com/v1
INJECTOR_NEWSAPI_APIKEY=<CONFIDENTIAL>
INJECTOR_NEWSAPI_URL=https://newsapi.org/v2
INJECTOR_NEWSDATA_APIKEY=<CONFIDENTIAL>
INJECTOR_NEWSDATA_URL=https://newsdata.io/api/1
INJECTOR_ENDPOINT=http://injector:8080

# Observer
OBSERVER_METRICS_SCHEDULE=30s

# API
API_HTTP_PORT=8083
API_URL_BASE=localhost:${API_HTTP_PORT}
API_ENDPOINT=http://${API_URL_BASE}
API_DB_HOST=postgres-api
API_DB_USER=postgres
API_DB_PASSWORD=postgres
API_DB_PORT=5432
API_DB_NAME=apidb
API_KEYCLOAK_CLIENT_ID=api-backend
API_KEYCLOAK_CLIENT_SECRET=<CONFIDENTIAL>
```

### Services
* GUI - http://localhost:8091
* API - http://localhost:8083
* Keycloak - http://localhost:8180

### Support tools
* Kafka UI http://localhost:9080/
* MinIO Console http://localhost:9001/ (admin/password)
* MongoDB Express http://localhost:9081/ (admin/pass)
* PgAdmin http://localhost:9082/
* RedisInsight http://localhost:9085/
* Keycloak http://localhost:8180/ (admin/admin)

===

## Run in local environment with Minikube
* Minikube
* Kubernetes client
* Helm

Create a profile on minikube and set it up as below:
```
minikube delete --all
minikube config set driver docker
minikube config set cpus 4
minikube config set memory 6144
minikube kubectl -- get po -A
minikube start
minikube addons enable dashboard
minikube addons enable metrics-server
minikube addons enable ingress
minikube addons enable ingress-dns
```

Insert the following lines into /etc/hosts
```
127.0.0.1 keycloak
127.0.0.1 api
127.0.0.1 api-app
```

Create the project namespace with the command `kubectl create namespace ccproject`.

#### Configuring the variables before deploying

It is necessary to configure files <PROJET_GIT_CLONE_DIR>/env/helm/infra/values.yaml and <PROJET_GIT_CLONE_DIR>/env/helm/services/values.yaml

Below are the basic structure of each of the files

*infra/values.yaml*
```yaml
# infra values
# Images
images:
  postgres_injector:
    name: postgres
    tag: "13"
  postgres_keycloak:
    name: postgres
    tag: "13"
  postgres_api:
    name: postgres
    tag: "13"
  mongodb_executor:
    name: mongo
    tag: "4.4"
  redis:
    name: redis
    tag: "7.0.7-alpine"
  minio:
    name: quay.io/minio/minio
    tag: latest
  zookeeper:
    name: docker.io/bitnami/zookeeper
    tag: "3.8"
  kafka:
    name: docker.io/bitnami/kafka
    tag: "3.3"
  keycloak:
    name: quay.io/keycloak/keycloak
    tag: "20.0"

# Services
services:
  postgres_injector:
    name: postgres-injector
    type: ClusterIP
    port: 5432
    protocol: TCP
  postgres_keycloak:
    name: postgres-keycloak
    type: ClusterIP
    port: 5432
    protocol: TCP
  postgres_api:
    name: postgres-api
    type: ClusterIP
    port: 5432
    protocol: TCP
  mongodb_executor:
    name: mongodb-executor
    type: ClusterIP
    port: 27017
    protocol: TCP
  redis:
    name: redis
    type: ClusterIP
    port: 6379
    protocol: TCP
  minio:
    name: minio
    type: ClusterIP
    port: 9000
    protocol: TCP
  zookeeper:
    name: zookeeper
    type: ClusterIP
    port: 2181
    protocol: TCP    
  kafka:
    name: kafka
    type: ClusterIP
    port: 9092
    protocol: TCP    
  keycloak:
    name: keycloak
    type: ClusterIP
    port: 8180
    protocol: TCP  

# Secrets
secrets:
  postgres_injector:
    name: postgres-injector-secret
    postgres_db: newsdb
    postgres_password: <confidential>
    postgres_user: <confidential>

  postgres_keycloak:
    name: postgres-keycloak-secret
    postgres_db: keycloak
    postgres_password: <confidential>
    postgres_user: <confidential>
  
  postgres_api:
    name: postgres-api-secret
    postgres_db: apidb
    postgres_password: <confidential>
    postgres_user: <confidential>

  mongodb_executor:
    name: mongodb-executor
    mongodb_initdb: executordb
    mongodb_initdb_password: <confidential>
    mongodb_initdb_root_password: <confidential>
    mongodb_initdb_root_username: <confidential>
    mongodb_initdb_username: <confidential>
  
  redis:
    name: redis-secret
    password: <confidential>

  minio:
    name: minio-secret
    root_user: <confidential>
    root_password: <confidential>
  
  keycloak:
    name: keycloak-secret
    keycloak_admin: <confidential>
    keycloak_admin_password: <confidential>
    
# ConfigMaps
configmaps:
  kafka:
    name: kafka-config
    allow_plaintext_listener: yes
    kafka_cfg_advertised_listeners: PLAINTEXT://kafka:9092
    kafka_cfg_auto_create_topics_enable: true
    kafka_cfg_default_replication_factor: "1"
    kafka_cfg_listeners: PLAINTEXT://:9092
    kafka_cfg_log_retention_hours: "24"
    kafka_cfg_message_max_bytes: "10485880"
    kafka_cfg_num_partitions: "3"

  keycloak:
    name: keycloak-realm-config

ingress:
  keycloak:
    name: keycloak-ingress
    host: keycloak    
```

*services/values.yaml*
```yaml
# services values

# Images
images:  
  observer:
    name: marcelodsales/observer
    tag: latest
  injector:
    name: marcelodsales/injector
    tag: latest
  api:
    name: marcelodsales/api
    tag: latest
  api_app:
    name: marcelodsales/api-app
    tag: latest
  executor:
    name: marcelodsales/executor
    tag: latest    

# Services
services:  
  injector:
    name: injector
    type: ClusterIP
    port: 8080
    protocol: TCP
  api:
    name: api
    type: ClusterIP
    port: 8080
    protocol: TCP
    forwardPort: 8083
  api_app:
    name: api-app
    type: ClusterIP
    port: 80
    protocol: TCP

# Secrets
secrets:  
  injector:
    name: injector-secret
    injector_newsdata_apikey: <confidential>
    injector_newsapi_apikey: <confidential>
    injector_mediastack_apikey: <confidential>

  api:
    name: api-secret
    api_keycloak_client_id: <confidential>
    api_keycloak_client_secret: <confidential>
  
  executor:
    name: executor-secret

    
# ConfigMaps
configmaps:
  observer:
    name: observer-config
    observer_metrics_schedule: "30s"
  
  injector:
    name: injector-config
    injector_newsdata_url: https://newsdata.io/api/1
    injector_newsapi_url: https://newsapi.org/v2
    injector_mediastack_url: http://api.mediastack.com/v1
    injector_cron: "0 * * * * ?"

  api:
    name: api-config
  
  api_app:
    name: api-app-config
    port: 80
  
  executor:
    name: executor-config

ingress:
  api:
    name: api-ingress
    host: api.127.0.0.1.nip.io
  api_app:
    name: api-app-ingress
    host: api-app.127.0.0.1.nip.io
```

**Observation** 
Although configured Ingress does not work as expected in Mac environments. In linux its behavior is different.

#### Provisioning the infrastructure
From the root where the project was cloned from, navigate to <PROJET_GIT_CLONE_DIR>/env/helm/infra
```
cd <PROJET_GIT_CLONE_DIR>/env/helm/infra
```

Run Helm to create project infrastructure
```
helm install ccproject-infra . --namespace ccproject
```

#### Provisioning the services
From the root where the project was cloned from, navigate to <PROJET_GIT_CLONE_DIR>/env/helm/services
```
cd <PROJET_GIT_CLONE_DIR>/env/helm/services
```

Run Helm to create project microservices
**Attention**: Do this process only after all the infrastructure is available. To do this, run `kubectl get pods -n ccproject` and check that all pods are in Running status.
```
helm install ccproject-services . --values ./values.yaml --values ../infra/values.yaml --namespace ccproject
```
#### To access the application create some port forwarding


Retrieve the name of all pods in the project
```
kubect get pods -n ccproject
```
Create the forwards as below
```
kubectl -n ccproject port-forward keycloak-<KEYCLOAK_POD_NAME> 8180:8180
kubectl -n ccproject port-forward api-<API_POD_NAME> 8083:8080
kubectl -n ccproject port-forward api-app-<API-APP_POD_NAME> 8091:80
```
**Note:** In the example above, the keycloak pod starts its name with keycloak- plus a random value generated when the pod is instanced. This same example applies to the other services.

Each command above requires an open session, in this case at least three open terminal sessions will be required

After these steps the applications will be available in the URLs:

* GUI - http://api-app:8091
* API - http://api:8083
* Keycloak - http://keycloak:8180

===

## Deploy in Openshift
* Openshift client

```
oc login --token=<OPENSHIFT_TOKEN> --server=<SERVER_URL>
```

**Create the project and deploy Helm**

```
oc new-project ccproject
oc create sa anyuid-sa --namespace ccproject
oc adm policy add-scc-to-user anyuid -z anyuid-sa --namespace ccproject
cd infra
helm install ccproject-infra .
cd services
helm install ccproject-services . --values ./values.yaml --values ../infra/values.yaml
```

**Updating Helm***
```
cd infra
helm upgrade ccproject-infra .
cd services
helm upgrade ccproject-services . --values ./values.yaml --values ../infra/values.yaml
```