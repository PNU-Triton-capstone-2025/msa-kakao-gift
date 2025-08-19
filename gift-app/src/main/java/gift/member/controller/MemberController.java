package gift.member.controller;

import gift.auth.Login;
import gift.auth.oauth.KakaoProperties;
import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberRegisterRequest;
import gift.member.dto.MemberTokenRequest;
import gift.member.dto.MemberTokenResponse;
import gift.member.dto.MemberUpdateRequest;
import gift.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final KakaoProperties kakaoProperties;

    public MemberController(MemberService memberService, KakaoProperties kakaoProperties) {
        this.memberService = memberService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        String kakaoLoginUrl = getKakaoLoginUrl();

        model.addAttribute("member", MemberLoginRequest.getEmpty());
        model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);

        return "members/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("member") MemberLoginRequest loginRequest, HttpServletResponse response, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return "members/login";
        }
        MemberTokenResponse tokenResponse = memberService.login(loginRequest);

        addTokenCookie(response, tokenResponse.token());
        return "redirect:/products";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("member", MemberRegisterRequest.getEmpty());

        return "members/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("member") MemberRegisterRequest registerRequest,
                           BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            return "members/register";
        }

        MemberTokenResponse tokenResponse = memberService.register(registerRequest);
        addTokenCookie(response, tokenResponse.token());

        return "redirect:/products";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireTokenCookie(response);

        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String mypage(@Login MemberTokenRequest memberTokenRequest, Model model) {
        model.addAttribute("member", memberTokenRequest);

        return "members/mypage";
    }

    @GetMapping("/edit")
    public String editPassword(@Login MemberTokenRequest memberTokenRequest, Model model) {
        model.addAttribute("member", MemberUpdateRequest.getEmpty(memberTokenRequest.id()));

        return "members/edit-password-form";
    }

    @PostMapping("/edit")
    public String editPassword(@Login MemberTokenRequest memberTokenRequest, @Valid @ModelAttribute("member") MemberUpdateRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/edit-password-form";
        }

        memberService.updatePassword(memberTokenRequest, request);

        return "redirect:/members/mypage";
    }

    @PostMapping("/delete")
    public String deleteMember(@Login MemberTokenRequest memberTokenRequest, @RequestParam String password, HttpServletResponse response) {
        memberService.deleteMember(memberTokenRequest, password);
        expireTokenCookie(response);

        return "redirect:/";
    }

    private String getKakaoLoginUrl() {
        return String.format(
                "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=profile_nickname%%20account_email%%20talk_message",
                kakaoProperties.clientId(),
                kakaoProperties.redirectUri()
        );
    }

    private void addTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt-token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
    }

    private void expireTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt-token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
