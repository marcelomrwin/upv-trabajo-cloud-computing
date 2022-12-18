```shell
docker-compose -f docker-compose-dev.yaml down && docker-compose -f docker-compose-dev.yaml pull && docker-compose -f docker-compose-dev.yaml up
docker-compose -f docker-compose-dev.yaml down --rmi all && docker-compose -f docker-compose-dev.yaml up
docker-compose -f docker-compose-dev.yaml down --rmi all && docker-compose -f docker-compose-dev.yaml pull && docker-compose -f docker-compose-dev.yaml up
```

```shell
docker-compose -f docker-compose-dev.yaml down && docker-compose -f docker-compose-dev.yaml pull && docker-compose -f docker-compose-dev.yaml up
```