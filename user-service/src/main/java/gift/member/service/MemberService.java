package gift.member.service;

import gift.auth.JwtUtil;
import gift.member.domain.Member;
import gift.member.domain.RoleType;
import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberRegisterRequest;
import gift.member.dto.MemberTokenRequest;
import gift.member.dto.MemberTokenResponse;
import gift.member.dto.MemberUpdateRequest;
import gift.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
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

        Member member = memberRepository.save(new Member(request.email(), request.password(), roleType));

        return new MemberTokenResponse(jwtUtil.generateToken(member));
    }

    @Transactional(readOnly = true)
    public MemberTokenResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        checkPassword(member.getPassword(), request.password(), "비밀번호가 일치하지 않습니다.");

        return new MemberTokenResponse(jwtUtil.generateToken(member));
    }

    @Transactional
    public void updatePassword(MemberTokenRequest memberTokenRequest, MemberUpdateRequest request) {
        checkPassword(memberTokenRequest.password(), request.password(), "현재 비밀번호가 일치하지 않습니다.");

        Member member = memberRepository.findById(memberTokenRequest.id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        member.updatePassword(request.password());
    }

    @Transactional
    public void deleteMember(MemberTokenRequest memberTokenRequest, String password) {
        checkPassword(memberTokenRequest.password(), password, "비밀번호가 일치하지 않습니다.");

        memberRepository.deleteById(memberTokenRequest.id());
    }

    private void checkPassword(String originPassword, String password, String msg) {
        if(!originPassword.equals(password)) {
            throw new IllegalArgumentException(msg);
        }
    }
}
