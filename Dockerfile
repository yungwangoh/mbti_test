FROM openjdk11-jdk
ARG JAR_FILE=build/lib/*.jar
VOLUME /tmp
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]