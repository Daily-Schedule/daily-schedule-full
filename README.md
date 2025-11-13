# daily-schedule-full
우아한 테크코스 오픈미션 팀 프로젝트

## ✨ 주요 기능

- 내일 할 일 등록 (Create)
- 내일 할 일 목록 조회 (Read)
- 내일 할 일 수정 (Update)
- 내일 할 일 삭제 (Delete)

## ⚙️ 기술 스택

- **Backend:** Spring Boot, Spring Data JPA
- **Database:** MySQL (Docker)

## 📖 API 명세

### 1. (Create) 내일 할 일 등록

- **Method:** `POST`
- **URL:** `/api/tomorrow`
- **Request Body:**
    ```json
    {
      "startAt": "2025-11-14T20:30:00",
      "endAt": "2025-11-14T21:00:00",
      "content": "스프링 부트 API 만들기"
    }
    ```
- **Success Response (201 CREATED):**
    ```json
    {
      "id": 1,
      "author": "Yoon",
      "startAt": "2025-11-14T20:30:00",
      "endAt": "2025-11-14T21:00:00",
      "content": "스프링 부트 API 만들기",
      "createdAt": "2025-11-13T20:30:00"
    }
    ```

### 2. (Read) 내일 할 일 목록 조회

- **Method:** `GET`
- **URL:** `/api/tomorrow`
- **Request Body:** (없음)
- **Success Response (200 OK):**
    ```json
    [
      {
        "id": 1,
        "author": "Yoon",
        "startAt": "2025-11-14T20:30:00",
        "endAt": "2025-11-14T21:00:00",
        "content": "스프링 부트 API 만들기",
        "createdAt": "2025-11-13T20:30:00"
      },
      {
        "id": 2,
        "author": "Yoon",
        "content": "Docker로 MySQL 실행하기",
        "startAt": "2025-11-14T21:30:00",
        "endAt": "2025-11-14T22:00:00",
        "createdAt": "2025-11-13T20:35:00"
      }
    ]
    ```

### 3. (Update) 내일 할 일 수정

- **Method:** `PATCH` (또는 `PUT`)
- **URL:** `/api/tomorrow/{id}` (예: `/api/tomorrow/1`)
- **Request Body:** (수정할 내용)
    ```json
    {
      "startAt": "2025-11-14T20:30:00",
      "endAt": "2025-11-14T21:00:00",
      "content": "스프링 부트 API 테스트하기"
    }
    ```
- **Success Response (200 OK):**
    ```json
    {
      "id": 1,
      "author": "Yoon",
      "startAt": "2025-11-14T20:30:00",
      "endAt": "2025-11-14T21:00:00",
      "content": "스프링 부트 API 테스트하기",
      "createdAt": "2025-11-13T20:30:00"
    }
    ```

### 4. (Delete) 내일 할 일 삭제

- **Method:** `DELETE`
- **URL:** `/api/tomorrow/{id}` (예: `/api/tomorrow/1`)
- **Request Body:** (없음)
- **Success Response (204 No Content):** (응답 본문 없음)

---

## 📝 개발 체크리스트

-   [x] README 등록
-   [X] Docker로 MySQL 실행
-   [X] `application.yml` 설정 (JPA `ddl-auto: update` 설정)
-   [ ] DTO 코드 작성 (Request/Response DTO)
-   [ ] Entity 코드 작성 (`Tomorrow.java`)
-   [ ] Repository 코드 작성 (`TomorrowRepository.java`)
-   [ ] Service 코드 작성 (`TomorrowService.java` - CRUD 로직)
-   [ ] Controller 코드 작성 (`TomorrowController.java` - CRUD API)
-   [ ] Postman API 테스트 (CRUD 4개 기능 모두)
-   [ ] Postman Collection Export 및 공유
-   [ ] 프론트엔드 CORS 문제 해결 (`@CrossOrigin` 설정)