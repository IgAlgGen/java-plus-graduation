# Explore With Me

Микросервисное приложение для публикации событий, подачи заявок на участие, управления категориями, подборками, комментариями и сбора статистики просмотров.

Проект построен как набор Spring Boot сервисов за API Gateway. Сервисы регистрируются в Eureka, получают общую конфигурацию из Spring Cloud Config Server и взаимодействуют между собой по HTTP через OpenFeign или внутренний клиент статистики.

## Описание проекта

Explore With Me решает задачи событийной платформы:

- пользователи создают события и управляют своими событиями;
- публичные клиенты ищут опубликованные события, категории, подборки и комментарии;
- администраторы управляют пользователями, категориями, событиями, подборками и модерацией комментариев;
- участники подают заявки на события, а инициаторы подтверждают или отклоняют заявки;
- отдельный stats-service сохраняет обращения к API и возвращает агрегированную статистику просмотров.

Архитектурный подход: микросервисы с отдельными базами данных, service discovery через Eureka, централизованная конфигурация через Config Server, входной HTTP-трафик через Gateway.

## Архитектура

### Сервисы

| Service | Module | Назначение |
| --- | --- | --- |
| `gateway-server` | `infra/gateway-server` | Единая входная точка для внешних REST API. Маршрутизирует запросы в core-сервисы через Eureka и `lb://...` routes. |
| `discovery-server` | `infra/discovery-server` | Eureka Server. Хранит реестр запущенных сервисов. |
| `config-server` | `infra/config-server` | Spring Cloud Config Server в `native` profile. Отдает конфигурацию из `infra/config-server/src/main/resources/config`. |
| `user-service` | `core/user-service` | Пользователи: административное создание, чтение, удаление; внутренний API проверки и получения пользователей. |
| `event-service` | `core/event-service` | События: создание, обновление, публичный и административный поиск, публикация, получение просмотров и подтвержденных заявок. |
| `request-service` | `core/request-service` | Заявки на участие: создание, отмена, подтверждение/отклонение, внутренние счетчики подтвержденных заявок. |
| `feature-service` | `core/feature-service` | Дополнительные функции: категории, подборки событий, комментарии и модерация комментариев. |
| `stats-server` | `stats-service/stats-server` | Сервис статистики: сохраняет hit'ы и возвращает агрегированные просмотры. |
| `stats-client` | `stats-service/stats-client` | Библиотечный модуль клиента статистики для `event-service`. |
| `stats-dto` | `stats-service/stats-dto` | Общие DTO статистики. |
| `internal-api` | `core/internal-api` | Общие DTO и пути внутреннего HTTP API между сервисами. |

### Взаимодействие

Внешний клиент работает через Gateway:

```text
Client -> gateway-server:8080 -> Eureka lb://service-name -> target service
```

Внутренние взаимодействия:

- `event-service -> user-service` через OpenFeign: проверка и получение пользователей.
- `event-service -> feature-service` через OpenFeign: проверка и получение категорий.
- `event-service -> request-service` через OpenFeign: счетчики подтвержденных заявок.
- `event-service -> stats-server` через `stats-client`, `DiscoveryClient`, `RestTemplate`, retry и Resilience4j CircuitBreaker.
- `request-service -> user-service` через OpenFeign: проверка пользователя.
- `request-service -> event-service` через OpenFeign: получение данных события.
- `feature-service -> user-service` через OpenFeign: проверка автора комментария.
- `feature-service -> event-service` через OpenFeign: проверка событий и получение кратких данных событий для подборок.


## Технологический стек

| Технология | Где используется |
| --- | --- |
| Java 21 | Все Maven-модули и Docker images на Temurin 21. |
| Spring Boot 3.3.0 | Бизнес-сервисы, stats-server, infra-сервисы. |
| Spring Cloud 2023.0.3 | Gateway, Config Server, Eureka, OpenFeign. |
| Spring Cloud Gateway | `gateway-server`. |
| Netflix Eureka | `discovery-server` и discovery clients. |
| Spring Cloud Config | `config-server` и clients. |
| OpenFeign | HTTP-взаимодействие core-сервисов. |
| PostgreSQL 16.1 | Docker БД для user/event/request/feature/stats сервисов. |
| H2 | Test profile в сервисах. |
| Spring Data JPA / Hibernate | Доступ к БД. |
| Querydsl 5 | Динамический поиск событий в `event-service`. |
| Resilience4j + Spring Retry | Устойчивость вызовов статистики в `event-service` и `stats-client`. |
| Docker / Docker Compose | Локальный запуск инфраструктуры и сервисов. |
| Maven | Multi-module build. |
| Lombok | DTO, entities, services. |

В проекте есть статические OpenAPI 3.0.1 спецификации в директории `openAPI`. Спецификации можно просмотреть через любой OpenAPI viewer или запустить локальный Swagger UI контейнер.

## Структура проекта

```text
.
├── core
│   ├── internal-api       # Общие внутренние DTO и ApiPaths
│   ├── user-service       # Пользователи
│   ├── event-service      # События и статистика просмотров событий
│   ├── request-service    # Заявки на участие
│   └── feature-service    # Категории, подборки, комментарии
├── infra
│   ├── discovery-server   # Eureka Server
│   ├── config-server      # Spring Cloud Config Server
│   └── gateway-server     # Spring Cloud Gateway
├── stats-service
│   ├── stats-dto          # DTO статистики
│   ├── stats-client       # Клиент stats-server
│   └── stats-server       # API и БД статистики
├── openAPI                # Статические OpenAPI спецификации основного API и stats API
├── postman                # Postman-коллекции
├── docker-compose.yml
└── pom.xml                # Корневой Maven reactor
```

Каждый запускаемый сервис имеет собственный `Dockerfile`. SQL-схемы лежат в `src/main/resources/schema.sql` соответствующего сервиса.

## Требования для запуска

- Java 21.
- Maven 3.9+.
- Docker.
- Docker Compose v2 (`docker compose`, не обязательно отдельный `docker-compose` binary).
- Свободные host-порты `8080` и `8761`.
- Доступ к Docker Hub для образов `postgres:16.1`, `maven:3.9.9-eclipse-temurin-21`, `eclipse-temurin:21-jre`.

## Запуск

### 1. Клонировать проект

```bash
git clone <repository-url>
cd java-plus-graduation
```

### 2. Проверить сборку

```bash
mvn clean package
```

Для быстрого локального билда без тестов:

```bash
mvn clean package -DskipTests
```

### 3. Запустить все сервисы через Docker Compose

```bash
docker compose up --build
```

Порядок запуска в compose:

1. `discovery-server`
2. `config-server`
3. PostgreSQL контейнеры
4. `user-service`, `feature-service`, `stats-server`
5. `event-service`, `request-service`
6. `gateway-server`

Часть сервисов зависит от `service_started`, а не от полной готовности приложения. Если Gateway сразу после старта возвращает 503, подождите регистрацию сервисов в Eureka и повторите запрос.

### 4. Проверить работоспособность

Eureka UI:

```text
http://localhost:8761
```

Gateway health:

```bash
curl http://localhost:8080/actuator/health
```

Пример публичного endpoint'а через Gateway:

```bash
curl "http://localhost:8080/categories?from=0&size=10"
```

Если сервисы еще не зарегистрировались, запрос может временно вернуть `503 Service Unavailable`.

## Docker

Запуск с пересборкой:

```bash
docker compose up --build
```

Запуск в фоне:

```bash
docker compose up -d --build
```

Остановка контейнеров:

```bash
docker compose down
```

Остановка с удалением volumes:

```bash
docker compose down -v
```

В compose нет именованных volumes, но образ PostgreSQL создает volume для каталога данных. `down -v` удалит эти данные.

Пересборка конкретного сервиса:

```bash
docker compose build event-service
docker compose up -d event-service
```

Логи всех сервисов:

```bash
docker compose logs -f
```

Логи конкретного сервиса:

```bash
docker compose logs -f gateway-server
```

Состояние контейнеров:

```bash
docker compose ps
```

## Конфигурация

### Где хранится конфигурация

- Локальные bootstrap-настройки сервисов: `*/src/main/resources/application.yaml`.
- Конфигурация Config Server: `infra/config-server/src/main/resources/application.yaml`.
- Конфиги, которые раздает Config Server: `infra/config-server/src/main/resources/config/*.properties`.
- SQL init: `schema.sql` в каждом сервисе с БД.

Config Server работает в `native` profile и читает конфиги из classpath-директории `/config`.

### Важные особенности

- В конфигурации core-сервисов по умолчанию задан `server.port=0`; в `docker-compose.yml` эти порты переопределяются через `SERVER_PORT`, чтобы Docker мог проверять `/actuator/health`. С host-машины core-сервисы всё равно доступны через Gateway, а не напрямую.
- Для `stats-server` порт фиксируется в конфигурации Config Server: `server.port=9090`. Это важно, потому что удаленная конфигурация из `infra/config-server/src/main/resources/config/stats-server.properties` имеет приоритет над локальным `stats-service/stats-server/src/main/resources/application.properties`.
- В `docker-compose.yml` Config Server получает `SERVER_PORT=8888`, но порт `8888` не опубликован наружу. Он используется внутри docker-сети.
- Gateway опубликован на host-порту `8080`.
- Eureka опубликована на host-порту `8761`.
- Базы PostgreSQL не опубликованы наружу; они доступны сервисам по docker DNS именам.

### Переменные окружения в docker-compose

| Variable | Где используется | Назначение |
| --- | --- | --- |
| `SERVER_PORT` | app-сервисы и `config-server` | Фиксирует внутренний порт контейнера в compose; нужен для `/actuator/health` healthcheck. |
| `SPRING_CONFIG_IMPORT` | все app-сервисы кроме Eureka | URL Config Server: `optional:configserver:http://config-server:8888`. |
| `SPRING_CLOUD_CONFIG_DISCOVERY_ENABLED` | app-сервисы | В compose отключает discovery lookup Config Server и использует прямой URL. |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | app-сервисы и Config Server | URL Eureka: `http://discovery-server:8761/eureka/`. |
| `SPRING_DATASOURCE_URL` | сервисы с БД | JDBC URL PostgreSQL в docker-сети. |
| `SPRING_DATASOURCE_USERNAME` | сервисы с БД | Пользователь БД. |
| `SPRING_DATASOURCE_PASSWORD` | сервисы с БД | Пароль БД. |
| `STATS_SERVICE_ID` | `event-service` | Имя stats-service в Eureka. |
| `USER_SERVICE_ID` | `event-service`, `request-service`, `feature-service` | Имя user-service для Feign. |
| `EVENT_SERVICE_ID` | `request-service`, `feature-service` | Имя event-service для Feign. |
| `REQUEST_SERVICE_ID` | `event-service` | Имя request-service для Feign. |
| `FEATURE_SERVICE_ID` | `event-service` | Имя feature-service для Feign. |

## Таблица портов

| Service | Port | Description |
| --- | --- | --- |
| `gateway-server` | `8080` host/container | Внешняя точка входа для REST API и actuator. |
| `discovery-server` | `8761` host/container | Eureka UI и registry. |
| `config-server` | `8888` container only | Config Server внутри docker-сети; host port не опубликован. |
| `user-service` | `8091` container only | Порт задан через `SERVER_PORT` в compose; внешний доступ идет через Gateway/Eureka. |
| `event-service` | `8092` container only | Порт задан через `SERVER_PORT` в compose; внешний доступ идет через Gateway/Eureka. |
| `request-service` | `8093` container only | Порт задан через `SERVER_PORT` в compose; внешний доступ идет через Gateway/Eureka. |
| `feature-service` | `8094` container only | Порт задан через `SERVER_PORT` в compose; внешний доступ идет через Gateway/Eureka. |
| `stats-server` | `9090` container only | Порт задан в Config Server; host port не опубликован, внешний доступ идет через Gateway. |
| `users-db` | `5432` container only | PostgreSQL БД `ewm-users`. |
| `events-db` | `5432` container only | PostgreSQL БД `ewm-events`. |
| `requests-db` | `5432` container only | PostgreSQL БД `ewm-requests`. |
| `feature-db` | `5432` container only | PostgreSQL БД `ewm-feature`. |
| `stats-db` | `5432` container only | PostgreSQL БД `stats`. |

## API Documentation

В проекте есть готовые OpenAPI 3.0.1 спецификации:

| File | API | Server URL в спецификации | Комментарий |
| --- | --- | --- | --- |
| `openAPI/ewm-main-service-spec.json` | Основной API Explore With Me | `http://localhost:8080` | Описывает Gateway-facing API пользователей, событий, заявок, категорий и подборок. |
| `openAPI/ewm-stats-service-spec.json` | Stats API | `http://localhost:9090` | Описывает `/hit` и `/stats` stats-service. В docker-compose эти endpoint'ы также доступны через Gateway на `http://localhost:8080`. |

Посмотреть спецификации можно через любой OpenAPI viewer, например Swagger Editor:

1. Открыть https://editor.swagger.io/.
2. Выполнить `File -> Import file`.
3. Выбрать `openAPI/ewm-main-service-spec.json` или `openAPI/ewm-stats-service-spec.json`.

Локально можно быстро поднять Swagger UI контейнер для одной спецификации:

```bash
docker run --rm -p 8088:8080 \
  -e SWAGGER_JSON=/spec/ewm-main-service-spec.json \
  -v "$PWD/openAPI:/spec" \
  swaggerapi/swagger-ui
```

После запуска UI будет доступен на `http://localhost:8088`.

Основные внешние API доступны через Gateway на `http://localhost:8080`.

### Gateway routes

| Route | Target service |
| --- | --- |
| `/admin/users/**` | `user-service` |
| `/events`, `/events/**`, `/admin/events/**`, `/users/*/events/**` | `event-service` |
| `/users/*/requests`, `/users/*/requests/**`, `/users/*/events/*/requests` | `request-service` |
| `/categories/**`, `/admin/categories/**` | `feature-service` |
| `/compilations/**`, `/admin/compilations/**` | `feature-service` |
| `/comments/**`, `/admin/comments/**` | `feature-service` |
| `/events/*/comments` | `feature-service` |
| `/admin/users/*/events/*/comments`, `/admin/users/*/comments/*` | `feature-service` |
| `/hit`, `/stats` | `stats-server` |

### Основные endpoint'ы

Пользователи:

| Method | Path | Service |
| --- | --- | --- |
| `POST` | `/admin/users` | `user-service` |
| `GET` | `/admin/users` | `user-service` |
| `DELETE` | `/admin/users/{userId}` | `user-service` |

События:

| Method | Path | Service |
| --- | --- | --- |
| `GET` | `/events` | `event-service` |
| `GET` | `/events/{id}` | `event-service` |
| `POST` | `/users/{userId}/events` | `event-service` |
| `GET` | `/users/{userId}/events` | `event-service` |
| `GET` | `/users/{userId}/events/{eventId}` | `event-service` |
| `PATCH` | `/users/{userId}/events/{eventId}` | `event-service` |
| `GET` | `/admin/events` | `event-service` |
| `PATCH` | `/admin/events/{eventId}` | `event-service` |

Заявки:

| Method | Path | Service |
| --- | --- | --- |
| `POST` | `/users/{userId}/requests?eventId={eventId}` | `request-service` |
| `GET` | `/users/{userId}/requests` | `request-service` |
| `PATCH` | `/users/{userId}/requests/{requestId}/cancel` | `request-service` |
| `GET` | `/users/{userId}/events/{eventId}/requests` | `request-service` |
| `PATCH` | `/users/{userId}/events/{eventId}/requests` | `request-service` |

Категории, подборки, комментарии:

| Method | Path | Service |
| --- | --- | --- |
| `GET` | `/categories` | `feature-service` |
| `GET` | `/categories/{catId}` | `feature-service` |
| `POST` | `/admin/categories` | `feature-service` |
| `PATCH` | `/admin/categories/{catId}` | `feature-service` |
| `DELETE` | `/admin/categories/{catId}` | `feature-service` |
| `GET` | `/compilations` | `feature-service` |
| `GET` | `/compilations/{compId}` | `feature-service` |
| `POST` | `/admin/compilations` | `feature-service` |
| `PATCH` | `/admin/compilations/{compId}` | `feature-service` |
| `DELETE` | `/admin/compilations/{compId}` | `feature-service` |
| `GET` | `/events/{eventId}/comments` | `feature-service` |
| `GET` | `/comments` | `feature-service` |
| `POST` | `/admin/users/{userId}/events/{eventId}/comments` | `feature-service` |
| `PATCH` | `/admin/users/{userId}/comments/{commentId}` | `feature-service` |
| `DELETE` | `/admin/users/{userId}/comments/{commentId}` | `feature-service` |
| `PATCH` | `/admin/comments/{commentId}/approve` | `feature-service` |
| `PATCH` | `/admin/comments/{commentId}/reject` | `feature-service` |

## Межсервисное взаимодействие

### HTTP/OpenFeign

Внутренние HTTP endpoint'ы описаны в `core/internal-api/src/main/java/ru/practicum/ewm/internal/ApiPaths.java`.

Примеры внутренних путей:

| Path | Назначение |
| --- | --- |
| `/internal/users/{userId}` | Получить пользователя. |
| `/internal/users/batch` | Получить пользователей пачкой. |
| `/internal/users/exists` | Проверить существование пользователей. |
| `/internal/events/{eventId}` | Получить событие. |
| `/internal/events/batch` | Получить события пачкой. |
| `/internal/events/exists` | Проверить существование событий. |
| `/internal/events/short` | Получить краткие данные событий. |
| `/internal/events/categories/{categoryId}/exists` | Проверить, есть ли события в категории. |
| `/internal/requests/confirmed-counts` | Получить число подтвержденных заявок по событиям. |
| `/internal/requests/events/{eventId}` | Получить заявки события. |
| `/internal/requests/users/{userId}/events/exists` | Проверить заявки пользователя на события. |
| `/internal/categories/{categoryId}` | Получить категорию. |
| `/internal/categories/batch` | Получить категории пачкой. |
| `/internal/categories/exists` | Проверить существование категорий. |

### Service Discovery

Все app-сервисы регистрируются в Eureka. Gateway и Feign-клиенты используют имена сервисов:

- `user-service`
- `event-service`
- `request-service`
- `feature-service`
- `stats-server`
- `config-server`

## Базы данных

| Docker service | Database | User | Application service | Schema file |
| --- | --- | --- | --- | --- |
| `users-db` | `ewm-users` | `users` | `user-service` | `core/user-service/src/main/resources/schema.sql` |
| `events-db` | `ewm-events` | `events` | `event-service` | `core/event-service/src/main/resources/schema.sql` |
| `requests-db` | `ewm-requests` | `requests` | `request-service` | `core/request-service/src/main/resources/schema.sql` |
| `feature-db` | `ewm-feature` | `feature` | `feature-service` | `core/feature-service/src/main/resources/schema.sql` |
| `stats-db` | `stats` | `stats` | `stats-server` | `stats-service/stats-server/src/main/resources/schema.sql` |

Схемы инициализируются через bind mount в `/docker-entrypoint-initdb.d/01-schema.sql`. Данные PostgreSQL хранятся во внутренних volumes контейнеров PostgreSQL; явные named volumes в `docker-compose.yml` не объявлены.

## Локальная разработка

Полная сборка:

```bash
mvn clean test
```

Сборка одного сервиса с зависимостями:

```bash
mvn clean package -pl core/event-service -am
```

Запуск тестового профиля использует H2 и отключает Eureka:

```bash
mvn test
```

Корневой `pom.xml` содержит профили:

```bash
mvn verify -Pcheck
mvn verify -Pcoverage
```

Практический нюанс: для запуска отдельного business-сервиса без Docker нужны доступные Eureka, Config Server и PostgreSQL либо собственные override-переменные/профили. Для unit/integration тестов используется `test` profile.

## Troubleshooting

### Сервис не регистрируется в Eureka

Проверьте:

```bash
docker compose logs -f discovery-server
docker compose logs -f <service-name>
```

Типичные причины:

- `discovery-server` еще не стал healthy;
- неверный `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`;
- сервис не получил конфигурацию от `config-server`;
- контейнер сервиса упал до регистрации.

Откройте Eureka UI:

```text
http://localhost:8761
```

### Gateway возвращает 503

`503` обычно означает, что Gateway не видит целевой сервис в Eureka.

Что проверить:

```bash
docker compose logs -f gateway-server
docker compose logs -f event-service
docker compose logs -f feature-service
```

Также проверьте, что нужный сервис появился в Eureka UI. После `docker compose up` регистрация может занять некоторое время.

### Config Server недоступен

В compose Config Server доступен внутри сети как:

```text
http://config-server:8888
```

Host-порт `8888` не опубликован. Проверяйте из логов контейнеров:

```bash
docker compose logs -f config-server
```

Если нужно диагностировать вручную, временно добавьте port mapping в compose или выполните запрос из контейнера в той же docker-сети.

### Проблемы подключения к PostgreSQL

Проверьте health БД:

```bash
docker compose ps
docker compose logs -f users-db
```

Типичные причины:

- сервис стартовал раньше полной готовности БД;
- изменили `schema.sql`, но старая БД уже была инициализирована;
- неверные `SPRING_DATASOURCE_*` переменные.

Для полного пересоздания БД:

```bash
docker compose down -v
docker compose up --build
```

### Docker не пересобрал изменения

Пересоберите без cache:

```bash
docker compose build --no-cache
docker compose up
```

### Stats-service не отвечает

Gateway маршрутизирует `/hit` и `/stats` в `stats-server` через `lb://stats-server`. Проверьте, что `stats-server` зарегистрирован в Eureka, получил конфигурацию от Config Server.

```bash
docker compose logs -f stats-server
docker compose logs -f event-service
docker compose logs -f gateway-server
```

Примеры внешних запросов через Gateway:

```bash
curl -X POST http://localhost:8080/hit \
  -H "Content-Type: application/json" \
  -d '{"app":"ewm-main-service","uri":"/events/1","ip":"127.0.0.1","timestamp":"2026-05-07 12:00:00"}'

curl "http://localhost:8080/stats?start=2026-05-07%2000:00:00&end=2026-05-08%2000:00:00"
```

## Known Issues / Ограничения

- OpenAPI спецификации лежат статическими файлами в `openAPI`, но Swagger UI и `/v3/api-docs` в приложении не настроены.
- Kafka и gRPC отсутствуют, несмотря на микросервисную архитектуру.
- Business-сервисы в compose запускаются на фиксированных внутренних портах для `/actuator/health`, но эти порты не опубликованы наружу; внешний доступ идёт через Gateway.
- `config-server` в compose слушает `8888`, но порт не опубликован наружу.
- App-сервисы в compose ожидают зависимости через `condition: service_healthy` с проверкой `/actuator/health`.
