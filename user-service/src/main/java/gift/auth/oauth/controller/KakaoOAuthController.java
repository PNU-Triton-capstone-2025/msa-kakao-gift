package gift.auth.oauth.controller;

import gift.auth.oauth.service.KakaoOAuthService;
import gift.member.dto.MemberTokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<MemberTokenResponse> kakaoRedirect(@RequestParam("code") String code){
        MemberTokenResponse kakaoToken = oAuthService.login(code);
        return ResponseEntity.ok(kakaoToken);
    }
}
