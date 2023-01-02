## Start services of the project

### Helm operations
```
helm lint . --values ./values.yaml --values ../infra/values.yaml
helm template . --values ./values.yaml --values ../infra/values.yaml
helm install ccproject-services . --values ./values.yaml --values ../infra/values.yaml --namespace ccproject
helm list --namespace ccproject
helm upgrade ccproject-services . --values ./values.yaml --values ../infra/values.yaml --namespace ccproject (optional: --recreate-pods)
helm delete ccproject-services --namespace ccproject
```