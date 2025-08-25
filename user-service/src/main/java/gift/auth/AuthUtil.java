package gift.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AuthUtil {
    static String extractToken(HttpServletRequest request) {
        if (isApiRequest(request)) {
            String authHeader = request.getHeader("Authorization");
            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        }
        else {
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("jwt-token".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
        }
        return null;
    }

    static void handleAuthError(HttpServletRequest request, HttpServletResponse response, String msg) throws IOException {
        if (isApiRequest(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            String jsonMsg = String.format("{\"error\": \"%s\"}", msg);
            response.getWriter().write(jsonMsg);
        }
        else {
            String encodedMsg = URLEncoder.encode(msg, StandardCharsets.UTF_8);
            response.sendRedirect("/members/login?error=true&message=" + encodedMsg);
        }
    }

    private static boolean isApiRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api");
    }
}
