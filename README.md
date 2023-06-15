# GoodJob 🤖
`2023.05.16` ~ `2023.06.16`
> 생성 AI 기반 채용관련 커뮤니티 서비스

[서비스 링크](https://waveofmymind.github.io)

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

<img width="889" alt="image" src="https://github.com/waveofmymind/GoodJob/assets/93868431/579cfda8-c39c-4b8c-ba12-8ad0ce8e0d71">

## ERD

![article](https://github.com/waveofmymind/GoodJob/assets/93868431/4a337a7d-e382-4ae1-b82f-4f5901ec945b)

## 프로젝트 멤버

- 전상준 - CI/CD 구축 및 모니터링 환경 구성, 생성 AI 서비스 구현
- 박우영 - Spring Batch 사용 동기방식 채용사이트들의 정보 받아와서 정리하는 워크플로우
- 박찬규 - 스프링 시큐리티와 jwt토큰을 이용한 회원 인증 서비스
- 정회성 - 커뮤니티 서비스


## 기술적 도전 및 트러블 슈팅

- [크롤링 트러블슈팅 - 박우영](https://velog.io/@wy9295/Java-%ED%81%AC%EB%A1%A4%EB%A7%81-%EC%97%90%EB%9F%AC%EB%AA%A8%EC%9D%8C)










