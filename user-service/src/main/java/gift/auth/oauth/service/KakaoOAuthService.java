package gift.auth.oauth.service;

import gift.auth.JwtUtil;
import gift.auth.oauth.KakaoApiClient;
import gift.auth.oauth.dto.KakaoTokenResponseDto;
import gift.auth.oauth.dto.KakaoUserInfoResponseDto;
import gift.member.domain.Member;
import gift.member.domain.RoleType;
import gift.member.dto.MemberTokenResponse;
import gift.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class KakaoOAuthService {
    private final KakaoApiClient loginApiClient;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public KakaoOAuthService(KakaoApiClient loginApiClient, MemberRepository memberRepository, JwtUtil jwtUtil){
        this.loginApiClient = loginApiClient;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public MemberTokenResponse login(String authCode){
        KakaoTokenResponseDto token = loginApiClient.fetchAccessToken(authCode);
        KakaoUserInfoResponseDto userInfo = loginApiClient.fetchUserInfo(token.accessToken());

        Member member = memberRepository.findByKakaoId(userInfo.id())
                .orElseGet(() -> registerMemberByOAuth(userInfo, token));

        String memberToken = jwtUtil.generateToken(member);
        return new MemberTokenResponse(memberToken);
    }

    private Member registerMemberByOAuth(KakaoUserInfoResponseDto userInfo, KakaoTokenResponseDto token){
        Member newMember = new Member(
                userInfo.kakaoAccount().email(),
                UUID.randomUUID().toString(),
                RoleType.USER,
                userInfo.id(),
                token.accessToken(),
                token.refreshToken()
        );
        return memberRepository.save(newMember);
    }
}
