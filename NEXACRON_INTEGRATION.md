# nexacroN 연동 가이드

## 목차
1. [개요](#개요)
2. [구성 요소](#구성-요소)
3. [API 사용법](#api-사용법)
4. [예제 코드](#예제-코드)
5. [주의사항](#주의사항)

## 개요

이 프로젝트는 Spring Boot와 nexacroN을 연동하기 위한 기본 구조를 제공합니다. nexacroN 플랫폼과 통신하기 위한 다양한 기능을 포함하고 있습니다.

## 구성 요소

### 1. nexacroN DTO 클래스
- `NexacroResponse`: nexacroN 형식의 응답 객체
- `NexacroDataset`: nexacroN Dataset 형식의 데이터 객체

### 2. 어노테이션
- `@NexacroMapping`: nexacroN API 매핑 정보 지정
- `@NexacroParam`: nexacroN 파라미터 바인딩

### 3. 핸들러
- `NexacroHandlerInterceptor`: 예외 처리 및 공통 응답 형식
- `NexacroResponseAdvice`: 응답 데이터 변환
- `NexacroMessageConverter`: nexacroN 메시지 파싱

### 4. 서비스
- `NexacroResponseFactory`: nexacroN 응답 객체 생성

## API 사용법

### 1. nexacroN 형식 API 사용

```java
@NexacroMapping("getUserList")
@GetMapping("/list")
public NexacroResponse<List<Map<String, Object>>> getUserList() {
    // 로직 구현
}
```

### 2. 파라미터 바인딩

```java
@NexacroMapping("getUser")
@GetMapping("/{id}")
public NexacroResponse<Map<String, Object>> getUser(
    @PathVariable Long id,
    @NexacroParam("includeEmail") String includeEmail) {
    // 로직 구현
}
```

### 3. 응답 형식

```java
// 단일 데이터 응답
return responseFactory.success(data);

// Dataset 응답
return responseFactory.success(dataset);

// 에러 응답
return responseFactory.error("ERR001", "User not found");
```

## 예제 코드

### 1. nexacroN API 컨트롤러

```java
@RestController
@RequestMapping("/nexacro/api/users")
@RequiredArgsConstructor
public class NexacroUserController {

    private final UserService userService;
    private final NexacroResponseFactory responseFactory;

    @NexacroMapping("getUserList")
    @GetMapping("/list")
    public NexacroResponse<List<Map<String, Object>>> getUserList() {
        List<User> users = userService.getAllUsers();

        List<Map<String, Object>> dataset = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", user.getId());
            row.put("username", user.getUsername());
            row.put("email", user.getEmail());
            dataset.add(row);
        }

        return responseFactory.success(dataset);
    }
}
```

### 2. 클라이언트 호출 예시

#### JavaScript
```javascript
// Dataset으로 데이터 받기
fetch('/nexacro/api/users/list')
  .then(response => response.json())
  .then(data => {
    console.log('Dataset:', data.dataset);
    console.log('성공:', data.success);
  });

// 단일 데이터 받기
fetch('/nexacro/api/users/1')
  .then(response => response.json())
  .then(data => {
    console.log('User:', data.data);
  });
```

#### nexacroN XAPI
```javascript
// Dataset으로 데이터 로드
this.transaction("getUserList", "UserService.getUserList", null, "ds_userList", function (sXml) {
    // 데이터 처리
}, {
    onError: function (e) {
        // 에러 처리
    }
});
```

## 주의사항

1. **JSON 형식**: nexacroN은 XML 또는 JSON 형식을 지원합니다. 기본값으로 JSON을 사용합니다.

2. **날짜 형식**: 날짜 데이터는 ISO 8601 형식으로 변환됩니다.

3. **Dataset 구조**:
   - List 형식의 데이터는 자동으로 Dataset으로 변환됩니다
   - Map 형식의 데이터는 단일 데이터로 변환됩니다

4. **에러 처리**: 모든 예외는 `NexacroResponseAdvice`를 통해 nexacroN 형식의 에러 응답으로 변환됩니다.

5. **메시지 파싱**:
   - JSON 형식: `{"username": "test", "email": "test@example.com"}`
   - key=value 형식: `username=test&email=test@example.com`

## 추가 설정

### 1. JSON 변환 옵션
```yaml
spring:
  jackson:
    default-property-inclusion: non_null
    serialization:
      write-dates-as-timestamps: false
```

### 2. 로깅 설정
```yaml
logging:
  level:
    com.example.nexacron: DEBUG
    org.nexacro: DEBUG
```