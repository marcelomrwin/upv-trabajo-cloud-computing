## Start Infrastructure for the project

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

### Troubleshootings

When testing with minikube and need to connect from outside the cluster, add the following line to /etc/hosts:
```shell
127.0.0.1 kafka
```

On macos, ingress does not work as mentioned, you have to create tunnels per application. To access keycloak, for example:
```
kubectl get svc keycloak -n ccproject -o wide
minikube service keycloak --url -n ccproject
```

To access it, the `keycloak` host must be inserted in the `/etc/hosts` file pointing to `127.0.0.1`.

Use the TUNNEL_PORT to access from localhost