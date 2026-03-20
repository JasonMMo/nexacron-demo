# NexacroN Spring Boot Demo

Spring Boot와 nexacroN, uiadapter를 사용하는 프로젝트 예제입니다.

## 기술 스택
- Java 17
- Spring Boot 3.3.2
- H2 Database
- nexacroN 17.0.0
- uiadapter 17.0.0

## 프로젝트 구조
```
nexacron-demo/
├── pom.xml                    # Maven 설정
├── src/main/java/
│   └── com/example/nexacron/
│       ├── NexacronDemoApplication.java  # 메인 클래스
│       ├── config/           # 설정 클래스
│       ├── controller/       # REST 컨트롤러
│       ├── model/            # JPA 엔티티
│       ├── repository/       # 데이터 리포지토리
│       ├── service/          # 비즈니스 로직
│       ├── web/              # 웹 관련 설정
│       └── nexacro/          # nexacroN 관련 클래스
└── src/main/resources/
    ├── application.yml       # 애플리케이션 설정
    └── application-test.yml  # 테스트 설정
```

## 실행 방법

1. Maven 빌드
```bash
mvn clean install
```

2. 애플리케이션 실행
```bash
mvn spring-boot:run
```

## 주요 기능

### 1. User API
- GET /api/users - 모든 사용자 목록 조회
- GET /api/users/{id} - 특정 사용자 조회
- POST /api/users - 사용자 생성
- PUT /api/users/{id} - 사용자 정보 수정
- DELETE /api/users/{id} - 사용자 삭제

### 2. H2 콘솔
- http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- 사용자명: sa
- 비밀번호: password

## nexacroN 연동

이 프로젝트는 nexacroN과 Spring Boot의 통합을 위한 기본 구조를 제공합니다. `NexacroMessageConverter`와 `UserController`에서 nexacroN 형식의 응답을 구현했습니다.