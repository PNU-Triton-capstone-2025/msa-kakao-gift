package gift.member.controller;

import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberTokenResponse;
import gift.member.dto.MemberRegisterRequest;
import gift.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {
    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<MemberTokenResponse> register(@Valid @RequestBody MemberRegisterRequest request) {
        MemberTokenResponse response = memberService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<MemberTokenResponse> registerAdmin(@Valid @RequestBody MemberRegisterRequest request) {
        MemberTokenResponse response = memberService.registerAdmin(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberTokenResponse> login(@RequestBody MemberLoginRequest request) {
        MemberTokenResponse response = memberService.login(request);

        return ResponseEntity.ok(response);
    }

}