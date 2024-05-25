FROM openjdk:17.0.1-jdk
VOLUME /tmp
EXPOSE 8000
ARG JAR_FILE=target/scalable-java-users-module-1.0.2.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar", "-Dspring.profiles.active=prod"]