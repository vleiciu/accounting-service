FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
EXPOSE 8082
ARG JAR_FILE=target/AccountingService-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} AccountingService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/AccountingService-0.0.1-SNAPSHOT.jar"]