## Troubleshooting with Openshift

```
oc status --suggest
oc describe pod/api-app-69b458d76-8gqvh | grep -i SCC
oc get pod/api-app-69b458d76-8gqvh -o yaml | oc adm policy scc-subject-review -f -
oc create sa anyuid-sa --namespace ccproject
oc adm policy add-scc-to-user anyuid -z anyuid-sa --namespace ccproject
oc set serviceaccount deployment api-app anyuid-sa