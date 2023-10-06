# GoodJob 🤖
`2023.05.16` ~ `2023.07.14`
> 생성 AI 기반 채용관련 서비스

- 비용 문제로 `08.08` 이후로 비활성화합니다.

## 어떻게 기획됐나요?

기존에는 이력서 검토나 모의 면접을 준비할 때 타인의 도움이 필요했던 불편함이 있었습니다.

그래서 챗 GPT 서비스를 이용해서 혼자서 이력서를 기반으로 검토를 받고, 면접 예상 질문을 받아볼 수 있다면 취업 준비에 도움이 되지 않을까에서 기획한 서비스입니다.

더 나아가 여러 사이트의 채용 공고를 한눈에 볼 수 있는 채용 공고 서비스를 지원하며, 다른 구직자들과 커뮤니티 서비스를 이용 할 수 있습니다.
또한,누구나 멘토링 서비스를 통해 멘토에게 실시간 채팅을 기반으로 한 커피챗 서비스를 이용할 수 있습니다.

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
- AWS RDS(MySQL)
- Spring Data Redis
- 챗 GPT 라이브러리 https://github.com/TheoKanning/openai-java
- 사람인 API
- 채용 공고 사이트 웹 크롤링(Selenium + Chrome Driver)
- 토스 페이먼트 결제 모듈
- Web Socket + STOMP

**View**

- HTML5
- CSS
- Thymeleaf

**DevOps**
- Google Cloud Platform
- Ubuntu 20.04 LTS
- Kubernetes 1.26
- Nginx Ingress Controller
- Istio Service Mesh
- Envoy Proxy
- Kiali
- Kafka 3.5.0V2 KRaft(without Zookeeper)
- UI for Kakfa
- Docker
- Jenkins
- GCP CloudBuild
- AWS RDS
- AWS Route53
- AWS S3
- AWS CloudWatch(SNS + Lambda를 통한 슬랙 알림)
- Apache JMeter
- Grafana + Prometheus

## 시스템 아키텍처(2023.07.12)

![스크린샷 2023-07-14 오전 12 35 41 2](https://github.com/waveofmymind/GoodJob/assets/93868431/c9efb774-67d3-4bf0-9746-2d240f260205)

## ERD

![erderd](https://github.com/waveofmymind/GoodJob/assets/93868431/0779a712-7c06-405a-ab26-a504f2cf2783)

## 프로젝트 멤버

- 전상준 - 인프라 구축, 생성 AI 서비스 구현
- 박우영 - Spring Batch 사용 비동기방식 채용사이트들의 정보 받아와서 정리하는 워크플로우
- 박찬규 - 스프링 시큐리티와 jwt토큰을 이용한 회원 인증 서비스, 마이페이지 및 결제 모듈
- 정회성 - 커뮤니티 서비스, 채팅 및 멘토링 서비스


## 기술적 도전 및 트러블 슈팅
- [서비스 원자성 보장하기 - 카프카 트랜잭션 아웃 박스 패턴](https://waveofmymind.github.io/posts/kafka-outbox-transaction/)
- [Cache Stampede 문제를 해결하기 위한 캐시 성능 개선 전략](https://waveofmymind.github.io/posts/improve-cache/)
- [쿠버네티스 서비스 로드밸런싱 적용기입니다. 그런데 이제 Istio를 곁들인 - 전상준](https://waveofmymind.github.io/posts/k8s-with-istio/)
- [서킷 브레이커 패턴을 적용한 쿠버네티스 트래픽 대응기 - 전상준](https://waveofmymind.github.io/posts/huge-traffic/)
- [이벤트 기반의 생성 AI 비동기 처리로 사용성 개선하기 - 전상준](https://waveofmymind.github.io/posts/kafka-processing/)
- [멀티 브로커 카프카, 주키퍼가 없는 KRaft 모드 - 전상준](https://waveofmymind.github.io/posts/kafka-kraft/)
- [로그 수집을 위한 AWS CloudWatch 적용기 - 전상준](https://waveofmymind.github.io/posts/aws-cloudwatch/)
- [효과적인 비즈니스 로직 모니터링 - 전상준](https://waveofmymind.github.io/posts/effective-monitoring/)
- [챗 GPT API 사용기 - 전상준](https://waveofmymind.github.io/posts/springboot+chatgpt/)
- [비동기 처리로 성능 향상시키기 - 전상준](https://waveofmymind.github.io/posts/async-processing/)
- [크롤링 트러블슈팅 - 박우영](https://velog.io/@wy9295/Java-%ED%81%AC%EB%A1%A4%EB%A7%81-%EC%97%90%EB%9F%AC%EB%AA%A8%EC%9D%8C)
- [배치 비동기처리 성능개선 - 박우영](https://velog.io/@wy9295/DB-Spring-Batch-%ED%99%9C%EC%9A%A9%ED%95%98%EC%97%AC-%EB%B3%91%EB%A0%AC%EC%B2%98%EB%A6%AC)
- [Redis - 깃허브 로그인 과정에서 직렬화 오류 - 박찬규](https://velog.io/@qmrma987/SpringBoot-JWT-Redis-%EC%82%AC%EC%9A%A9%EC%A4%91-%EA%B9%83%ED%97%88%EB%B8%8C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-API-serializationfailedexception-%EC%B2%98%EB%A6%AC)
- [로그인 후 이전 페이지로 sendRedirect 요청시 NullPointerException 오류 - 박찬규](https://velog.io/@qmrma987/SpringBoot-Thymeleaf-JWT-Redis-%EC%82%AC%EC%9A%A9%EC%A4%91-sendRedirect-%EB%AC%B8%EC%A0%9C)
- [Redis - 로그인 통합 테스트 중 NullpointerException 오류 - 박찬규](https://velog.io/@qmrma987/SpringBoot-Redis-%ED%82%A4-%EC%84%A4%EC%A0%95)






