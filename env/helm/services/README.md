## Start services of the project

### Helm operations
```
helm lint .
helm template .
helm install ccproject-services . --namespace ccproject
helm list --namespace ccproject
helm upgrade ccproject-services . --namespace ccproject
helm delete ccproject-services --namespace ccproject
```