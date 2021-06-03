FROM openjdk:8-alpine

ARG WAR_FILE=./banking-web/target/*.jar

COPY ${WAR_FILE} webapp.jar

CMD ["java", "-jar", "webapp.jar"]