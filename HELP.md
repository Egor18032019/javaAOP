Создать бд 'demo' через интерфейс или через докер как показано ниже

```shell
docker run -it --name t1_demo -p 5432:5432 -e POSTGRES_PASSWORD=0000 -e POSTGRES_DB=t1_demo -d postgres:11-alpine
```

Запросы;

```shell
curl -i -X GET http://127.0.0.1:8080/client 
```

 ```shell
curl -i -X GET http://localhost:8080/transaction/100001
```
 ```shell
curl -i -X GET http://localhost:8080/account/100000
```
```shell
curl -i -X POST http://127.0.0.1:8080/register -H 'Content-Type: application/json' -d '{"id":"15","first_name":"firstName","last_name":"last_name","middle_name":"middle_name"}'
```

```shell
curl -i -X POST http://127.0.0.1:8080/account/create -H 'Content-Type: application/json' -d '{"client_id":"1234","account_type":"DEBIT","balance":"1"}'
```

```shell
curl -i -X POST http://127.0.0.1:8080/transaction/create -H 'Content-Type: application/json' -d '{"account_id":"123","amount":"2","transaction_time":"2023-10-01T12:34:56"}'
```
подключиться к бд из контейнера
docker exec -it 185f767425dd bash
psql --username=postgres --dbname=t1_demo
\connect t1_demo
SELECT * FROM databasechangeloglock;
SELECT * FROM datasource_error_log;
SELECT * FROM client;
SELECT * FROM transactions;

INSERT INTO datasource_error_log (id,stack_trace, message, method_signature)VALUES (100000,'текст стектрейса', 'текст сообщения', 'сигнатура метода');
INSERT INTO transactions (id,account_id, amount, transaction_time)
VALUES (100000,123456789, 1500.50, '2023-10-15 12:34:56');