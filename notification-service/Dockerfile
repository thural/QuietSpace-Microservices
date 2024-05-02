FROM eclipse-temurin:21-jre-alpine as builder

WORKDIR extracted

ADD build/libs/notification-service-0.0.1-SNAPSHOT.jar app.jar

RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:21-jre-alpine

WORKDIR application

COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./

EXPOSE 8844

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]