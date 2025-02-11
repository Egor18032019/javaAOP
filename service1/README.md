# Запуск

* Запустить кафку и бд
```shell
docker-compose up
```
 

# Kafka ui на http://localhost:8091

## Примеры запросов

```shell
curl -i -X POST http://127.0.0.1:8080/register -H 'Content-Type: application/json' -d '{"first_name":"firstName","last_name":"last_name","middle_name":"middle_name"}'
```

```shell
curl -i -X POST http://127.0.0.1:8080/account/create -H 'Content-Type: application/json' -d '{"client_id":"af07a493-f390-41e0-83bc-900233234031","account_type":"DEBIT","balance":"12","account_status":"OPEN"}'
```

```shell
curl -i -X POST http://127.0.0.1:8080/transaction/create -H 'Content-Type: application/json' -d '{"account_id":"87fc4431-7afe-4548-a8b5-83af7df74bfd","amount":"2","transactionTime":"2023,10,1,12,34,56"}'
```
 


в тз про тесты ничего не было