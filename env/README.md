```shell
docker-compose -f docker-compose-dev.yaml down && docker-compose -f docker-compose-dev.yaml pull && docker-compose -f docker-compose-dev.yaml up
docker-compose -f docker-compose-dev.yaml down --rmi all && docker-compose -f docker-compose-dev.yaml up
docker-compose -f docker-compose-dev.yaml down --rmi all && docker-compose -f docker-compose-dev.yaml pull && docker-compose -f docker-compose-dev.yaml up
```

```shell
docker-compose -f docker-compose-dev.yaml down && docker-compose -f docker-compose-dev.yaml pull && docker-compose -f docker-compose-dev.yaml --env-file .env.dev up
```

```shell
docker-compose down && docker-compose pull && docker-compose --env-file .env.dev up
```

## Services
* Kafka UI http://localhost:9080/
* MinIO Console http://localhost:9001/
* MongoDB Express http://localhost:9081/
* PgAdmin http://localhost:9082/
* Kafdrop http://localhost:9083/
* RedisInsight http://localhost:9085/