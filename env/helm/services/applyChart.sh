kubectl delete namespace ccproject || true
kubectl create namespace ccproject
helm install ccproject . --namespace ccproject
kubectl get all -n ccproject