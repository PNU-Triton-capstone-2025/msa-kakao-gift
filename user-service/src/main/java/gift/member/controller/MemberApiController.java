package gift.member.controller;

import gift.member.domain.Member;
import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberRegisterRequest;
import gift.member.dto.MemberTokenResponse;
import gift.member.dto.MemberTokenRequest;
import gift.member.dto.MemberUpdateRequest;
import gift.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {
    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{id}/kakao-token")
    public ResponseEntity<Map<String, String>> getKakaoAccessToken(@PathVariable("id") Long id) {
        Member member = memberService.getKakaoMember(id);

        Map<String, String> response = Map.of("accessToken", member.getKakaoAccessToken());
        return ResponseEntity.ok(response);
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

    @PatchMapping("/edit")
    public ResponseEntity<Void> update(
            @RequestHeader("X-Member-Id") Long memberId,
            @Valid @RequestBody MemberUpdateRequest memberUpdateRequest) {

        memberService.updatePassword(memberId, memberUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-Member-Id") Long memberId,
            @RequestParam("password") String password) {

        memberService.deleteMember(memberId, password);
        return ResponseEntity.noContent().build();
    }
}