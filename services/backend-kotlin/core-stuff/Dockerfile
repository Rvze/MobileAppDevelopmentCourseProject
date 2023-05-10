FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY /build/libs/core-stuff-1.0.jar build/

WORKDIR /app/build
ENTRYPOINT java -jar core-stuff-1.0.jar
