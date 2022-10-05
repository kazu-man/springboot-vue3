# ビルド環境
FROM gradle:jdk11-jammy as build-stage
WORKDIR /app
COPY . .
RUN ./gradlew build

# 本番環境
FROM openjdk:11-slim as production-stage
COPY --from=build-stage /app/build/libs /tmp/jar
EXPOSE 80
CMD ["java","-Dspring.profiles.active=production","-jar","/tmp/jar/app.jar"]
