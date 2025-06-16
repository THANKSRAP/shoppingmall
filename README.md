# SpringMVC-Template(without Spring Boot)

- Spring MVC 기반 웹 애플리케이션 프로젝트입니다.
- Spring Boot의 자동 설정을 사용하지 않고, XML 기반으로 DispatcherServlet, web.xml 등을 설정할 예정입니다.
- 기본적인 Spring MVC 설정부터, 브랜치별로 기능을 확장해나가며 진행했습니다.
- Thymleaf를 사용해 View를 구현할 예정입니다.


## Spring MVC 설정 및 설정 파일
- `web.xml` : DispatcherServlet 등록, Context 설정
- `dispatcher-servlet.xml` : Spring MVC Controller, View 설정
- `root-context.xml` : 공통 빈 등록, DB 연동 등
- `pom.xml` : 의존성 관리
- `WEB-INF/views/` : JSP View 저장 위치


## 주요 의존성
- Java 17
- Spring Framework 5.3.x (Core, WebMVC, JDBC)
- Maven 3.x
- MyBatis
- Thymleaf
- Thymleaf Layout
- MySQL
- Log4j2 (SLF4J 연동)
- Tomcat 9 이상 (WAR 배포용)