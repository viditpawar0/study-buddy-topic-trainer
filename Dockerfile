FROM openjdk:25-slim
ARG JAR_FILE=build/libs/Topic\ Trainer-0.0.1-SNAPSHOT.jar
COPY $JAR_FILE app.jar
ENTRYPOINT ["java","-jar","/app.jar"]