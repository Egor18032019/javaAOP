
* Задание 2.

1. Изменить аспект @LogDataSourceError: 
    В первую очередь аспект должен отсылать сообщение в топик t1_demo_metrics. 
    В заголовке должен указываться тип ошибки: DATA_SOURCE; 
    В случае, если отправка не удалась - записать в БД.

2. Разработать аспект @Metric, принимающий в качестве значения время в миллисекундах. 
   Если время работы метода превышает заданное значение, 
    аспект должен отправлять сообщение в топик Kafka (t1_demo_metrics) c информацией:
            о времени работы, имени метода и параметрах метода, если таковые имеются. 
   В заголовке передать тип ошибки METRICS.

3. Реализовать 2 консьюмера, слушающих топики t1_demo_accounts и t1_demo_transactions. 
   При получении сообщения сервис должен сохранять счет и транзакцию в бд. 
4. (Консьюмер и код, сохраняющий клиента, есть в проекте) 
   В качестве ключей к сообщениям можно генерировать случайный UUID.  
