# Shopping Mall Project

Spring MVC 기반의 쇼핑몰 프로젝트입니다.

## 🏗️ 프로젝트 구조

```
shoppingmall/
├── src/main/java/com/example/shoppingmall/
│   ├── common/                    # 공통 모듈
│   │   ├── config/               # 설정 클래스들
│   │   ├── exception/            # 예외 처리
│   │   ├── response/             # 응답 DTO
│   │   └── util/                 # 유틸리티
│   ├── user/                     # 사용자 도메인
│   │   ├── controller/           # 사용자 컨트롤러
│   │   ├── service/              # 사용자 서비스
│   │   ├── dao/                  # 데이터 접근 계층
│   │   ├── domain/               # 도메인 객체
│   │   └── exception/            # 사용자 예외
│   ├── item/                     # 상품 도메인
│   │   ├── controller/           # 상품 컨트롤러
│   │   ├── service/              # 상품 서비스
│   │   ├── dao/                  # 데이터 접근 계층
│   │   └── domain/               # 도메인 객체
│   ├── cart/                     # 장바구니 도메인
│   ├── order/                    # 주문 도메인
│   ├── review/                   # 리뷰 도메인
│   ├── notice/                   # 공지사항 도메인
│   └── admin/                    # 관리자 도메인
├── src/main/resources/
│   ├── mapper/                   # MyBatis 매퍼
│   ├── db.properties            # 데이터베이스 설정
│   ├── mail.properties          # 메일 설정
│   └── log4j2.xml              # 로깅 설정
└── src/main/webapp/
    ├── resources/               # 정적 리소스
    │   ├── css/                # 스타일시트
    │   └── js/                 # 자바스크립트
    └── WEB-INF/
        ├── views/              # 뷰 템플릿
        └── spring/            # Spring 설정
```

## 🚀 주요 기능

- **사용자 관리**: 회원가입, 로그인, 이메일 인증
- **상품 관리**: 상품 조회, 카테고리별 분류
- **장바구니**: 상품 추가, 수량 변경, 삭제
- **주문 관리**: 주문 생성, 주문 내역 조회
- **리뷰 시스템**: 상품 리뷰 작성 및 조회
- **관리자 기능**: 로그인 상태 관리

## 🛠️ 기술 스택

- **Framework**: Spring MVC 5.3.14
- **Database**: MySQL
- **ORM**: MyBatis
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven
- **Java Version**: 17

## 📋 클린 코드 개선사항

### ✅ 완료된 개선사항

1. **System.out.println 제거**

   - 모든 디버그 출력을 적절한 로깅으로 대체
   - SLF4J + Log4j2 사용

2. **패키지 구조 정리**

   - 설정 파일들을 `common/config`로 통합
   - `PageRequest`를 `common/dto`로 이동하여 재사용성 향상
   - 일관된 계층 구조 적용

3. **의존성 주입 개선**

   - `@Autowired` 제거, `@RequiredArgsConstructor` 사용
   - 생성자 주입 방식으로 통일

4. **예외 처리 표준화**

   - 전역 예외 처리기 개선
   - 구체적인 예외 타입별 처리

5. **코드 간소화**

   - 불필요한 주석 제거
   - 메서드명 일관성 확보
   - 중복 코드 제거

6. **페이징 시스템 개선**

   - `PageRequest` 클래스에 정적 팩토리 메서드 추가
   - `PageResult` 클래스에 유틸리티 메서드 추가
   - 모든 도메인에서 일관된 페이징 처리

7. **로깅 시스템 강화**
   - 구조화된 로깅 메시지
   - 성능과 디버깅을 위한 적절한 로그 레벨
   - 비즈니스 로직 추적을 위한 상세 로깅

### 🔧 추가 개선 권장사항

1. **DTO 패턴 적용**

   - Request/Response DTO 분리
   - 도메인 객체와 DTO 분리

2. **검증 로직 강화**

   - Bean Validation 적용
   - 비즈니스 로직 검증 추가

3. **보안 강화**

   - 비밀번호 암호화
   - XSS 방지
   - CSRF 보호

4. **성능 최적화**
   - 캐싱 적용
   - 페이징 처리 개선
   - 쿼리 최적화

## 🚀 실행 방법

1. **데이터베이스 설정**

   ```bash
   docker-compose up -d
   ```

2. **프로젝트 빌드**

   ```bash
   mvn clean install
   ```

3. **애플리케이션 실행**

   ```bash
   mvn spring-boot:run
   ```

4. **접속**
   ```
   http://localhost:8080/shoppingmall
   ```

## 📝 개발 가이드라인

### 코드 스타일

- 클래스명: PascalCase
- 메서드명: camelCase
- 상수: UPPER_SNAKE_CASE
- 패키지명: lowercase

### 로깅 규칙

- INFO: 비즈니스 로직 성공
- WARN: 예상 가능한 오류
- ERROR: 예상치 못한 오류

### 예외 처리

- 비즈니스 예외: CustomException 사용
- 시스템 예외: GlobalExceptionHandler에서 처리
- 사용자 친화적 메시지 제공

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.
