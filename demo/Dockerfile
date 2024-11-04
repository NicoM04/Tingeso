FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY build/libs/demo-0.0.1-SNAPSHOT.jar backend.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","./backend.jar"]