package gift.auth.oauth.controller;

import gift.auth.oauth.service.KakaoOAuthService;
import gift.member.dto.MemberTokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/members/login")
public class KakaoOAuthController {
    private final KakaoOAuthService oAuthService;

    public KakaoOAuthController(KakaoOAuthService oAuthService){
        this.oAuthService = oAuthService;
    }

    @GetMapping("/oauth2/code/kakao")
    public String kakaoRedirect(@RequestParam("code") String code, HttpServletResponse response){
        MemberTokenResponse kakaoToken = oAuthService.login(code);

        addTokenCookie(response, kakaoToken.token());

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
