# ═══════════════════════════════════════════════════════════════════
#  SGS Backend — Dockerfile multi-stage
# ═══════════════════════════════════════════════════════════════════

# Stage 1 : Build Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /build

# Copier pom.xml séparément pour profiter du cache Docker
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copier les sources et compiler
COPY src ./src
RUN mvn clean package -DskipTests -q

# Stage 2 : Image de production légère
FROM eclipse-temurin:17-jre-alpine AS runtime

# Créer un utilisateur non-root (sécurité)
RUN addgroup -S sgs && adduser -S sgs -G sgs
USER sgs

WORKDIR /app

# Copier le JAR depuis le stage builder
COPY --from=builder /build/target/*.jar app.jar

# Port exposé
EXPOSE 8080

# Options JVM optimisées pour conteneur
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
