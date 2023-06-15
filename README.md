# GoodJob
---
> 생성 AI 기반 채용관련 커뮤니티 서비스

## 어떻게 기획됐나요?

기존에는 이력서 검토나 모의 면접을 준비할 때 타인의 도움이 필요했던 불편함이 있었습니다.

그래서 챗 GPT 서비스를 이용해서 혼자서 이력서를 기반으로 검토를 받고, 면접 예상 질문을 받아볼 수 있다면 취업 준비에 도움이 되지 않을까에서 기획한 서비스입니다.

더 나아가 여러 사이트의 채용 공고를 한눈에 볼 수 있는 채용 공고 서비스를 지원하며, 다른 구직자들과 커뮤니티 서비스를 이용 할 수 있습니다.

## 기술 스택

**Backend**
- Java 17
- Spring Boot 3.1.0
- Spring Security
    - OAuth 2.0
    - JWT 기반의 토큰 인증
- Spring Batch
- JPA(Hibernate)
- Querydsl
- Validation
- MySQL
- Redis
- 챗 GPT 라이브러리 https://github.com/TheoKanning/openai-java
- 사람인 API
- 채용 공고 사이트 웹 크롤링

**View**

- HTML5
- CSS
- Thymeleaf

**DevOps**

- Naver Cloud PlatForm
  - CentOS 7.8
- Docker
- NginX ProxyManager
- Jenkins
- AWS RDS
- AWS S3
- AWS CloudWatch
- Grafana + Prometheus

## 시스템 아키텍처

![img.png](img.png)

## ERD


## 기술적 도전 및 트러블 슈팅








