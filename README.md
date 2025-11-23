# daily-schedule-full

우아한 테크코스 오픈미션 팀 프로젝트

## 💬 Docker로 MySQL 실행하기

- 프론트엔드에서 DB 설치 없이 동일한 환경을 구성할 수 있도록 Docker를 사용한다.
- Docker Desktop 설치 방법

  - [Docker](https://www.docker.com/get-started/) 에서 알맞는 버전의 Docker Desktop을 설치한다.
    ![Docker 설치 안내](https://github.com/user-attachments/assets/2673847c-e031-4cb5-937d-e27db6026f96)
  - Docker를 사용하기 위한 계정을 생성한다.
  - Docker Desktop을 실행 후 가입한 계정으로 로그인한다.
    ![Docker 로그인 화면](https://github.com/user-attachments/assets/cdfa50b0-d462-4d1f-a708-0cc8d837d9fb)
  - 재부팅 후 Docker Desktop을 실행했을 때 아래와 같은 화면이 뜬다면 아무 키나 눌러서 업데이트 진행을 한다.

    ![업데이트 안내 화면](https://github.com/user-attachments/assets/d4f512ad-a2e5-460a-ab64-5fba76a36871)

  - 최종적으로 잘 실행되는 것을 확인한다.

    ![Docker 실행 확인 화면](https://github.com/user-attachments/assets/c439a9d8-4dd9-47b6-b13c-a1e202b35dce)

- Docker로 MySQL 실행

  ```bash
  docker run -d -p 3307:3306 --name mysql-jaksim MYSQL_ROOT_PASSWORD=admin -e MYSQL_DATABASE=jaksim_db mysql:8.0
  ```

  - `3307:3306`으로 한 이유는 기존에 로컬 MySQL을 3306 포트에서 사용하고 있어서 로컬의 3307포트로 오는 요청을 컨테이너의 3306 포트로 전달하도록 명령어를 수정했다. (로컬 MySQL을 3306에서 사용하지 않는다면 `3306:3306` 으로 하면 된다.)
  - `password`: 알맞은 MySQL root 비밀번호 입력
  - `MYSQL_DATABASE`: DB 이름 설정

- application.yml 설정

  - Docker로 띄운 MySQL DB 정보를 저장하고 JPA 설정을 명시하기 위해 daily_schedule.daily_schedule_be 폴더에 application.yml를 추가하고 다음 코드를 입력한다.

    ```yml
    server:
      port: 8080 # 서버 포트 설정 (선택 사항)

    spring:
      application:
        name: daily-schedule-be

      # Docker로 띄운 MySQL DB 정보
      datasource:
        url: jdbc:mysql://localhost:3307/jaksim_DB?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
        username: { 사용자의 username 입력 }
        password: { 사용자의 비밀번호 입력 }
        driver-class-name: com.mysql.cj.jdbc.Driver

      jpa:
        # database-platform: org.hibernate.dialect.MySQL8Dialect
        hibernate:
          ddl-auto: update
        show-sql: true

    # 사용자 정의 JWT 설정
    jwt:
      # Base64로 인코딩된, 32바이트 이상의 비밀 키
      secret-key: YourSuperSecretKeyThatIsVeryLongAndSecureForHS256
      # 토큰 만료 시간 (예: 1800000ms = 30분)
      expiration-time: 1800000
    ```

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
- **URL:** `/api/schedules`
- **Request Body:**

  ```json
  {
    "startTime": "2025-11-14T20:30:00",
    "endTime": "2025-11-14T21:00:00",
    "content": "스프링 부트 API 만들기"
  }
  ```

- **Success Response (201 CREATED):**

  ```json
  {
    "id": 1,
    "userId": "yoon2013",
    "startTime": "2025-11-14T20:30:00",
    "endTime": "2025-11-14T21:00:00",
    "content": "스프링 부트 API 만들기",
    "scheduleResultId": 1,
    "createdAt": "2025-11-13T20:30:00",
    "updatedAt": "2025-11-13T20:30:00"
  }
  ```

### 2. (Read) 내일 할 일 목록 조회

- **Method:** `GET`
- **URL:** `/api/schedules`
- **Query Parameters:**
  - `userId` (필수): 조회할 사용자의 ID (예: `yoon2013`)
  - `date` (필수): 조회할 날짜 (형식: `YYYY-MM-DD`, 예: `2025-11-14`)
  - **예시 URL:** `/api/schedules?userId=yoon2013&date=2025-11-14`
- **Request Body:** (없음)
- **Success Response (200 OK):**

  ```json
  [
    {
      "id": 1,
      "userId": "yoon2013",
      "startTime": "2025-11-14T20:30:00",
      "endTime": "2025-11-14T21:00:00",
      "content": "스프링 부트 API 만들기",
      "scheduleResultId": 1,
      "createdAt": "2025-11-13T20:30:00",
      "updatedAt": "2025-11-13T20:30:00"
    },
    {
      "id": 2,
      "userId": "yoon2025",
      "startTime": "2025-11-14T21:30:00",
      "endTime": "2025-11-14T22:00:00",
      "content": "Docker로 MySQL 실행하기",
      "scheduleResultId": 2,
      "createdAt": "2025-11-13T20:35:00",
      "updatedAt": "2025-11-13T20:35:00"
    }
  ]
  ```

### 3. (Update) 내일 할 일 수정

- **Method:** `PATCH` (또는 `PUT`)
- **URL:** `/api/schedules/{id}` (예: `/api/schedules/1`)
- **Request Body:** (수정할 내용)

  ```json
  {
    "startTime": "2025-11-14T20:30:00",
    "endTime": "2025-11-14T21:00:00",
    "content": "스프링 부트 API 테스트하기"
  }
  ```

- **Success Response (200 OK):**

  ```json
  {
    "id": 1,
    "userId": "yoon2013",
    "startTime": "2025-11-14T20:30:00",
    "endTime": "2025-11-14T21:00:00",
    "content": "스프링 부트 API 테스트하기",
    "scheduleResultId": 1,
    "createdAt": "2025-11-13T20:30:00",
    "updatedAt": "2025-11-13T20:30:00"
  }
  ```

### 4. (Delete) 내일 할 일 삭제

- **Method:** `DELETE`
- **URL:** `/api/schedules/{id}` (예: `/api/schedules/1`)
- **Request Body:** (없음)
- **Success Response (204 No Content):** (응답 본문 없음)

---

## 📝 개발 체크리스트

- [x] README 등록
- [x] Docker로 MySQL 실행
- [x] `application.yml` 설정 (JPA `ddl-auto: update` 설정)
- [x] DTO 코드 작성 (Request/Response DTO)
- [x] Entity 코드 작성
- [x] Repository 코드 작성
- [x] Service 코드 작성
- [x] Controller 코드 작성
- [x] Postman API 테스트 (CRUD 4개 기능 모두)
- [x] 로그인 기능과 합병 후 로그인 계정 정보 이용 기능 추가
- [ ] Postman Collection Export 및 공유
- [ ] 프론트엔드 CORS 문제 해결 (`@CrossOrigin` 설정)
