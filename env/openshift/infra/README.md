## Install infrastructure in openshift

### Helm operations
```
helm lint .
helm template .
helm install ccproject-infra . --namespace ccproject
helm list --namespace ccproject
kubectl -n ccproject get pods
helm upgrade ccproject-infra . --namespace ccproject (optional --recreate-pods)
helm delete ccproject-infra --namespace ccproject
```

