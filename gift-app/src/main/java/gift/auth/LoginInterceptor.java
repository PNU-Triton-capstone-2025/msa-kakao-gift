package gift.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    public LoginInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String token = AuthUtil.extractToken(request);

        if (token == null) {
            AuthUtil.handleAuthError(request, response, "인증 정보가 없습니다. 다시 로그인해주세요");
            return false;
        }

        if (!jwtUtil.validateToken(token)) {
            AuthUtil.handleAuthError(request, response, "인증 정보가 유효하지 않거나 만료되었습니다. 다시 로그인해주세요.");
            return false;
        }
        request.setAttribute("isLoggedIn", Boolean.TRUE);
        return true;
    }
}
