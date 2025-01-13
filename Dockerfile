# Etapa de compilación
FROM eclipse-temurin:21.0.5_11-jdk AS builder
WORKDIR /backend

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Copiar el archivo pom.xml y las fuentes
COPY pom.xml .
COPY src ./src

# Construir la aplicación (omitimos los tests)
RUN mvn clean package -DskipTests

# Etapa de producción
FROM eclipse-temurin:21.0.5_11-jdk
WORKDIR /apbackendp

# Configurar zona horaria a Chile
ENV TZ=America/Santiago
RUN apt-get update && apt-get install -y tzdata

# Copiar el archivo JAR generado desde la etapa de compilación
COPY --from=builder /backend/target/*.jar backend.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "backend.jar"]
