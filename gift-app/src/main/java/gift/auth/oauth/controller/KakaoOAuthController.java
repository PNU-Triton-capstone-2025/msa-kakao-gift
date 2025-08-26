package gift.auth.oauth.controller;

import gift.member.dto.MemberTokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;

@Controller
@RequestMapping("/members/login")
public class KakaoOAuthController {

    private final RestClient userApiClient;

    public KakaoOAuthController(RestClient.Builder builder, @Value("${api.gateway.uri}") String gatewayUri) {
        userApiClient = builder
                .baseUrl(gatewayUri)
                .build();
    }

    @GetMapping("/oauth2/code/kakao")
    public String kakaoRedirect(@RequestParam("code") String code, HttpServletResponse response) {
        MemberTokenResponse tokenResponse = userApiClient.get()
                .uri("/api/members/login/oauth/kakao?code={code}", code)
                .retrieve()
                .body(MemberTokenResponse.class);

        addTokenCookie(response, tokenResponse.token());
        return "redirect:/products";
    }

    private void addTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt-token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
    }
}
