# [토이프로젝트] 감정일기장

## [0. 도메인모델](https://velog.io/@onlydev7777/%ED%86%A0%EC%9D%B4%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%90%EC%A0%95%EC%9D%BC%EA%B8%B0%EC%9E%A5-0-%EB%8F%84%EB%A9%94%EC%9D%B8-%EB%AA%A8%EB%8D%B8)
## [1. 로그인 프로세스](https://velog.io/@onlydev7777/%ED%86%A0%EC%9D%B4%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%90%EC%A0%95%EC%9D%BC%EA%B8%B0%EC%9E%A5-1)
## [2. JWT 토큰 검증 프로세스](https://velog.io/@onlydev7777/%ED%86%A0%EC%9D%B4%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%90%EC%A0%95%EC%9D%BC%EA%B8%B0%EC%9E%A5-2-JWT-%ED%86%A0%ED%81%B0-%EA%B2%80%EC%A6%9D-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4)
## [3. OAuth2 / Oidc 로그인 프로세스](https://velog.io/@onlydev7777/%ED%86%A0%EC%9D%B4%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%90%EC%A0%95%EC%9D%BC%EA%B8%B0%EC%9E%A5-3-OAuth2-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4)
## [4. Monolithic -> MSA 아키텍처](https://velog.io/@onlydev7777/%ED%86%A0%EC%9D%B4%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%90%EC%A0%95%EC%9D%BC%EA%B8%B0%EC%9E%A5-4-Monolithic-MSA)
## [5. Spring Cloud Eureka Server - Spring Cloud Gateway 연동](https://velog.io/@onlydev7777/%ED%86%A0%EC%9D%B4%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8MSA-%EA%B0%90%EC%A0%95%EC%9D%BC%EA%B8%B0%EC%9E%A5-5-Spring-Cloud-Eureka-Server-Spring-Cloud-Gateway-%EC%97%B0%EB%8F%99)
## [6. Spring Cloud Config 연동](https://velog.io/@onlydev7777/%ED%86%A0%EC%9D%B4%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8MSA-%EA%B0%90%EC%A0%95%EC%9D%BC%EA%B8%B0%EC%9E%A5-6-Spring-Cloud-Config-%EC%97%B0%EB%8F%99)

## 0~3 : Monolithic 아키텍처

인프런 [이정환님의 React 강의](https://www.inflearn.com/course/%ED%95%9C%EC%9E%85-%EB%A6%AC%EC%95%A1%ED%8A%B8/dashboard)를 통해 만든 감정 일기장 React 프로젝트에 Monolithic 기반 아키텍처 Spring Boot 백엔드 서버에 연동 한다.

## ☀️ 백엔드 기술스택
### 1) 로그인 인증 : Spring Security
### 2) OAuth2 / Oidc 로그인 인증 : Spring Security OAuth2 Client/
### 3) 인증방식 : JWT + Redis
### 4) DB 매핑 기법 : ORM (Spring Data JPA)
### 5) DTO 매핑 라이브러리 : MapStruct

## 4~? : MicroService 아키텍처

인프런 이도원 강사님의 [Spring Cloud로 개발하는 마이크로서비스 애플리케이션(MSA)](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%81%B4%EB%9D%BC%EC%9A%B0%EB%93%9C-%EB%A7%88%EC%9D%B4%ED%81%AC%EB%A1%9C%EC%84%9C%EB%B9%84%EC%8A%A4) 강의를 듣고 학습한 내용을 바탕으로 Monolithic 아키텍처를 MSA 아키텍처로 전환한다.

## ☀️ MSA 아키텍처 기술스택
### 1. Spring Cloud Eureka
### 2. Spring Cloud Gateway
### 3. Spring Cloud Config + RabbitMQ
### 4. Spring Cloud OpenFeign
### 5. Spring Cloud Circuit Breaker Resilience4j
### 6. Zipkin
### 7. Apache Kafka
### 8. Prometheus
### 9. Grafana
### 10. Docker