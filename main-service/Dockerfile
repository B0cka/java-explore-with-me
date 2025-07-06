FROM eclipse-temurin:17-jre-jammy
VOLUME /tmp
ARG JAR_FILE=target/main-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]