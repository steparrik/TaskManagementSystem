FROM maven:3.8.5-openjdk-17 AS build
COPY src /app/src
COPY pom.xml /app
WORKDIR /app
RUN mvn clean package -DskipTests
FROM openjdk:17
ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]