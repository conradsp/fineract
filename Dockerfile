
FROM java:8

MAINTAINER  Antony Omeri, antonyomeri@gmail.com

RUN mkdir -p /app
WORKDIR /app

RUN ./build.sh