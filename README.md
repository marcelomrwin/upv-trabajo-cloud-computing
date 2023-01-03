# Máster Universitario en Computación en la Nube y de Altas Prestaciones

## Trabajo cloud computing

### Instructions to play

Clone this repo
```
git clone https://github.com/marcelomrwin/ccproject
```

### Test with development environment
* IDE [Intellij, Vscode, Eclipse]
* Java 17
* Apache Maven
* Container CLI [Docker, Podman]
* REST Client [Postman, Insomnia ]





### Run in local environment with Minikube
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
helm install ccproject-services . --values ./values.yaml --values ../infra/values.yaml
```