### RESTfullProxyService

REST API сервис, который перенаправляет запросы на https://jsonplaceholder.typicode.com
с использованием Spring Framework.

1. Были реализованы 3 контроллера, проксирующие запросы на https://jsonplaceholder.typicode.com
- [/api/posts/**](./src/main/java/org/example/controller/PostController.java)
- [/api/users/**](./src/main/java/org/example/controller/UserController.java)
- [/api/albums/**](./src/main/java/org/example/controller/AlbumController.java)

2. Реализована базовая авторизация с ролевой моделью (7 ролей)
- ROLE_ADMIN -- имеет доступ ко всем обработчикам
- ROLE_POSTS_VIEWER -- имеет доступ к GET запросу /posts/**
- ROLE_USERS_VIEWER - имеет доступ к GET запросу /users/**
- ROLE_ALBUMS_VIEWER - имеет доступ к GET запросу /albums/**
- ROLE_POSTS_EDITOR - имеет доступ к обработчикам /posts/**
- ROLE_USERS_EDITOR - имеет доступ к обработчикам /users/**
- ROLE_ALBUMS_EDITOR - имеет доступ к обработчикам /albums/**  
[Файл конфигурации](./src/main/java/org/example/config/SecurityConfig.java)

3. Реализован аудит с сохранением в базу данных с ипользованием Spring AOP  
[Аудит](./src/main/java/org/example/audit/AuditAspect.java)  
[Конфигурация базы данных](./src/main/resources/application.properties)
4. Все GET запросы кэшируются, также при DELETE запросе данные очищатся к кэше
5. Запуск приложения с помощью maven:  
   Настроить свой свободный порт в конфигурационном файле:  
   [application.properties](./src/main/resources/application.properties)
   в поле:
```yaml
server.port=8080
```

Сборка проекта с помощью Maven:

```bash
mvn clean install
```

Запуск приложения с помощью команды:

```bash
java -jar target/T1TestTask-0.0.1-SNAPSHOT.jar
```
6. Пользователи и их роли хранятся в базе данных.  
Для их создания реализован [UserCreateController](./src/main/java/org/example/controller/UserCreateController.java)
и [RoleController](./src/main/java/org/example/controller/RoleController.java)
7. Написанны [тесты](./src/test/java/org/example) для контроллеров, проксирущих запросы.
8. Реализована конечная точка для запросов по [websocket](./src/main/java/org/example/webSocket)