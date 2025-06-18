# 📰 CENS-BE (Chrome Extension News Summary - Backend)

**CENS-BE**는 뉴스 기사를 자동으로 요약해주는 크롬 확장 프로그램의 백엔드 서버입니다.  
OpenAI API를 활용하여 뉴스 본문을 한 문장으로 요약하고, 다양한 카테고리의 기사를 저장·조회할 수 있도록 지원합니다. 
Google OAuth2 기반 로그인과 JWT 인증을 통해 보안성을 확보합니다.

---

## 🚀 주요 기능

- 📰 Jsoup을 이용한 뉴스 기사 크롤링
- 🧠 OpenAI GPT를 활용한 뉴스 요약 기능
- 🔐 Google OAuth2 로그인 및 JWT 인증/인가
- 🗂️ 기사 카테고리 코드 기반 필터링
- 📝 전체 기사 목록 및 단일 기사 조회 API 제공
- 🔐 예외 및 오류 처리 (CustomException)
- ❌ 기사 전체 삭제 (관리자용 초기화 기능)

---

## 🛠️ 기술 스택

| 구분        | 기술 스택                          |
|-----------|--------------------------------|
| Language  | Java 17                        |
| Framework | Spring Boot                    |
| 웹 크롤링     | Java Jsoup                     |
| AI 요약     | Spring AI + OpenAI GPT         |
| DB / ORM  | MySQL,  JPA (Hibernate) |
| 인증        | Google OAuth2, JWT   |
| 테스트       | JUnit 5, Mockito               |
| 기타        | Lombok, REST API        |

---

## 🔐 인증/인가 구조

- 사용자는 Google 계정으로 로그인합니다.
- 최초 로그인 시 JWT access & refresh token 발급
- access token 만료 시 refresh token을 통해 재발급 요청 가능
- 인증된 사용자만 기사 저장/삭제 가능

---


## 🏛️ 프로젝트 구조

```
src
├── main
│ ├── java/com/handong/cens
│ │ ├── article # 기사 도메인 (Entity, DTO, Service 등)
│ │ ├── auth # OAuth2 & JWT 인증 관련
│ │ └── commons # 공통 예외 처리
│ └── resources # 설정 파일 (application.yml 등)
└── test
    └── java/com/handong/cens/article/service
        └── ArticleServiceTest.java
```


---

## ⚙️ 환경 변수 및 설정 예시

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://${DB_DOMAIN}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            scope:
              - email
              - profile
```


## 🤖 Commit Message Convention

- feat : 새로운 기능 추가, 기존의 기능을 요구 사항에 맞추어 수정
- fix : 기능에 대한 버그 수정
- build : 빌드 관련 수정
- chore : 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore
- docs : 문서(주석) 수정
- style : 코드 스타일, 포맷팅에 대한 수정
- refactor : 기능의 변화가 아닌 코드 리팩터링 ex) 변수 이름 변경
- release : 버전 릴리즈
- merge : 병합

## 🧑‍💻 developers

- frontend: [corkang](https://github.com/corkang), [Park MinJun](https://github.com/ParkMinjun0721)
- backend: [suminJN](https://github.com/SuminJN)

## 📽️ Project Link

- [github](https://github.com/Chrome-Extension-News-Summary-CENS)


```
본 프로젝트는 한동대학교 소프트웨어공학 수업의 팀 프로젝트로 진행되었습니다.
```
