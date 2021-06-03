FROM openjdk:8-alpine

ARG WAR_FILE=./banking-web/target/*.war

COPY ${WAR_FILE} webapp.war

CMD ["java", "-jar", "webapp.war"]