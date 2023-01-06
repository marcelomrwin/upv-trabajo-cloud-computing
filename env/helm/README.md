# Working with Helm Chart

## Kubernetes

### Using minikube
```
minikube delete --all
minikube config set driver docker
minikube config set cpus 4
minikube config set memory 8192
minikube kubectl -- get po -A
minikube start
minikube addons enable dashboard
minikube addons enable metrics-server
minikube addons enable ingress
minikube addons enable ingress-dns
minikube addons enable logviewer

minikube addons list
```

### Creating the Namespace
```
kubectl create namespace ccproject
```

## Enable access to a single service 
```
minikube service <service-name> --url -n ccproject
kubectl port-forward service/kafka 9092 -n ccproject
```

## Access the applications
```
kubectl -n ccproject port-forward keycloak-... 8180:8180
kubectl -n ccproject port-forward api-app-... 8091:80
kubectl -n ccproject port-forward api-5fdff7b54b-8b78p 8083:8080
```

http://keycloak:8180



## Does not work :-(
To access the services outside minikube use `minikube tunnel --cleanup` and your ingress resources would be available at "127.0.0.1"


### Kubernetes operations
```
kubectl get pods -n ccproject
kubectl get svc -n ccproject
kubectl get ep -n ccproject
kubectl get secrets -n ccproject
kubectl get configmap -n ccproject
kubectl get ingress -n ccproject
kubectl -n ccproject logs <pod>
```
