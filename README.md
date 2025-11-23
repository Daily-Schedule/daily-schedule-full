# 작심 1일 웹 서비스 (daily-schedule-full)

우아한 테크코스 오픈미션 팀 프로젝트

## 👥 팀 소개

|                                 BE                                 |                                 BE                                 |                                 BE                                 |
|:------------------------------------------------------------------:|:------------------------------------------------------------------:|:------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/ohwoong12" width="150" height="150"> | <img src="https://avatars.githubusercontent.com/hi2242" width="150" height="150"> | <img src="https://avatars.githubusercontent.com/202110861" width="150" height="150"> |
| 권오웅 | 윤종근 | 윤미나 |
| [@ohwoong12](https://github.com/ohwoong12) | [@hi2242](https://github.com/hi2242) | [@202110861](https://github.com/202110861) |
| [권오웅의 개발 과정 보기](https://wiggly-dash-83a.notion.site/2b44a5e03d3380ae8316c4aaba5b5f6c?pvs=74) | [윤종근의 개발 과정 보기](https://wiggly-dash-83a.notion.site/2b44a5e03d3380318ad9eb0fce43bce3?pvs=74) | [윤미나의 개발 과정 보기](https://wiggly-dash-83a.notion.site/2b44a5e03d33809ab0edf5274601cda5?pvs=74) |



## 📍 목록

- [Docker로 MySQL 실행하기](#-docker로-mysql-실행하기)
- [ERD](#️-erd)
- [사용자 FLOW](#-사용자-flow)
- [주요 기능](#-주요-기능)
- [기술 스택](#️-기술-스택)
- [API 명세](#-api-명세)

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

## 🖌️ ERD

![alt text](https://file.notion.so/f/f/f415f33a-2998-452e-8253-b12774e120e8/1c248733-49ff-4b6b-9dc4-e50ed510cf18/image.png?table=block&id=2b44a5e0-3d33-8097-b718-eceecfc579f9&spaceId=f415f33a-2998-452e-8253-b12774e120e8&expirationTimestamp=1763906400000&signature=Ueq2sYabKJALzgEGRYCr1kHUPw5yCvYg37F1NESjyqM&downloadName=image.png)

## 🧐 사용자 FLOW

![alt text](https://file.notion.so/f/f/f415f33a-2998-452e-8253-b12774e120e8/6042544e-71d6-4722-a954-284e903a8b94/Mermaid_Chart_-_Create_complex_visual_diagrams_with_text.-2025-11-23-054948.png?table=block&id=2b44a5e0-3d33-80f4-9307-ef818cf3aef9&spaceId=f415f33a-2998-452e-8253-b12774e120e8&expirationTimestamp=1763906400000&signature=BiSUvPev6Xh3Y31aABZQ4zcBBmIfl4T8XP6n7rZDZUY&downloadName=Mermaid+Chart+-+Create+complex%2C+visual+diagrams+with+text.-2025-11-23-054948.png)

## ✨ 주요 기능

- 회원 가입 및 로그인

- 내일 할 일 등록 (Create)
- 내일 할 일 목록 조회 (Read)
- 내일 할 일 수정 (Update)
- 내일 할 일 삭제 (Delete)

- 오늘 일정 조회
- 오늘 일정 시작 및 종료

## ⚙️ 기술 스택

- **Frontend** React, TypeScript
- **Backend:** Spring Boot, Spring Data JPA
- **Database:** MySQL (Docker)

## 📖 API 명세

### [postman API Documentation](https://documenter.getpostman.com/view/50179945/2sB3dHVt3t)

---
