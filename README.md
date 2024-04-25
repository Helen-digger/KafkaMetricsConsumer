# Домашнее задание по курсу открытой школы "Холдинг Т1"
## Реализация системы мониторинга с использованием Spring Kafka (Producer)

### Что было сделано
1. Написан Kafka Consumer для чтения метрик из кафки.
2. Создана модель данных для хранения метрик на стороне сервиса с помощью Spring Data JPA.
3. Реализованы репозитории для хранения и получения метрик.
4. Добавлено кэширование запросов в БД, а также инвалидация при изменении данных. В качестве кэша используется ConcurrentMapCache.
5. Добавлен сервис для сохранения и получения метрик.
6. Добавлен контроллер для получения списка метрик и получения метрики по идентификатору.
7. Добавлена валидация сущностей на уровне контроллера и на уровне модели данных.
8. Добавлены модульные тесты на основе SpringBootTest и TestContainers.
9. Добавлен swagger-ui для описания контрактов API.

### How-to
1. Для того чтобы поднять сервис требуется наличие Java 17 и Docker на целевой системе.
2. Убедиться, что порт 8086 доступен и не занят другими сервисами. Порт можно сменить в `application.properties`.
3. Развернуть сервис консьюмер для анализа метрик. [kafka-metrics-produer](https://github.com/Helen-digger/KafkaMetricsProduce)
4. Выполнить команду ```./gradlew bootRun``` в командной строке
5. Перейти в браузере по адресу `http://localhost:8085/swagger-ui`
6. С помощью swagger-ui выполнить метод `/metrics` и получить список метрик. Если метрик нет, небходимо добавить её через `kafka-metrics-producer`.
7. С помощью swagger-ui выполинить метод `/metrics/{id}` для определенной метрики и получить текущие показания. Выводится rate, как частота с которой происходит приращение значения, за последние 5 минут, что полезно для оценки повышения или понижения нагрузки. Также, выводится максимальный уровень метрики за 5 минут, для оценки здоровья метрики. Сторонний сервис может считывать данную метрику и установить механизм алертинга, чтобы сигнализировать об отставании от SLO. 
8. Тесты можно запустить с помощью команды ```./gradlew test```