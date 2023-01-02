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
