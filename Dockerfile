# OpenJDK 이미지를 기반으로 함
FROM openjdk:17-jdk

# Chrome 및 ChromeDriver 설치에 필요한 의존성 설치
RUN yum -y update && \
    yum -y install wget unzip curl

# Google Chrome 설치
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm && \
    yum -y localinstall google-chrome-stable_current_x86_64.rpm

# ChromeDriver 설치
RUN wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/$(curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE)/chromedriver_linux64.zip && \
    unzip /tmp/chromedriver.zip chromedriver -d /usr/bin/

# 필요한 경우 추가 설정을 이곳에 작성
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]