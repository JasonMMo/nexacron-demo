# Apple Reminder App 웹 버전 개발 명세서 (Specification)

## 1. 제품 개요

### 1.1 제품명
**Apple Reminder App 웹 버전**

### 1.2 제품 설명
Apple Reminders 앱의 웹 버전으로, 사용자가 웹 브라우저를 통해 어디서든 원활하게 리마인더를 관리할 수 있도록 제공하는 서비스. 기존 Apple Reminders 앱의 핵심 기능을 웹 환경에 최적화하여 제공합니다.

### 1.3 타겟 사용자
- 기존 Apple 사용자 (iPhone, iPad, Mac 사용자)
- 웹 환경에서 작업을 선호하는 사용자
- 여러 기기 간에 리마인더를 동기화해야 하는 사용자
- 간단하고 직관적인 인터페이스를 선호하는 사용자

### 1.4 핵심 가치 제안
- **크로스 플랫폼**: 웹 브라우저로 어디서든 접속 가능
- **실시간 동기화**: 서버 기반의 중앙 관리로 데이터 일관성 유지
- **직관적 UI**: Apple 스타일의 깔끔하고 사용하기 쉬운 인터페이스
- **뛰어난 성능**: 신속한 응답 속도와 원활한 사용자 경험

## 2. 기능 요구사항

### 2.1 핵심 기능

#### 2.1.1 계정 관리
- **회원 가입/로그인**
  - 이메일 주소로 가입
  - 비밀번호 (암호화 저장)
  - 간편 로그인 기능 (예: OAuth)
  - 세션 관리

#### 2.1.2 리마인더 목록
- **메인 리스트 화면**
  - 리마인더 전체 목록 표시
  - 검색 필터 (제목, 내용, 날짜, 태그)
  - 정렬 기능
    - 생성일 기준
    - 마감일 기준
    - 우선순위 기준
  - 상태 필터 (전체, 완료됨, 미완료)

#### 2.1.3 리마인더 생성/수정
- **새 리마인더 생성**
  - 제목 입력
  - 상세 내용 입력
  - 마감일 설정 (일시, 반복)
  - 우선순위 설정 (낮음, 보통, 높음)
  - 알림 시간 설정
  - 태그 설정
  - 하위 리마인더 서브태스크 생성

- **리마인더 수정**
  - 기존 리마인더 정보 편집
  - 완료 상태 토글
  - 삭제 기능
  - 복사 기능

#### 2.1.4 상세 기능
- **리마인더 상세보기**
  - 모든 정보 표시
  - 관련 서브태스크 목록
  - 첨부파일 표시 (이미지, 첨부파일)
  - 연락처 정보 표시

- **검색 기능**
  - 전체 텍스트 검색
  - 태그 기반 검색
  - 날짜 범위 검색

- **필터링**
  - 태그별 필터
  - 날짜별 필터
  - 우선순위별 필터
  - 그룹별 필터

#### 2.1.5 알림 관리
- **푸시 알림**
  - 마감 임박 알림
  - 일일 알림
  - 반복 알림

- **이메일 알림**
  - 요약 알림

#### 2.1.6 그룹 관리
- **리마인더 그룹**
  - 리마인더 분류 및 관리
  - 그룹별 공유 기능

#### 2.1.7 라벨/태그 관리
- **태그 생성/관리**
  - 색상 지정
  - 아이콘 지정
  - 검색 및 필터링에 활용

### 2.2 고급 기능

#### 2.2.1 내보내기/가져오기
- **CSV/JSON 내보내기**
  - 리마인더 데이터 백업
  - 다른 서비스로 이전

- **CSV/JSON 가져오기**
  - 다른 서비스에서 데이터 가져오기

#### 2.2.2 템플릿
- **리마인더 템플릿**
  - 자주 사용하는 리마인더 템플릿 저장
  - 빠르게 생성 가능

#### 2.2.3 통계
- **리마인더 통계**
  - 완료율 표시
  - 카테고리별 통계
  - 일별/주별/월별 통계

## 3. 비기능 요구사항

### 3.1 성능 요구사항
- **페이지 로딩**: 3초 이내
- **API 응답**: 500ms 이내
- **실시간 동기화**: 5초 이내 반영
- **동시 접속자**: 1000명 동시 접속 지원
- **데이터 처리**: 1만 건 이내의 리마인더 표시 성능 보장

### 3.2 보안 요구사항
- **데이터 암호화**: HTTPS 사용
- **비밀번호 암호화**: bcrypt 또는 PBKDF2 사용
- **세션 관리**: JWT 토큰 사용
- **SQL 인젝션 방지**: 매개변화화 쿼리 사용
- **XSS 방지**: 입력값 검증 및 출력값 이스케이프

### 3.3 호환성 요구사항
- **웹 브라우저**
  - Chrome 최신 버전 지원
  - Firefox 최신 버전 지원
  - Safari 최신 버전 지원
  - Edge 최신 버전 지원
- **반응형 디자인**
  - 데스크톱 (1920x1080)
  - 태블릿 (768x1024)
  - 모바일 (375x812)

### 3.4 데이터베이스 요구사항
- **H2**: 개발 환경
- **MySQL**: 운영 환경
- **데이터 백업**: 일자별 자동 백업

## 4. API 명세

### 4.1 인증 API
```
POST /api/auth/signup     - 회원 가입
POST /api/auth/login      - 로그인
POST /api/auth/logout     - 로그아웃
POST /api/auth/refresh    - 토큰 갱신
```

### 4.2 리마인더 API
```
GET    /api/reminders      - 리마인더 목록 조회
GET    /api/reminders/{id} - 특정 리마인더 조회
POST   /api/reminders      - 리마인더 생성
PUT    /api/reminders/{id} - 리마인더 수정
DELETE /api/reminders/{id} - 리마인더 삭제
POST   /api/reminders/{id}/complete - 완료 상태 변경
```

### 4.3 검색 API
```
GET /api/reminders/search?query=검색어 - 리마인더 검색
GET /api/reminders/filter?tag=태그 - 필터링
```

### 4.4 그룹 API
```
GET    /api/groups        - 그룹 목록 조회
POST   /api/groups        - 그룹 생성
PUT    /api/groups/{id}   - 그룹 수정
DELETE /api/groups/{id}   - 그룹 삭제
```

### 4.5 nexacroN 전용 API
```
GET    /nexacro/api/reminders/list      - 리마인더 목록 (Dataset 형식)
POST   /nexacro/api/reminders           - 리마인더 생성
GET    /nexacro/api/reminders/{id}      - 특정 리마인더 조회
PUT    /nexacro/api/reminders/{id}      - 리마인더 수정
DELETE /nexacro/api/reminders/{id}      - 리마인더 삭제
POST   /nexacro/api/reminders/complete - 완료 처리
```

## 5. UI/UX 요구사항

### 5.1 디자인 시스템
- **Apple 스타일 유지**: 깔끔하고 간결한 디자인
- **색상 팔레트**
  - 기본 색상: 화이트, 그레이
  - 강조 색상: 파란색 (Apple Blue)
  - 배경색: F5F5F7
- **폰트**: San Francisco (Apple 스타일)

### 5.2 인터페이스 구성
- **헤더**
  - 로고
  - 검색 바
  - 사용자 메뉴

- **사이드바**
  - 네비게이션 메뉴
  - 메인 리스트
  - 태그 리스트
  - 그룹 리스트

- **메인 콘텐츠**
  - 리마인더 카드 형식
  - 상세 뷰
  - 생성/수정 폼

- **푸터**
  - 공지사항
  - 개인정보 처리방침
  - 이용약관

### 5.3 상호작용 요구사항
- **드래그 앤 드롭**: 우선순위 변경
- **더블 클릭**: 상세 보기
- **키보드 단축키**:
  - Ctrl+N: 새 리마인더 생성
  - Ctrl+F: 검색
  - Enter: 편집 시작
  - Esc: 편집 취소

## 6. 아키텍처

### 6.1 백엔드 아키텍처
```
┌─────────────────────────────────┐
│      Frontend (nexacroN)       │
└─────────────┬───────────────────┘
              │ HTTP/HTTPS
              ▼
┌─────────────────────────────────┐
│     API Gateway / Load Balancer │
└─────────────┬───────────────────┘
              │
              ▼
┌─────────────────────────────────┐
│        Spring Boot App          │
│  ├── Controller                │
│  ├── Service                  │
│  ├── Repository                │
│  └── Configuration             │
└─────────────┬───────────────────┘
              │
              ▼
┌─────────────────────────────────┐
│          Database              │
│  ┌──────────┐  ┌───────────┐  │
│  │   H2     │  │  MySQL    │  │
│  │(개발)    │  │(운영)     │  │
│  └──────────┘  └───────────┘  │
└─────────────────────────────────┘
```

### 6.2 데이터 모델
```java
// User 엔티티
@Entity
public class User {
    @Id
    private Long id;
    private String email;
    private String password;
    private String name;
    private LocalDateTime createdAt;
}

// Reminder 엔티티
@Entity
public class Reminder {
    @Id
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private boolean completed;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String priority;

    @ManyToOne
    private User user;

    @ManyToOne
    private Group group;

    @ManyToMany
    private List<Tag> tags;

    @OneToMany(mappedBy = "reminder")
    private List<Subtask> subtasks;
}

// Tag 엔티티
@Entity
public class Tag {
    @Id
    private Long id;
    private String name;
    private String color;
    private String icon;

    @ManyToMany(mappedBy = "tags")
    private List<Reminder> reminders;
}

// Group 엔티티
@Entity
public class Group {
    @Id
    private Long id;
    private String name;
    private String description;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "group")
    private List<Reminder> reminders;
}
```

## 7. 개발 로드맵

### 7.1 1단계 (MVP - Minimum Viable Product)
- [ ] 기본 회원가입/로그인 기능
- [ ] 리마인더 생성/조회/수정/삭제 기능
- [ ] 기본 목록 표시
- [ ] 간단한 검색 기능
- [ ] H2 데이터베이스 연동
- [ ] nexacroN API 기본 구현

### 7.2 2단계
- [ ] 반복 알림 기능
- [ ] 태그/라벨 기능
- [ ] 그룹 기능
- [ ] 우선순위 설정
- [ ] 상세 검색 및 필터링
- [ ] 반응형 디자인

### 7.3 3단계
- [ ] 고급 검색 기능
- [ ] 통계 기능
- [ ] 내보내기/가져오기 기능
- [ ] 템플릿 기능
- [ ] 실시간 동기화
- [ ] 모바일 최적화

## 8. 성능 지표 (KPI)

### 8.1 사용자 경험
- **Daily Active Users (DAU)**: 목표 10,000명
- **Monthly Active Users (MAU)**: 목표 50,000명
- **사용자 만족도**: NPS 40점 이상
- **이탈률**: 20% 이하

### 8.2 기술 지표
- **페이지 로딩 시간**: 2초 이내
- **API 응답 시간**: 300ms 이내
- **에러율**: 0.1% 이하
- **서버 가용성**: 99.9% 이상

## 9. 위험 분석

### 9.1 기술적 위험
- **데이터 손실**: 정기 백업 및 복원 절차 수립
- **성능 저하**: 캐시 전략 및 DB 튜닝
- **보안 취약점**: 정기 보안 점검

### 9.2 시장 위험
- **경쟁 앱 대비 차별화점**: Apple 스타일 UI/UX 강화
- **사용자 습득**: 초기 사용자 모집 전략 수립
- **수익 모델**: 프리미엄 기능 개발

### 9.3 운영 위험
- **서비스 중단**: 백업 시스템 및 대안 마련
- **사용자 피드백**: 빠른 버그 수정 및 기능 개선

## 10. 출시 계획

### 10.1 베타 버전
- **기간**: 개발 후 1개월
- **참여자**: 내부 테스트 100명
- **목표**: 기본 기능 검증 및 피드백 수집

### 10.2 정식 출시
- **날짜**: 베타 테스트 후 1개월
- **마케팅**: Apple 사용자 커뮤니티 홍보
- **지원**: 24/7 고객 지원 체계 구축

## 11. 유지보수 계획

### 11.1 버전 관리
- **주간**: 작은 버그 수정
- **월간**: 기능 개선 및 새 버전 출시
- **분기별**: 주요 업데이트 및 새 기능 출시

### 11.2 모니터링
- **사용자 활동**: Google Analytics 설정
- **시스템 상태**: CloudWatch/기타 모니터링 도구
- **오류 알림**: Slack/Email 알림 설정

---

**작성일자**: 2024-03-20
**작성자**: 개발팀
**버전**: 1.0