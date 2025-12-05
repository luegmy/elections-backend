# Etapa de construcción (Build Stage)
FROM maven:3.8.7-eclipse-temurin-17 AS build
# Copia el código fuente
COPY . /app
WORKDIR /app
# Construye el proyecto y genera el JAR
RUN mvn clean package -DskipTests

# Etapa final (Production Stage)
FROM eclipse-temurin:17-jre-jammy
# Establece el puerto expuesto
EXPOSE 8080
# Copia el JAR generado en la etapa de construcción
COPY --from=build /app/target/*.jar app.jar
# Comando para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
