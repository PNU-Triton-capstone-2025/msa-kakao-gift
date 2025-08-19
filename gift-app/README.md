# spring-gift-order (Mission 4)

## API 명세

### 홈 View

| 기능 | HTTP Method | 엔드포인트 (Endpoint) | 설명     |
| :--- | :--- | :--- |:-------|
| 메인 페이지 | `GET` | `/` | 메인 페이지 |

### 회원 관리 View

| 기능          | HTTP Method | 엔드포인트 (Endpoint)    | 설명          |
|:------------|:------------|:--------------------|:------------|
| 로그인 페이지     | `GET`       | `/members/login`    | 로그인 페이지     |
| 로그인 처리      | `POST`      | `/members/login`    | 로그인         |
| 회원가입 페이지    | `GET`       | `/members/register` | 회원가입 페이지    |
| 회원가입 처리     | `POST`      | `/members/register` | 회원가입        |
| 로그아웃 처리     | `POST`      | `/members/logout`   | 로그아웃        |
| 마이페이지       | `GET`       | `/members/mypage`   | 마이페이지 조회    |
| 비밀번호 변경 페이지 | `GET`       | `/members/edit`     | 비밀번호 변경 페이지 |
| 비밀번호 변경     | `POST`      | `/members/edit`     | 비밀번호 변경 처리  |
| 회원 탈퇴       | `POST`      | `/members/delete`   | 회원 탈퇴 처리    |

### 카카오 로그인 View

| 기능 | HTTP Method | 엔드포인트 (Endpoint) | 설명 |
| :--- | :--- | :--- | :--- |
| 카카오 로그인 처리 | `GET` | `/members/login/oauth2/code/kakao` | 카카오 로그인 성공 후 인가 코드를 받아 처리 |

### 상품 관리 View

| 기능 | HTTP Method | 엔드포인트 (Endpoint) | 설명          |
| :--- | :--- | :--- |:------------|
| 상품 목록 페이지 | `GET` | `/products` | 전체 상품 목록 조회 |

### 상품 관리 관리자 View
| 기능 | HTTP Method | 엔드포인트 (Endpoint) | 설명 |
| :--- | :--- | :--- | :--- |
| 상품 목록 조회 | `GET` | `/admin/product` | 전체 상품 목록 조회 |
| 상품 상세 조회 | `GET` | `/admin/product/{productId}` | 상품 상세 정보 조회 |
| 상품 추가 | `POST` | `/admin/product/add` | 새 상품 등록 |
| 상품 수정 | `POST` | `/admin/product/edit/{productId}` | 상품 정보 수정 |
| 상품 삭제 | `DELETE` | `/admin/product/{productId}` | 상품 삭제 |

### 상품 옵션 관리자 View

| 기능 | HTTP Method | 엔드포인트 (Endpoint) | 설명 |
| :--- | :--- | :--- |:---|
| 상품 옵션 관리 페이지 | `GET` | `/admin/products/{productId}/options` | 특정 상품의 옵션 목록 및 추가/삭제 페이지 |
| 상품 옵션 추가 | `POST` | `/admin/products/{productId}/options/add` | 특정 상품에 새 옵션 추가 |
| 상품 옵션 삭제 | `POST` | `/admin/products/{productId}/options/delete/{optionId}` | 특정 상품의 옵션 삭제 |

### 위시리스트 관리 View

| 기능 | HTTP Method | 엔드포인트 (Endpoint) | 설명 |
| :--- | :--- | :--- |:---|
| 위시리스트 페이지 | `GET` | `/wishes` | 사용자의 위시리스트 목록 페이지를 표시 |
| 위시리스트 추가 처리 | `POST` | `/wishes/add` | 상품을 위시리스트에 추가하고 목록 페이지로 리다이렉트 |
| 위시리스트 수량 수정 처리 | `POST` | `/wishes/update/{wishId}` | 위시리스트 상품의 수량을 수정하고 목록 페이지로 리다이렉트 |
| 위시리스트 삭제 처리 | `POST` | `/wishes/delete/{wishId}` | 위시리스트에서 상품을 삭제하고 목록 페이지로 리다이렉트 |

### 주문 관리 View

| 기능 | HTTP Method | 엔드포인트 (Endpoint) | 설명 |
| :--- | :--- | :--- |:---|
| 주문서 작성 페이지 | `GET` | `/orders/form` | 위시리스트 ID(`wishId`)를 받아 주문서 페이지를 표시 |
| 주문 처리 | `POST` | `/orders/create` | 주문 정보를 받아 주문을 생성하고 성공 페이지로 리다이렉트 |
| 주문 성공 페이지 | `GET` | `/orders/success` | 주문 성공 후 표시되는 페이지 |

### 회원 관리 API

| 기능 | HTTP Method | 엔드포인트 (Endpoint) | 요청 (Request) | 응답 (Response)                |
| :--- | :--- | :--- | :--- |:-----------------------------|
| 일반 회원가입 | `POST` | `/api/members/register` | Body: `MemberRegisterRequest` | **201 Created** Body: JWT 토큰 |
| 관리자 회원가입 | `POST` | `/api/members/register/admin` | Body: `MemberRegisterRequest` | **201 Created** Body: JWT 토큰 |
| 로그인 | `POST` | `/api/members/login` | Body: `MemberLoginRequest` | **200 OK** Body: JWT 토큰      |


### 상품 관리 API

| 기능 | HTTP Method | 엔드포인트 (Endpoint)                  | 요청 (Request) | 응답 (Response) |
| :--- | :--- |:----------------------------------| :--- | :--- |
| 상품 등록 | `POST` | `/api/admin/products`             | Body: `ProductRequestDto` (상품 정보) | **201 Created** Body 없음 |
| 전체 상품 조회 | `GET` | `/api/admin/products`             | 없음 | **200 OK** Body: `List<ProductResponseDto>` (상품 목록) |
| 특정 상품 조회 | `GET` | `/api/admin/products/{productId}` | Path: `productId` | **200 OK** Body: `ProductResponseDto` (상품 상세 정보) |
| 상품 정보 수정 | `PUT` | `/api/admin/products/{productId}` | Path: `productId` Body: `ProductRequestDto` (수정할 상품 정보) | **204 No Content** Body 없음 |
| 상품 삭제 | `DELETE` | `/api/admin/products/{productId}` | Path: `productId` (상품 ID) | **204 No Content** Body 없음 |

### 상품 옵션 API

| 기능 | HTTP Method | 엔드포인트 (Endpoint) | 요청 (Request) | 응답 (Response) |
| :--- | :--- | :--- | :--- |:---|
| 상품 옵션 목록 조회 | `GET` | `/api/products/{productId}/options` | Path: `productId` | **200 OK** Body: `List<ProductOptionResponseDto>` |


### 위시리스트 관리 API

| 기능 | HTTP Method | 엔드포인트 (Endpoint) | 요청 (Request) | 응답 (Response) |
| :--- | :--- | :--- | :--- |:---|
| 위시리스트 추가 | `POST` | `/api/wishes` | Header: `Authorization` (JWT 토큰)<br>Body: `WishRequest` | **201 Created**<br>Body: 없음 |
| 위시리스트 조회 | `GET` | `/api/wishes` | Header: `Authorization` (JWT 토큰)<br>Query: `page`, `size`, `sort` | **200 OK**<br>Body: `Page<WishListResponse>` (위시리스트 목록) |
| 위시리스트 수량 수정 | `PATCH` | `/api/wishes/{wishId}` | Header: `Authorization` (JWT 토큰)<br>Path: `wishId`<br>Body: `WishUpdateRequest` | **204 No Content**<br>Body: 없음 |
| 위시리스트 삭제 | `DELETE` | `/api/wishes/{wishId}` | Header: `Authorization` (JWT 토큰)<br>Path: `wishId` | **204 No Content**<br>Body: 없음 |


### 주문 관리 API

| 기능 | HTTP Method | 엔드포인트 (Endpoint) | 요청 (Request) | 응답 (Response) |
| :--- | :--- | :--- | :--- |:---|
| 주문 생성 | `POST` | `/api/orders` | Header: `Authorization` (JWT 토큰)Body: `OrderRequestDto` | **201 Created**Body: `OrderResponseDto` (생성된 주문 정보) |


## 주문하기 - 외부 API 연동

### 0단계 -  기본 코드 준비

- [x] 상품 관리 페이지 번호 하이퍼링크 수정

### 1단계 - 카카오 로그인

- [x] 토큰 응답 매핑 DTO 생성
- [x] 사용자 정보 응답 매핑 DTO 생성
- [x] 카카오 API 통신 클라이언트 구현
- [x] 인가 코드 리다이렉트 컨트롤러 구현
- [x] 카카오 로그인 페이지 UI 구현
- [x] 카카오 로그인 테스트 코드 작성

### 1단계 - 코드 리뷰 반영

- [x] Test code를 실행할 때 JwtUtil 대신 FakeJwtUtil 적용해서 JWT 의존성 제거 
- [x] test용 properties 파일 정의

### 2단계 - 주문하기

- [x] orders 도메인, repository 구현
- [x] 카카오 메시지 템플릿용 DTO 구현
- [x] orderService 구현 - 주문 시 상품 옵션 재고 차감 처리
    - [x] 일반 로그인 사용자 : 주문만 완료
    - [x] 카카오 로그인 사용자 : 주문 완료 후 카카오톡 메시지 발송
- [x] orderController 구현
- [x] 상품 주문 페이지 구현

### 2단계 - 코드 리뷰 반영

- [x] kakao OAuth RestClient baseUrl 상수를 KakaoProperties로 이동
- [x] CreateOrder Transaction 단위 조정
- [x] 주문하기 시 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제하기
- [x] 주문하기 테스트 코드 추가

### 3단계 - 배포하기

- [x] EC2 생성 및 배포 스크립트 작성
- [x] GitHub Action workflow 작성
- [x] CORS 문제 해결
- [ ] (옵션) HTTPS 적용하기

# spring-gift-product (Mission 1)

## 상품관리 - 스프링 입문

### 1단계 - 상품 API

- [x] Product 도메인 구현
- [x] Product DTO 구현
- [x] Product Service 구현
- [x] Product Controller 구현
- [x] 공통 예외 처리 구현 (NOT_FOUND)

### 1단계 - 코드 리뷰 반영

- [x] 불필요한 주식 제거
- [x] 테스트용 코드 제거
- [x] 규칙 9: getter/setter/프로퍼티를 쓰지 않는다 - Product setter 제거
- [x] service 계층의 id, Map 객체를 repository 계층으로 이동

### 2단계 - 관리자 화면

- [x] 상품 리스트 조회 페이지 구현
- [x] 상품 추가 페이지 구현
- [x] 상품 상세 페이지 구현
- [x] 상품 수정 & 삭제 페이지 구현

### 2단계 - 코드 리뷰 반영

- [x] 주석 제거
- [x] 불필요한 생성자 제거
- [x] Optional 예외 처리 refactoring
- [x] 매직 넘버 제거

### 3단계 - 데이터베이스 적용

- [x] schema.sql 작성
- [x] interface를 사용해서 기존 repo 추상화
- [x] JdbcProductRepository 구현
- [x] 테스트 코드 보완(ProductApiControllerTest)

### 3단계 - 코드 리뷰 반영

- [x] JdbcClient에서 사용할 RowMapper 직접 구현하고 setter와 기본 생성자 제거
- [x] 사용하지 않는 메소드 제거
- [x] validation 사용자 경험 개선(500 error -> forwarding)

# spring-gift-wishlist (Mission 2)

## 위시 리스트 - 요청과 응답 심화

### 0단계 - 기본 코드 준비

### 1단계 - 유효성 검사 및 예외 처리

- [x] 유효성 검사 로직 구현하기(검증기).
- [x] API 에 유효성 검사 적용
- [x] 상품관리 관리자 페이지에 적용

### 1단계 - 코드 리뷰 반영

- [x] 유효성 검사 실패시 모든 실패 항목 메시지에 포함하기
- [x] README.md 에 체크박스 사용해보기
- [x] 400 에러 테스트 코드 추가하기

### 2단계 - 회원 로그인

- [x] Member 도메인 구현, 스키마 추가
- [x] MemberRepository 구현
- [x] JWT 토큰 생성, 토근 정보 추출 구현
- [x] MemberService 구현
- [x] MemberController 구현
- [x] Member 예외 처리 구현
- [x] JWT 인증 필터(인터셉터) 적용
- [x] 인증 예외 처리 구현
- [x] 테스트 코드 JWT 인증 추가

### 2단계 - 코드 리뷰 반영

- [x] 헬퍼 메서드 접근 지정자 수정
- [x] 표기법 통일(properties)
- [x] JWT expiration 적용
- [x] JWT 관련 deprecated 메소드 사용 제거
- [x] Interceptor: authHeader 토큰 문제 수정
- [x] Member register, login 테스트 코드 작성
- [x] 테스트 코드 메소드 추출, 불필요한 코드 제거

### 3단계 - 위시 리스트

- [x] 회원 login, register 페이지 구현
- [x] cookie 기반 토큰 인터셉터 처리 로직 구현
- [x] 위시 리스트 domain, repository 설계
- [x] 위시 리스트 service, controller 설계
- [x] 위시 리스트 페이지 구현
- [x] 상품 관리 관리자 페이지 접근 권한 검증
- [x] 회원 수정, 삭제에 필요한 service, repository 추가 구현
- [x] 회원 수정, 삭제 페이지 구현
- [ ] 암호화 적용하기

### 3단계 - 코드 리뷰 반영

- [x] wish service, repository, controller 피드백 내용 수정
- [x] AccessDeniedException 제거 후 적절한 커스텀 예외 도입
- [x] Interceptor 간 의존성 제거하기

# spring-gift-enhancement (Mission 3)

## 상품 고도화 - JPA

### 0단계 - 기본 코드 준비

### 1단계 - 엔티티 매핑

- [x] domain 모델 JPA entity로 전환하기
- [x] repository interface 추가
- [x] JdbcClient -> JPA 전환하기
- [x] @DataJpaTest를 활용한 테스트 코드 작성해보기

### 1단계 - 코드 리뷰 반영

- [x] import 시 와일드 카드 사용 제거
- [x] 코드 포매팅 개선
- [x] @ManyToOne optional 설정
- [x] 외래 키 제약조건 명시적으로 설정하기
- [x] member repository 인터페이스 파라미터 참조형으로 변경
- [x] member repository 불필요한 Adapter 코드 제거
- [x] product repository 인터페이스 파라미터 참조형으로 변경
- [x] product repository 불필요한 Adapter 코드 제거
- [x] wish repository 인터페이스 파라미터 참조형으로 변경
- [x] wish repository 불필요한 Adapter 코드 제거
- [x] wish repository Response 의존성 제거
- [x] wish repository 메소드에서 JPA 변경 감지 활용하기

### 2단계 - 페이지네이션

- [x] Pageable 객체를 활용해서 product pagination 적용하기
- [x] product Thymeleaf 페이지 기반으로 수정하기
- [x] Pageable 객체를 활용해서 wish pagination 적용하기
- [x] wish Thymeleaf 페이지 기반으로 수정하기
- [x] 페이지네이션 기능 확인용 DB에 테스트 데이터 추가하기

### 2단계 - 코드 리뷰 반영

- [x] 페이지네이션 sort 필드 유효성 검증 로직 추가
- [ ] 페이지네이션 ID 값 기준으로 다음 데이터 찾는 메서드 구현

### 3단계 - 상품 옵션

- [x] Option 엔티티 생성 및 Product 연관 관계 설정
- [x] Option 재고 차감, 중복 이름 검증 구현
- [x] Option repository 구현
- [x] Option service 구현
- [x] Option controller 구현
- [x] (선택) 상품 옵션 서비스 화면 추가
- [x] Option Api 요청 테스트 코드 추가

### 3단계 - 코드 리뷰 반영

- [x] 사용하지 않는 변수 제거
- [x] product, productOption 연관관계 설정 메서드 리팩토링, 중복 제거
- [x] Product update시 변경되는 필드만 접근하도록 변경
- [x] 페이지네이션 sort 인자 에러를 PropertyReferenceException가 아닌 Controller 단에서 처리하도록 수정
- [x] Transactional 범위 축소
- [x] '카카오' 이름 사용 가능 여부 RolyType에 따라 제한하도록 수정하기