# Etapa 1: build do app
FROM maven:3.9.9-eclipse-temurin-23 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: imagem final
FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p /opt/uploads && chmod -R 777 /opt/uploads
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]