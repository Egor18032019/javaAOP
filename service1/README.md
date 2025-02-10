# Запуск

* Запустить кафку и бд
```shell
docker-compose up
```
 
Перейти в папку service1 и запустить приложение.
Перейти в папку service2 и запустить приложение.


# Kafka ui на http://localhost:8091

## Примеры запросов

```shell
curl -i -X POST http://127.0.0.1:8080/register -H 'Content-Type: application/json' -d '{"id":"1","first_name":"firstName","last_name":"last_name","middle_name":"middle_name"}'
```

```shell
curl -i -X POST http://127.0.0.1:8080/account/create -H 'Content-Type: application/json' -d '{"client_id":"1","account_type":"DEBIT","balance":"12","account_status":"OPEN"}'
```

```shell
curl -i -X POST http://127.0.0.1:8080/transaction/create -H 'Content-Type: application/json' -d '{"account_id":"4a0ecc15-6511-4d48-b288-7f43eab0c82c","amount":"2","transactionTime":"2023,10,1,12,34,56"}'
```
 


в тз про тесты ничего не было