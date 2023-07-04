# GoodJob 🤖
`2023.05.16` ~ `2023.06.16` ~ `2023.07.14`
> 생성 AI 기반 채용관련 커뮤니티 서비스

- [서비스 링크](https://waveofmymind.shop)

## 어떻게 기획됐나요?

기존에는 이력서 검토나 모의 면접을 준비할 때 타인의 도움이 필요했던 불편함이 있었습니다.

그래서 챗 GPT 서비스를 이용해서 혼자서 이력서를 기반으로 검토를 받고, 면접 예상 질문을 받아볼 수 있다면 취업 준비에 도움이 되지 않을까에서 기획한 서비스입니다.

더 나아가 여러 사이트의 채용 공고를 한눈에 볼 수 있는 채용 공고 서비스를 지원하며, 다른 구직자들과 커뮤니티 서비스를 이용 할 수 있습니다.

## 기능 구현 목록

- [생성 AI 서비스](https://quasar-safflower-616.notion.site/AI-43e104d648564e03be6a00a079566bdd?pvs=4)
- [채용 공고 서비스](https://quasar-safflower-616.notion.site/68e738ec880f4c26a2efa43fbdacdd72?pvs=4)
- [커뮤니티 서비스](https://quasar-safflower-616.notion.site/58cabee91b1e449885edebe3ac0e0f35?pvs=4)
- [회원 인증 서비스](https://quasar-safflower-616.notion.site/5681bfaac2634bbdb73920a3351d124d?pvs=4)

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

<<<<<<< HEAD
<<<<<<< HEAD
- ~~Naver Cloud PlatForm~~
  - ~~CentOS 7.8~~
- ~~Docker~~
- ~~NginX ProxyManager~~
=======
>>>>>>> 34f4db80bfc7b7200ad226b70cb6e000d2c90b33
- Google Cloud Platform
- Kubernetes
- Kafka 3.4.0 KRaft
- Docker
- Jenkins
- AWS RDS
- AWS Route53
=======
- Naver Cloud PlatForm
  - CentOS 7.8
- Docker
- Google Cloud PlatForm
- Kubernetes
- Confluence Kafka
- NginX ProxyManager
- Jenkins
- AWS RDS
- AWS Route 53
>>>>>>> 68460b18ecb62cb22878a30c0ac18fe49c231747
- AWS S3
- AWS CloudWatch
- Apache JMeter
- Grafana + Prometheus

## 시스템 아키텍처(2023.07.03)

![스크린샷 2023-07-03 오전 12 42 39](https://github.com/waveofmymind/GoodJob/assets/93868431/8c8047a3-25f7-439f-a84b-70af4cf07b68)

## ERD

![goodjob06302](https://github.com/waveofmymind/GoodJob/assets/93868431/e698c43f-6f7d-40ab-bf2c-8b793be66194)

## 프로젝트 멤버

- 전상준 - CI/CD 구축 및 모니터링 환경 구성, 생성 AI 서비스 구현
- 박우영 - Spring Batch 사용 동기방식 채용사이트들의 정보 받아와서 정리하는 워크플로우
- 박찬규 - 스프링 시큐리티와 jwt토큰을 이용한 회원 인증 서비스
- 정회성 - 커뮤니티 서비스


## 기술적 도전 및 트러블 슈팅

- [로그 수집을 위한 AWS CloudWatch 적용기 - 전상준](https://waveofmymind.github.io/posts/aws-cloudwatch/)
- [효과적인 비즈니스 로직 모니터링 - 전상준](https://waveofmymind.github.io/posts/effective-monitoring/)
- [챗 GPT API 사용기 - 전상준](https://waveofmymind.github.io/posts/springboot+chatgpt/)
- [비동기 처리로 성능 향상시키기 - 전상준](https://waveofmymind.github.io/posts/async-processing/)
- [크롤링 트러블슈팅 - 박우영](https://velog.io/@wy9295/Java-%ED%81%AC%EB%A1%A4%EB%A7%81-%EC%97%90%EB%9F%AC%EB%AA%A8%EC%9D%8C)
- [배치 비동기처리 성능개선 - 박우영](https://velog.io/@wy9295/DB-Spring-Batch-%ED%99%9C%EC%9A%A9%ED%95%98%EC%97%AC-%EB%B3%91%EB%A0%AC%EC%B2%98%EB%A6%AC)
- [Redis - 깃허브 로그인 과정에서 직렬화 오류 - 박찬규](https://velog.io/@qmrma987/SpringBoot-JWT-Redis-%EC%82%AC%EC%9A%A9%EC%A4%91-%EA%B9%83%ED%97%88%EB%B8%8C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-API-serializationfailedexception-%EC%B2%98%EB%A6%AC)
- [로그인 후 이전 페이지로 sendRedirect 요청시 NullPointerException 오류 - 박찬규](https://velog.io/@qmrma987/SpringBoot-Thymeleaf-JWT-Redis-%EC%82%AC%EC%9A%A9%EC%A4%91-sendRedirect-%EB%AC%B8%EC%A0%9C)







