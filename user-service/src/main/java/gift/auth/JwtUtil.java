package gift.auth;

import gift.member.domain.Member;
import gift.member.domain.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long timeoutMs;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.tokenTimeoutSec}") long timeOutSec) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.timeoutMs = timeOutSec * 1000;
    }

    public String generateToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + timeoutMs);

        return Jwts.builder()
                .subject(member.getEmail())
                .claim("role", member.getRole().name())
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    public RoleType getRoleType(String token) {
        String roleStr = getClaims(token).get("role", String.class);
        return RoleType.valueOf(roleStr);
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims
                    .getExpiration()
                    .before(new Date());
        }
        catch (Exception e){
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
}
