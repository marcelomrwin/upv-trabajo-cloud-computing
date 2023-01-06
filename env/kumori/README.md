https://github.com/kumori-systems/documentation
https://github.com/kumori-systems/documentation/blob/master/01-getting-started/manual.adoc


Download binary https://gitlab.com/kumori-systems/community/tools/kumorictl/-/releases
Or use the docker image `docker pull kumoripublic/kumorictl`

Iniciando o workspace

```
kumorictl init
kumorictl config -a admission-forge.vera.kumori.cloud
kumorictl login u: mddasil p: Y7776123
kumorictl get all
```

### Criando volumes
```
kumorictl register volume postgres-keycloak-data --items 3 --size 100Mi --type persistent
```


## USANDO KAM
```
npm install @kumori/kam
kam mod init vera.cloud/ccproject

https://gitlab.com/kumori-systems/community/examples/sharded-volumes/-/blob/master/manifests/deployment/manifest.cue
https://gitlab.com/kumori-systems/community/tools/kumorictl
https://gitlab.com/kumori-systems/community/examples/service-configuration