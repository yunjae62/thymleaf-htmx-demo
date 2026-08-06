# 작업 메모

Thymeleaf + HTMX PoC 진행 요약. 회사 EDMS(충전/통계/문의) 개편 검토용, 10년 된 Angular 11 + Java 8 + Bootstrap 2.4 + MyBatis 스택 대체 가능성 검증 목적.

## 스택

Java 25, Spring Boot 4.0.7, Web MVC, Thymeleaf, HTMX 2.0.4, Spring Data JPA, H2, Pure CSS, Vanilla JS. `htmx-spring-boot-thymeleaf`(wimdeblauwe) 라이브러리로 `@HxRequest` 분기와 `th:hx-*` 다이얼렉트 사용.

## 범위 결정

- 최초 계획: 로그인/유저/통계/충전/문의 → 문의 제외, 충전을 게시판(Post)으로 대체
- 최종 범위: 로그인, 유저(엔티티까지), 게시판 목록/상세. 댓글·통계·수정/삭제는 미착수

## 패키지 구조

기능별 패키지(`auth`, `user`, `post`, `global/config`). 도메인당 `entity/`, `Controller`, `Service`, `Repository` 분리.

## 인증

- `User`(id, nickname, loginId, password, role) — `role`은 `UserRole` enum이 `GrantedAuthority` 구현. 내부 `Authorities` 클래스에 `ROLE_USER`/`ROLE_ADMIN` 컴파일타임 상수 보유(애너테이션에서 쓸 수 있게)
- `CustomUserDetails` — `UserDetails`를 record로 구현, 도메인 `User`를 감싸는 방식. 스프링 기본 `User` 클래스 대신 만든 이유는 `nickname` 등 도메인 정보에 로그인 후 접근하기 위함
- `UserDetailsServiceImpl` — `loginId`로 조회, `user.getRole()`을 그대로 권한으로 사용(역할별 매핑 자동 반영)
- `SecurityConfig` — `BCryptPasswordEncoder` 빈 등록(해시가 `{bcrypt}` 접두사 없는 순수 BCrypt라 `DelegatingPasswordEncoder` 기본값 사용 불가)
- `data.sql`에 기본 계정 시드: `admin` / `12345678`(BCrypt 해시로 저장, 평문은 주석에 명시)

## 게시판

- `Post`(id, title, content, author, createdAt). `content`는 `@Lob`(DB 방언 독립적, `columnDefinition="TEXT"` 대신 채택)
- 목록/상세만 구현(`PostController`, `PostService`), CRUD 나머지는 미착수
- 목록 페이지네이션은 htmx 대표 패턴 적용: 같은 URL(`GET /posts`)을 `@HxRequest` 여부로 분기해서, 전체 페이지는 `post/list`, htmx 요청은 `post/list :: list` fragment만 반환 — 컨트롤러/서비스/템플릿 전부 재사용
- 페이지 번호는 `spring.data.web.pageable.one-indexed-parameters: true`로 1부터 시작하도록 설정(URL 쿼리파라미터만 1-based, 내부 `Pageable`은 그대로 0-based)

## 공통 레이아웃 / htmx 배선

- `layout/base.html`에 `head(title)`/`header`/`footer` fragment 분리, 각 페이지는 `th:replace`로 조합(레이아웃 다이얼렉트 라이브러리 없이 순수 Thymeleaf fragment로 구현)
- CSRF: `<meta name="_csrf">`/`<meta name="_csrf_header">` + `htmx:configRequest` 리스너로 모든 htmx 요청에 토큰 자동 첨부. 리스너는 `document`(❌ `document.body`, `<head>` 실행 시점엔 아직 없어서 리스너 미등록되는 버그 있었음)에 등록
- `common.css`: CSS 변수 기반 최소 디자인(헤더 nav, 테이블, 페이저 active 상태, 버튼)

## 해결한 버그 (근본 원인 포함)

1. **htmx 요청 중 로그인 페이지가 부분 영역에 그대로 삽입됨** — 인증 안 된 상태에서 `hx-get` 요청 시 302를 htmx가 그대로 따라가 로그인 페이지 전체 HTML을 타겟 영역에 스왑. `htmx-spring-boot`의 `HxLocationRedirectAuthenticationEntryPoint`를 htmx 요청(`HX-Request` 헤더)에만 조건부로 등록해 해결
2. **`/login` 자체가 무한 리다이렉트 루프** — `.exceptionHandling().authenticationEntryPoint(...)`로 엔트리포인트를 **전역** 교체하면 스프링 시큐리티가 `DefaultLoginPageGeneratingFilter`(기본 로그인 페이지 생성 필터) 자체를 필터 체인에서 제외해버림. `.defaultAuthenticationEntryPointFor(entryPoint, matcher)`로 htmx 요청에만 조건부 적용하도록 좁혀서 해결. 필터 체인을 직접 덤프해서 원인 확정(1번 수정 시 있었던 회귀 버그)
3. **로그인 성공 후 `/login?continue`, `/favicon.ico?continue` 등 엉뚱한 곳으로 리다이렉트** — 2번 버그가 있던 동안 `SPRING_SECURITY_SAVED_REQUEST`에 `/login`·`favicon.ico` 같은 "페이지가 아닌" 경로가 잘못 저장됐던 잔여물 + `/favicon.ico`가 애초에 인증이 필요 없어야 하는데 permitAll 목록에 없었음. `RequestCache` 빈으로 `/login`은 저장 대상에서 제외(재발 방지), `/favicon.ico`는 permitAll 추가

## 커밋 이력 (현재까지)

```
d5bf5a4 style: 공통 CSS 디자인 개선
829845d fix: htmx 로그인 리다이렉트 및 인증 우회 경로 버그 수정
f67d92c feat: 게시글 목록/상세 조회 화면 추가
2c8fd2b docs: README.md 추가
ef3ab54 feat: 게시글 도메인 추가
cb9f147 feat: 유저 및 인증 기능 추가
25231ed feat: HTMX 문법 확인용 코드 예제 코드 추가
bfb12f7 first commit
```

## 남은 것 (미착수)

- 게시글 작성/수정/삭제, 검색
- 커스텀 로그인 화면(현재 스프링 시큐리티 기본 생성 페이지 그대로 — `common.css` 미적용 상태)
- 통계 화면
- 유저 관리 화면(ADMIN 전용 — `UserRole.ADMIN`은 정의만 되어 있고 실사용처 없음)
