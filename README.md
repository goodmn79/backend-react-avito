# ADS-ONLINE: платформа для продажи вещей #

## Описание ## 

ADS-ONLINE — это платформа для публикации и покупки объявлений о продаже вещей.
Проект реализован в рамках дипломной работы и включает полный цикл функциональности для взаимодействия с объявлениями.

## Функциональность ##
- Регистрация и авторизация пользователей.
- Разграничение ролей: пользователь и администратор.
- Создание, редактирование и удаление объявлений и комментариев.
- Администратор может управлять всеми объявлениями и комментариями.
- Возможность загружать изображения для объявлений и аватарки пользователей.

## Технологический стек ##
- Backend: Java 11, Spring Boot, Spring Data JPA, PostgreSQL, Liquibase.
- Frontend: React (запускается через Docker).
- Безопасность: Spring Security.
- Логирование: SLF4J.
- Тестирование: JUnit, Mockito, Spring Security Test, БД H2.
- Документация API: OpenAPI (Swagger).
- Контейнеризация: Docker.

## Настройка и запуск приложения ##
1. Клонируйте репозиторий проекта:
   https://github.com/goodmn79/backend-react-avito.git

2. Создайте файл .env в корневой директории проекта и настройте переменные окружения для базы данных и каталога хранения изображений:
```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ads_db

SPRING_DATASOURCE_USERNAME=your_db_user

SPRING_DATASOURCE_PASSWORD=your_db_password

APPLICATION_IMAGES_DIRECTORY=/path/to/images
```
3. Соберите JAR-файл:
```bash
mvn clean install 
```
Запустите бэкенд-приложение:
```bash
java -jar target/ads-0.0.1-SNAPSHOT.jar
```
Фронтенд запускается через Docker:
```bash
docker run -p 3000:3000 --rm ghcr.io/dmitry-bizin/front-react-avito:v1.21
```

## Авторы ##
Powered by ©AYE.team

## Лицензия ##
Все права защищены ©AYE.team
