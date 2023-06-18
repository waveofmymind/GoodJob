#FROM openjdk:17.0.1-jdk-slim
#
#RUN apt-get update && apt-get install -y \
#  libssl-dev \
#  libnss3 \
#  && rm -rf /var/lib/apt/lists/*
#
#RUN apt-get -y update
#
#RUN apt -y install wget
#
#RUN apt -y install unzip
#
#RUN apt -y install curl
#
#RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
#
#RUN apt -y install ./google-chrome-stable_current_amd64.deb
#
#RUN wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/` curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE`/chromedriver_linux64.zip
#
#RUN unzip /tmp/chromedriver.zip chromedriver -d /usr/bin
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#EXPOSE 8080
#ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${JAVA_TIMEZ} ${JAVA_ACTIVE} -jar /app.jar"]
FROM gradle:7.6.1-jdk17 AS build
COPY . /home/gradle/src
WORKDIR /home/gradle/src
ARG JAR_FILE=build/libs/*.jar

FROM mcr.microsoft.com/java/jre:17-zulu-ubuntu
COPY --from=build /home/gradle/src/build/libs/*.jar /app.jar
RUN apt-get update && apt-get install -y \
  libssl-dev \
  libnss3 \
  && rm -rf /var/lib/apt/lists/*

RUN apt-get -y update

RUN apt -y install wget

RUN apt -y install unzip

RUN apt -y install curl

RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb

RUN apt -y install ./google-chrome-stable_current_amd64.deb

RUN wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/` curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE`/chromedriver_linux64.zip

RUN unzip /tmp/chromedriver.zip chromedriver -d /usr/bin
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${JAVA_ACTIVE} -jar /app.jar"]