# -----------------------------------------------------------------------------
# STAGE 1: Builder
# -----------------------------------------------------------------------------
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Copiamos primero la definición de dependencias
COPY pom.xml .
# Descargamos dependencias en modo offline
RUN mvn dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Compilamos sin correr tests
RUN mvn clean package -DskipTests

# Extraemos las capas del JAR (Layered JAR)
RUN java -Djarmode=layertools -jar target/*.jar extract

# -----------------------------------------------------------------------------
# STAGE 2: Runtime
# -----------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# CREACIÓN DE USUARIO NO-ROOT (Seguridad Crítica)
# Evitamos que la app corra como 'root' dentro del contenedor.
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiamos las capas extraídas en el Stage 1 en orden específico
# 1. Dependencias (librerías externas)
COPY --from=builder /build/dependencies/ ./
# 2. Loader de Spring Boot
COPY --from=builder /build/spring-boot-loader/ ./
# 3. Dependencias Snapshot (nuestras librerías internas si las hubiera)
COPY --from=builder /build/snapshot-dependencies/ ./
# 4. Nuestra aplicación (el código que cambia frecuentemente)
COPY --from=builder /build/application/ ./

# Exponemos el puerto
EXPOSE 8080

# Usamos el JarLauncher para arrancar usando las capas
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]