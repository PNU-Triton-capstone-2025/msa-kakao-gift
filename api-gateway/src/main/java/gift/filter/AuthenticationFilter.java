package gift.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import org.slf4j.Logger; // Logger 임포트
import org.slf4j.LoggerFactory; // LoggerFactory 임포트

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final SecretKey secretKey;

    public static class Config {}

    public AuthenticationFilter(@Value("${jwt.secret}") String secretKey) {
        super(Config.class);
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // 요청 헤더에 Authorization이 없는 경우 401 에러 반환
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Authorization 헤더가 없습니다.", HttpStatus.UNAUTHORIZED);
            }

            // 토큰 추출
            String authorizationHeader = Objects.requireNonNull(request.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

            // 토큰 유효성 검증
            if (!isJwtValid(jwt)) {
                return onError(exchange, "유효하지 않거나 만료된 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }

            Claims claims = getClaims(jwt);
            String memberId = String.valueOf(claims.get("id"));
            String role = claims.get("role", String.class);

            ServerHttpRequest newRequest = request.mutate()
                    .header("X-Member-Id", memberId)
                    .header("X-Member-Role", role)
                    .build();

            return chain.filter(exchange.mutate().request(newRequest).build());
        };
    }

    private boolean isJwtValid(String jwt) {
        try {
            Claims claims = getClaims(jwt);
            return !claims.getExpiration().before(new java.util.Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
