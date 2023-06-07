# OpenJDK 이미지를 기반으로 함
FROM openjdk:17-jdk


# 필요한 경우 추가 설정을 이곳에 작성
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]