# ===== Étape 1 : construire l'application =====
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# D'abord les fichiers Maven seuls, pour profiter du cache
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Ensuite le code source, et on compile
COPY src src
RUN ./mvnw package -DskipTests -B

# ===== Étape 2 : exécuter l'application =====
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]