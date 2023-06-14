FROM openjdk:17.0.1-jdk-slim

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
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${JAVA_TIMEZ} ${JAVA_ACTIVE} -jar /app.jar"]


# 기존 내용임
## OpenJDK 이미지를 기반으로 함
#FROM openjdk:17-jdk
#
#
## 필요한 경우 추가 설정을 이곳에 작성
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]