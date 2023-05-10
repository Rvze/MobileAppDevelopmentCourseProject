#!/bin/bash
# shellcheck disable=SC2164

# BUILD library-recognition service
cd library-recognition
gradle clean
gradle build
cd ..

# BUILD core-stuff service
cd core-stuff
gradle clean
gradle build
cd ..
#docker build .

# BUILD docker compose
docker-compose build
docker-compose down
docker-compose up
