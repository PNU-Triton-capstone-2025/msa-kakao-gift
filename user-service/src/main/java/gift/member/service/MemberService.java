package gift.member.service;

import gift.auth.JwtUtil;
import gift.member.domain.Member;
import gift.member.domain.RoleType;
import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberRegisterRequest;
import gift.member.dto.MemberTokenResponse;
import gift.member.dto.MemberUpdateRequest;
import gift.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public MemberTokenResponse register(MemberRegisterRequest request) {
        return registerMember(request, RoleType.USER);
    }

    @Transactional
    public MemberTokenResponse registerAdmin(MemberRegisterRequest request) {
        return registerMember(request, RoleType.ADMIN);
    }

    @Transactional
    public MemberTokenResponse registerMd(MemberRegisterRequest request) {
        return registerMember(request, RoleType.MD);
    }

    private MemberTokenResponse registerMember(MemberRegisterRequest request, RoleType roleType) {
        if(memberRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + request.email());
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = memberRepository.save(new Member(request.email(), encodedPassword, roleType));

        return new MemberTokenResponse(jwtUtil.generateToken(member));
    }

    @Transactional(readOnly = true)
    public MemberTokenResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return new MemberTokenResponse(jwtUtil.generateToken(member));
    }

    @Transactional
    public void updatePassword(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        member.updatePassword(passwordEncoder.encode(request.newPassword()));
    }

    @Transactional
    public void deleteMember(Long memberId, String password) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        memberRepository.deleteById(memberId);
    }
}
