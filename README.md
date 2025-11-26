# 서비스 아키텍처 개요

## 전체 토폴로지
- **api-gateway** (Spring Cloud Gateway): 단일 진입점으로, JWT 검증 후 각 도메인 서비스로 라우팅하며 사용자 식별 정보를 헤더에 주입합니다.
- **gift-app** (BFF + UI): Thymeleaf 기반 서버 렌더링과 REST BFF 역할을 겸하며, 사용자가 받은 JWT를 그대로 게이트웨이 호출에 실어 보냅니다.
- **도메인 서비스**: `user-service`(인증/계정), `product-service`(카탈로그+관리자 CRUD), `wish-service`(위시리스트), `order-service`(주문)로 분리되어 독립 실행됩니다.

## 서비스 역할 및 의존 관계
- **gift-app**
  - `api.gateway.uri` 설정을 베이스 URL로 삼아 RestClient로 게이트웨이 엔드포인트(상품, 위시, 주문, 회원)를 호출합니다.
  - UI 흐름(상품 목록/상세, 위시리스트, 주문, 카카오 OAuth)을 제공하고 사용자 입력을 REST 호출로 변환합니다.
  - → 게이트웨이에 의존하며, 사용자 JWT를 포함해 요청을 전달합니다.

- **api-gateway**
  - 경로별 라우트에 `AuthenticationFilter`를 적용해 보호된 API를 `product-service`, `wish-service`, `order-service`, `user-service`로 팬아웃합니다.
  - → 도메인 서비스는 게이트웨이가 주입한 `X-Member-Id`, `X-Member-Role` 헤더를 신뢰해 인증/인가 컨텍스트를 받습니다.

- **user-service**
  - 로그인/회원가입을 처리하고 `id`, `role` 클레임이 담긴 JWT를 발급·검증합니다.
  - 카카오 OAuth 토큰을 받아오는 API를 제공하며, 발급된 JWT는 다른 서비스 호출 시 인증 근거가 됩니다.

- **product-service**
  - 상품 목록/상세 조회와 관리자용 CRUD를 노출합니다.
  - `/api/admin/**` 요청에 대해 `X-Member-Role=ADMIN`인지 인터셉터에서 검증합니다.

- **wish-service**
  - `X-Member-Id`로 회원 스코프를 구분해 위시리스트 CRUD와 페이지네이션을 제공합니다.

- **order-service**
  - `X-Member-Id`를 사용해 주문 생성과 주문 내역 조회를 처리합니다.

## 요청 흐름
1. **회원가입·로그인 (공개 경로)**
   - `gift-app`이 `/api/members/register`, `/api/members/login`에 자격 증명을 전송합니다.
   - 게이트웨이는 해당 경로에 필터를 적용하지 않고 `user-service`로 바로 전달합니다.
   - `user-service`가 사용자 정보를 검증해 만료 시간이 포함된 JWT를 발급합니다.

2. **인증이 필요한 호출**
   - 이후 모든 REST 호출은 `Authorization: Bearer <token>`을 포함한 채 게이트웨이로 전달됩니다.
   - `AuthenticationFilter`가 토큰 유효성·만료를 확인하고 `id`/`role` 클레임을 추출해 `X-Member-Id`, `X-Member-Role` 헤더로 추가합니다.
   - 각 도메인 서비스는 헤더 기반으로 회원 스코프나 관리자 권한을 판단해 비즈니스 로직을 수행합니다.

## 종단 간 상호작용 요약
- **Browser ↔ gift-app**: 사용자는 서버 렌더링된 화면에 접근하고, 로그인 후 받은 JWT가 세션/쿠키/헤더 형태로 유지됩니다.
- **gift-app ↔ api-gateway**: 모든 API 요청이 게이트웨이를 거치며 동일한 JWT를 전달합니다.
- **api-gateway ↔ 도메인 서비스**: 게이트웨이는 JWT를 검증·헤더 주입 후 경로에 맞는 서비스로 라우팅합니다.
- **도메인 서비스**: 전달받은 식별/역할 정보를 신뢰해 상품 관리(관리자), 위시/주문(회원) 등 역할 기반 흐름을 구현합니다.
