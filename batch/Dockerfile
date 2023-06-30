FROM gradle:7.6.1-jdk17 AS build
# 전체 소스 코드 복사
COPY . /home/gradle/src
WORKDIR /home/gradle/src
# Gradle 빌드 실행 (api 모듈 빌드에 필요한 모든 의존성과 함께)
RUN gradle api:build

# 실행 스테이지
FROM mcr.microsoft.com/java/jre:17-zulu-ubuntu
# 빌드 스테이지에서 생성된 API 모듈의 JAR 파일을 이미지에 복사
COPY --from=build /home/gradle/src/api/build/libs/*.jar /app.jar
# Google Chrome 및 ChromeDriver 설치
RUN apt-get update && apt-get install -y \
  libssl-dev \
  libnss3 \
  wget \
  unzip \
  curl && \
  wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
  apt -y install ./google-chrome-stable_current_amd64.deb && \
  wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/`curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE`/chromedriver_linux64.zip && \
  unzip /tmp/chromedriver.zip chromedriver -d /usr/bin && \
  rm -rf /var/lib/apt/lists/*

# 8080 포트 노출
EXPOSE 8081
# Java 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${JAVA_ACTIVE} -jar /app.jar"]