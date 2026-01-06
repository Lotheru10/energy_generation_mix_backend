# ---- build ----
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

RUN chmod +x gradlew
RUN ./gradlew clean bootJar --no-daemon
RUN ls -la build/libs
RUN cp build/libs/*-SNAPSHOT.jar /app/app.jar || cp build/libs/*.jar /app/app.jar

# ---- run ----
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

