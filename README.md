- Spring Framework 기반 웹 애플리케이션 프로젝트입니다.
- Spring Boot의 자동 설정을 사용하지 않고, XML 기반으로 DispatcherServlet, web.xml 등을 설정할 예정입니다.
- 기본적인 Spring MVC 설정부터, 브랜치별로 기능을 확장해나가며 진행했습니다.
- Thymleaf를 사용해 View를 구현할 예정입니다.

#### Spring MVC 설정 및 설정 파일
- `web.xml` : DispatcherServlet 등록, Context 설정
- `dispatcher-servlet.xml` : Spring MVC Controller, View 설정
- `root-context.xml` : 공통 빈 등록, DB 연동 등
- `pom.xml` : 의존성 관리
- `WEB-INF/views/` : Thymleaf View 저장 위치



---

### 1. **프로젝트 개요**

- **프로젝트명**: '땡스랩'
- **개요**: 여성 샌들 쇼핑몰인 ‘댕스랩’을 벤치마킹하여 상품 탐색, 장바구니, 주문, 회원 관리 등의 기능을 제공하는 **웹 기반 쇼핑몰 서비스**
- **개발 목적**: 실제 쇼핑몰 서비스 구조를 학습하고, 웹 백엔드 개발 및 배포 경험을 쌓기 위함


---


### 2. **역할 분담**

| 이름      | 담당 영역             | 상세 설명                                              |
| ------- | ----------------- |----------------------------------------------------|
| **김지후** | **상품(Item) 파트**   | `item`, `item_image`, `category`, `item_option`    |
| **강민서** | **주문(Order) 파트**  | `order`, `order_item`, `payment`, `delivery`       |
| **나영문** | **게시판(Board) 파트** | `item_question`, `question_answer`, `notice` |
| **나현지** | **장바구니(Cart) 파트** | `cart`                                             |
| **임홍빈** | **회원(User) 파트**   | `user`, `address`, `wishlist` |




---

### 3. **사용 기술 스택**

| 구분 | 기술 스택 | 설명 |
| --- | --- | --- |
| **언어** | Java (v17) | JVM 기반 객체지향 언어 |
| **템플릿 엔진** | Thymeleaf | 서버사이드 렌더링을 위한 템플릿 엔진 |
| **웹 서버 프레임워크** | Spring Framework (v5.3.x) | Java 기반 백엔드 프레임워크 |
| **DB 연동** | MyBatis | SQL Mapper 기반의 ORM 프레임워크, XML 기반 쿼리 사용 |
| **인증/인가** | ~~JWT (JSON Web Token)~~ | ~~토큰 기반 Stateless 인증 방식, 사용자 인증 처리에 사용~~ |
| **DB** | MySQL 8.x | 관계형 데이터베이스 |
| **DevOps** | Docker | 개발/배포 환경 일관성을 위한 컨테이너 기반 운영 도구 |
| **보안** | ~~Spring Security~~ | ~~보안 및 코드~~ |
| **etc** | Lombok, Validation, 테스트 관련 의존성 | 코드 생성 향상, 입력값 검증, 테스트 |


---

### 4. **시스템 아키텍처**

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

### 5. **주요 기능 명세**

- 추후 Admin 페이지 개발 예정
  - 회원 관리, 상품 관리, 매출 분석 등 관리자 전용 기능 구현 예정


| **기능 구분**  | **기능명**             | **설명**                         |
| ---------- | ------------------- | ------------------------------ |
| **회원 관리**  | 회원가입, 로그인, 로그아웃     | 세션 기반 인증 시스템 (추후 JWT 인증 도입 예정) |
| **상품 탐색**  | 카테고리별 상품 조회, 검색, 정렬 | Thymeleaf 템플릿을 통한 동적 페이지 렌더링   |
| **상품 리뷰**  | 상품 리뷰 작성 및 조회       | 실제 구매자만 작성 가능하도록 제한            |
| **장바구니**   | 상품 담기, 수량 변경, 삭제    | 로그인 사용자 기준으로 관리                |
| **주문 기능**  | 주문 생성 및 주문 내역 조회    | 결제 기능은 추후 도입 예정                |
| **게시판 기능** | 상품 문의/답변, 공지사항      | 커뮤니티 메뉴를 통해 사용자 소통 기능 제공       |



---

### 6. **DB 설계**

#### 초기 ERD
https://www.erdcloud.com/d/trrPYnTx93T5C5AnB

#### 실제 사용 ERD
https://www.erdcloud.com/d/2wsusuEasvGatRfzH



---

### 7. **보안 및 인증 처리**

- 세션기반 로그인 인증
  - 현재는 세션기반으로 로그인 인증 구현
  - 추후 JWT 및 Spring Security 적용 예정
- **~~JWT 기반 로그인 인증~~**
    - ~~로그인 시 토큰 발급~~
    - ~~Authorization 헤더에 `Bearer <token>` 형태로 요청~~
- **~~Spring Security 적용~~**
    - ~~URL 접근 제어~~
    - ~~관리자 전용 URL 보호~~