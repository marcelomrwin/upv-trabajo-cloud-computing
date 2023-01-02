## Generate files

### Kubernetes
```shell
cd kubernetes
kompose --file ../docker-compose.yaml convert
```

### Openshift
```shell
cd openshift
kompose --provider openshift --file ../docker-compose.yaml convert
```

### Helm
```
kompose --file ../docker-compose.yaml convert -c -o .
```

It's needed adjust some files

### Helm operations
```
helm lint .
helm template .
kubectl create namespace ccproject
helm install ccproject . --namespace ccproject
helm list --namespace ccproject
helm upgrade ccproject . --namespace ccproject
helm delete ccproject --namespace ccproject
```

### Kafka Tests
```
minikube service <service-name> --url
kubectl port-forward service/kafka 9092 -n ccproject
```

### Minikube, creating a tunnel for all LoadBalancers (recommended for local tests)
```
minikube tunnel
```

### Troubleshootings

When testing with minikube and need to connect from outside the cluster, add the following line to /etc/hosts:
```shell
127.0.0.1 kafka
```