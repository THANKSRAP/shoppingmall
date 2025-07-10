# 🛍️ 땡스랩 쇼핑몰 (ThanksLab Shopping Mall)

Spring Framework 기반 웹 애플리케이션 프로젝트입니다. Spring을 통해 레거시 구조를 경험해보고자 했습니다. Spring Boot의 자동 설정 기능을 사용하지 않고 직접 설정을 해보며 Spring과 Servlet, Tomcat 등의 연결이 어떻게 이루어지는지 이해해보고자 했습니다.  

DB 모델링 부터 진행하여 Backend를 구현했습니다. Spring Boot의 자동 설정을 사용하지 않고, XML 기반으로 DispatcherServlet, web.xml 등을 설정할 예정입니다. 기본적인 Spring MVC 설정부터, 브랜치별로 기능을 확장해나가며 진행했습니다. Thymleaf를 사용해 View를 구현할 예정입니다.



---

## 📌 프로젝트 개요

- **프로젝트명**: '땡스랩'
- **개요**: 여성 샌들 쇼핑몰인 ‘댕스랩’을 벤치마킹하여 상품 탐색, 장바구니, 주문, 회원 관리 등의 기능을 제공하는 **웹 기반 쇼핑몰 서비스**
- **프로젝트 기간**: 25.06.09. ~


---

## ⚙️ 사용 기술 스택

Spring을 통해 레거시 구조를 경험해보고자 했습니다. Spring Boot의 자동 설정 기능을 사용하지 않고 직접 설정을 해보며 Spring과 Servlet, Tomcat 등의 연결이 어떻게 이루어지는지 이해해보고자 했습니다. 

JPA와 같이 자동화된 ORM 대신 MyBatis를 사용해 복잡한 SQL 쿼리를 직접 작성하고 최적화해보고자 했습니다. DAO에 대한 이해도를 높이고자 했습니다.  

Docker를 통해 DB 환경 등 개발 환경을 통일 시켜서 진행하고자 했습니다. 


- 언어
  - Java (v17)
- 템플릿 엔진
  - Thymleaf
- 웹 서버 프레임워크
  - Spring Framework (v5.3.x)
- DB 연동
  - MyBatis
- 인증/인가
  - JWT (Json Web Token)
- DB
  - MySQL 8.x
- DevOps
  - Docker
- 보안
  - Spring Security
- ETC
  - Lombok, Validation, Junit 등


---

## 🧾 설정 파일 설명

- `web.xml` : DispatcherServlet 등록, Context 설정
- `dispatcher-servlet.xml` : Spring MVC Controller, View 설정
- `root-context.xml` : 공통 빈 등록, DB 연동 등
- `pom.xml` : 의존성 관리
- `WEB-INF/views/` : Thymleaf View 저장 위치


---

## 🛠️ 환경변수 관리 (.env) (추후 리팩토링 예정)

`.env` 파일을 사용해 환경변수를 통합으로 관리할 예정입니다.  
현재는 `.gitignore`에 등록하지 않아, 환경변수에 대한 정보를 볼 수 있습니다. 추후 리팩토링 예정입니다.  `.env`로 환경변수를 관리할 수 있다면 `.env.dev`, `.env.prod` 등으로 나누어서 관리할 수 있습니다.  
SpringDotenv(환경변수 관리)라는 의존성을 Spring Framework에서 사용할 수 있는지 확인이 필요합니다.  
- Spring Framework는 Spring Boot와 달리 `.env` 파일이 자동으로 로딩 되지 않음
- Spring Framework에서는 직접 `.env` 파일을 읽고 `Environment`나 Bean 주입하는 방식으로 사용 
→ Spring Framework에서는 `.properties` + `@Value` 방식이 더 추천된다고 합니다. 환경변수를 관리하는 방식에 대해서 다시 생각해보고 추후 리팩토링 진행 예정입니다.


---

## 📁 프로젝트 구조

`도메인형 아키텍처`를 채택해 구현을 진행했습니다.  
기능이나 계층(Service, Controller 등)을 기준으로 나누는 전통적인 방식(Layered Architecture)이 아닌, 업무 도메인 중심으로 디렉토리와 클래스를 구성함으로써 다음과 같은 이점을 얻고자 했습니다:
- 높은 응집도: 관련된 도메인 클래스들이 하나의 디렉토리에 모여 있어 변경이나 유지보수가 쉬움
- 관심사의 분리(Separation of Concern): 각 도메인의 책임이 명확하게 분리됨
- 확장 용이성: 기능 추가 시 해당 도메인 디렉토리 내에서만 작업 가능
- 협업 효율 증가: 역할 분담이 도메인 단위로 나뉘므로 팀원이 각 도메인에 집중하기 쉬움

> 도메인마다 공통으로 사용하는 기능(ex: 예외 처리, 보완 설정, 로깅, 인터셉터 등)은 별도 공통 패키지 또는 설정 패키지(ex: config, common 패키지)에 분리해서 관리할 예정입니다.


```
🛍️ shoppingmall/
├── .idea/                           # IntelliJ 설정 폴더
├── db/                              # DB 초기화 SQL, ERD 등
├── logs/                            # 로그 파일 저장 위치
├── src/
│   ├── main/
│   │   ├── java/com/example/shoppingmall/
│   │   │
│   │   │── config/                  # 전체 공통 설정
│   │   │   ├── WebMvcConfig.java
│   │   │   ├── SecurityConfig.java
│   │   │   ├── MyBatisConfig.java
│   │   │   └── JwtTokenProvider.java
│   │   │
│   │   │── common/                  # 공통 모듈 (예외, 응답 포맷, 유틸 등)
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── CustomException.java
│   │   │   │   └── ErrorResponse.java
│   │   │   ├── util/
│   │   │   │   └── JwtUtil.java
│   │   │   └── response/
│   │   │       └── ApiResponse.java
│   │   │
│   │   │── user/                    # 회원 도메인
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── dto/
│   │   │   └── domain/
│   │   │
│   │   │── item/                    # 상품 도메인
│   │   │── cart/                    # 장바구니 도메인
│   │   │── order/                   # 주문 도메인
│   │   │── board/                   # 게시판/문의 도메인
│   │   │   └── ...
│   │   │
│   │   └── resources/
│   │       ├── dispatcher-servlet.xml     # Spring MVC 설정
│   │       ├── root-context.xml           # 공통 빈 설정
│   │       ├── mapper/                    # MyBatis Mapper XML
│   │       └── static/, templates/        # 정적 자원 및 Thymeleaf 뷰
│   │
│   └── test/                      # 테스트 코드
│       └── java/com/example/shoppingmall/
├── target/                        # Maven 빌드 아웃풋
├── docker-compose.yml             # 개발환경 통합을 위한 Docker 설정
├── pom.xml                        # 프로젝트 의존성 정의
├── .env                           # 환경 변수 파일 (.gitignore 대상 예정)
├── .gitignore
└── README.md
```



---

## 🔧 시스템 아키텍처

```
[사용자]
   ↓
[Thymeleaf 기반 웹 페이지]
   ↓ (폼 전송 / 요청)
[Spring + MyBatis 서버]
   ↓ (쿼리 실행)
[MySQL DB]

+ 세션기반 로그인/회원관리 처리
+ Docker로 서버와 DB 환경 컨테이너 구성 및 실행
```




---

## 🚀 주요 기능 명세

| 기능 영역     | 세부 기능                         | 설명                                 |
|------------|--------------------------------|------------------------------------|
| 회원 관리    | 회원가입, 로그인, 로그아웃            | 세션 기반 인증 시스템 (JWT 도입 예정)     |
| 상품 탐색    | 카테고리별 조회, 검색, 정렬           | Thymeleaf를 통한 동적 페이지 렌더링       |
| 상품 리뷰    | 리뷰 작성 및 조회                    | 구매자만 리뷰 작성 가능                    |
| 장바구니     | 상품 담기, 수량 변경, 삭제            | 로그인 사용자의 장바구니 세션 기반 관리     |
| 주문 기능    | 주문 생성 및 조회                    | 결제 기능은 추후 추가 예정                |
| 게시판 기능  | 상품 문의/답변, 공지사항             | 커뮤니티/고객센터 기능 제공               |
| 관리자 기능  | 회원/상품/매출 관리 (예정)            | 관리자 전용 Admin 페이지 예정             |



---

## 🧮 DB 설계

- 🔗 [초기 ERD 설계](https://www.erdcloud.com/d/trrPYnTx93T5C5AnB)
- 🔗 [최종 사용 ERD](https://www.erdcloud.com/d/2wsusuEasvGatRfzH)



---

## 🔀 Git 브랜치 전략

- `main` : 배포(운영)용 브랜치
- 기능(도메인)별 브랜치 : `feature/user`, `feature/item`, `feature/order` ...



---

## 📘 GitHub Convention

**Issue 기반 개발 흐름**
- 이 프로젝트는 **모든 작업을 Issue 중심으로 관리**합니다.
- 기능 개발, 버그 수정, 리팩토링 등 모든 작업은 먼저 Issue를 등록한 후,
- 해당 이슈 번호를 기준으로 브랜치를 생성하고, 커밋 및 Pull Request(PR)을 작성합니다.

**이러한 흐름을 통해 다음과 같은 이점을 얻을 수 있습니다:**
- 작업 단위가 명확하게 추적되어 기록 및 리뷰가 수월함
- PR과 커밋이 특정 이슈에 정확하게 연결되어 흐름이 명확해짐
- GitHub Projects 및 Kanban 보드와 자연스럽게 연계 가능
- 자동화 도구(릴리즈 노트, CI/CD 트리거 등)와 통합 용이


---

### 📝 커밋 컨벤션

#### 커밋 형식

```
<type>: <subject>
- <body> (선택)
Issue: #<issue number>
```

- `subject`: 현재형 + 명령형, 50자 이내
- `body`: 변경 이유, 상세 설명 등 (선택 사항)
- `Issue: #번호`를 통해 관련 이슈를 연결결합니다.


#### 커밋 예시

```bash
feat: 사용자 회원가입 기능 구현
- 유효성 검증 및 비밀번호 암호화 추가
Issue: #12
```


#### 커밋 타입 목록

>	Conventional Commits 표준을 따랐습니다. 자동 릴리즈, changelog 등과 호환이 되도록 (feat:, fix:, ...) 등의 형태를 사용합니다.

| 타입         | 설명                                |
| ---------- | --------------------------------- |
| `feat`     | 새로운 기능 추가                         |
| `fix`      | 버그 수정                             |
| `docs`     | 문서 작업 (README, API 문서 등)          |
| `style`    | 코드 스타일 변경 (공백, 세미콜론 등 / 기능 변경 없음) |
| `refactor` | 리팩토링 (기능 변경 없이 구조 개선)             |
| `test`     | 테스트 코드 추가 또는 수정                   |
| `chore`    | 설정 파일, 빌드 작업, 패키지 관리 등 기타 잡무성 작업  |
| `perf`     | 성능 개선                             |
| `ci`       | CI 환경 구성 및 수정                     |
| `build`    | 빌드 도구 또는 의존성 관련 작업                |
| `revert`   | 이전 커밋 되돌리기                        |


---

### 📝 Issue 컨벤션

#### Issue 제목 형식

```
[<유형>] <작업 요약> #<이슈번호>
```


#### Issue 제목 예시

```bash
[Feature] 사용자 주소 등록 기능 구현 #32
[Bug] 로그인 실패 시 잘못된 에러 메시지 출력 #33
[Refactor] 장바구니 서비스 리팩토링 #34
```


#### Issue 타입 목록

> Commit, PR과 표기를 달리해 역할을 확실하게 구분

| 타입         | 설명                  |
| ------------ | ------------------- |
| `[Feature]`  | 기능 추가 또는 개선         |
| `[Bug]`      | 오류 수정               |
| `[Refactor]` | 코드 리팩토링, 구조 개선      |
| `[Docs]`     | 문서 관련 작업            |
| `[Test]`     | 테스트 관련 작업           |
| `[Chore]`    | 설정, 패키지, CI 등 기타 잡무 |


#### Issue 본문 예시

>📄 이슈 본문은 `.github/`에 템플릿을 생성해 관리합니다

```md
## ✨ 설명
회원가입 시 주소 등록 기능이 누락되어 있음

## ✅ 작업 내용
- 주소 등록 form 추가
- UserController에 주소 처리 API 구현
- MyBatis mapper 및 테스트 코드 추가

## 📎 참고자료
- [디자인 시안 - Figma 링크]
- [API 명세서 - Notion 문서]
```


---

### 📝 Pull Request(PR) 컨벤션

#### PR 제목 형식

```
<type>: <요약 설명>
```

- `type`은 커밋 메시지에서 사용하는 타입과 동일 (`feat`, `fix`, `refactor` 등)
- `요약 설명`은 간결하게 해당 작업의 목적이나 결과를 서술
- 이슈 번호는 PR **본문에 Closes #번호**로 명시 (제목에는 포함하지 않음)


#### PR 제목 예시

```bash
feat: 회원가입 기능 구현
fix: 장바구니 수량 오류 수정
refactor: 회원 도메인 서비스 책임 분리
docs: README 구조도 추가
```

#### PR 본문 예시

>📄 PR 본문은 `.github/`에 템플릿을 생성해 관리합니다

```md
## 🔗 관련 이슈
Closes #32

## ✨ 주요 변경 사항
- 회원가입 기능 구현: 유효성 검증, 암호화, 중복 체크
- UserController, UserService 작성
- MyBatis 매핑 파일 추가

## ✅ 테스트 내역
- 회원가입 성공/실패 케이스 테스트 완료
- 중복 이메일 등록 방지 테스트 통과

## 📌 TODO (후속 작업)
- 이메일 인증 기능 분리 예정
```


---

### ✅ 전체 작업 흐름 요약

1. **이슈 생성**

   ```bash
   [Feature] 회원가입 기능 구현 #32
   ```

2. **브랜치 생성**

   ```bash
   git checkout -b feature/signup
   ```

3. **커밋 작성**

   ```bash
   feat: 사용자 회원가입 기능 구현
   - 유효성 검증 및 중복 체크 추가
   Issue: #32
   ```

4. **PR 생성**

   - 제목:

     ```bash
     feat: 회원가입 기능 구현
     ```
   - 본문:

     ```md
     Closes #32
     ...
     ```



---

## 🧑‍🤝‍🧑 팀원 역할 분담(추후 작성 완료)

| 이름      | 담당 영역             | 상세 설명                                              |
|-----------|----------------------|------------------------------------------------------|
| 김지후     | 상품(Item) 파트        | `item`, `item_image`, `category`, `item_option` 등 |
| 강민서     | 주문(Order) 파트       | `order`, `order_item`, `payment`, `delivery`       |
| 나영문     | 게시판(Board) 파트     | `item_question`, `question_answer`, `notice`        |
| 나현지     | 장바구니(Cart) 파트     | `cart`                                              |
| 임홍빈     | 회원(User) 파트        | `user`, `address`, `wishlist`                      |
