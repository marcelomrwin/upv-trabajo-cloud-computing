## Generate files

### Generate from docker-compose (only skeleton)
```
kompose --file ../docker-compose.yaml convert -c -o .
```

*IMPORTANT*
It's needed adjust some files

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