# Working with Helm Chart

### Generate from docker-compose (only skeleton)
```
kompose --file ../docker-compose.yaml convert -c -o .
```

*IMPORTANT*
It's needed adjust some files


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
minikube addons enable logviewer

minikube addons list
```

## Enable access to a single service 
```
minikube service <service-name> --url -n ccproject
kubectl port-forward service/kafka 9092 -n ccproject
```

### Minikube, creating a tunnel for all LoadBalancers (recommended for local tests)
```
minikube tunnel --cleanup
```

To access the services outside minikube use `minikube tunnel --cleanup` and your ingress resources would be available at "127.0.0.1"

### Creating the Namespace
```
kubectl create namespace ccproject
```

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
