FROM gradle:jdk21-alpine AS build

COPY . /app

WORKDIR /app

RUN gradle clean build --no-daemon


FROM eclipse-temurin:21-jre-alpine AS builder

WORKDIR extracted

COPY --from=build /app/build/libs/user-service-0.0.1-SNAPSHOT.jar ./app.jar

RUN java -Djarmode=layertools -jar app.jar extract


FROM eclipse-temurin:21-jre-alpine

WORKDIR application

COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./

EXPOSE 8081

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]