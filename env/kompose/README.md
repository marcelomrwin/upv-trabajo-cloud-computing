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