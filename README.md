# Система управления задачами

## Настройка и запуск

1. **Клонируйте репозиторий:**
- git clone https://github.com/steparrik/TaskManagementSystem.git
   
2. **Настройте переменные окружения:**
- Создайте файл .env в корневой директории проекта со следующим содержимым:
- DB_NAME=your_database_name
- DB_USERNAME=your_database_username
- DB_PASSWORD=your_database_password
- DB_URL=jdbc:postgresql://postgres:5432/your_database_name 
- SERVER_PORT=8080
- JWT_SECRET=your_jwt_secret
- JWT_LIFE=3600

3. **Соберите проект:**
- mvn clean package

4. **Запустите docker-compose:**
- docker-compose up --build

5. **Доступ к API документации:**
- Swagger UI доступен по следующему адресу:
- http://localhost:SERVER_PORT/swagger-ui/index.html
