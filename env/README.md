```shell
docker-compose down && docker-compose pull && docker-compose --env-file .env up
```

## Services
* Kafka UI http://localhost:9080/
* MinIO Console http://localhost:9001/ (admin/password)
* MongoDB Express http://localhost:9081/ (admin/pass)
* PgAdmin http://localhost:9082/
* RedisInsight http://localhost:9085/
* Keycloak http://localhost:8180/ (admin/admin)

## Managing Kafka
```json
{
  "partitions": [
    {
      "topic": "job-response",
      "partition": 0,
      "offset": -1
    }
  ],
  "version": 1
}
```

## Important, change kafka message size
```shell
./kafka-configs.sh --bootstrap-server localhost:19092 \
                --alter --entity-type topics \
                --entity-name job-response \
                --add-config max.message.bytes=10485880
```


```shell
./kafka-topics.sh --bootstrap-server localhost:19092 --describe --topic job-response
./kafka-configs.sh --alter --bootstrap-server localhost:19092 --entity-type topics --entity-name job-response --add-config retention.ms=43200000 (12 hours)
./kafka-configs.sh --alter --bootstrap-server localhost:19092 --entity-type topics --entity-name job-response --add-config retention.ms=86400000 (24 hours)
```

```shell
docker-compose down && docker-compose pull && docker-compose --env-file .env up
```

## Troubleshooting
When trying to start services and the services freezes, normally is something related to volumes. Try remove untagged volumes using `docker volume ls` and `docker volume rm`