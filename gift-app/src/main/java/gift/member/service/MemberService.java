package gift.member.service;

import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberRegisterRequest;
import gift.member.dto.MemberTokenResponse;
import gift.member.dto.MemberUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private final RestClient restClient;

    public MemberService(RestClient.Builder builder, @Value("${api.gateway.uri}") String gatewayUri) {
        this.restClient = builder
                .baseUrl(gatewayUri)
                .build();
    }

    public MemberTokenResponse register(MemberRegisterRequest request) {
        try {
            return restClient.post()
                    .uri("/api/members/register")
                    .body(request)
                    .retrieve()
                    .body(MemberTokenResponse.class);
        } catch (RestClientException e) {
            log.error("user-service 호출 실패: 회원가입 요청 처리 중 오류가 발생했습니다.", e);
            throw new RuntimeException("회원가입 처리 중 오류가 발생했습니다.", e);
        }
    }

    public MemberTokenResponse registerAdmin(MemberRegisterRequest request) {
        return restClient.post()
                .uri("/api/members/register/admin")
                .body(request)
                .retrieve()
                .body(MemberTokenResponse.class);
    }

    public MemberTokenResponse registerMd(MemberRegisterRequest request) {
        return restClient.post()
                .uri("/api/members/register/md")
                .body(request)
                .retrieve()
                .body(MemberTokenResponse.class);
    }

    public MemberTokenResponse login(MemberLoginRequest request) {
        try {
            return restClient.post()
                    .uri("/api/members/login")
                    .body(request)
                    .retrieve()
                    .body(MemberTokenResponse.class);
        }
        catch (RestClientException e) {
            log.error("user-service 호출 실패: 로그인 요청 처리 중 오류가 발생했습니다.", e);
            throw new RuntimeException("로그인 처리 중 오류가 발생했습니다.", e);
        }
    }

    public void updatePassword(String token, MemberUpdateRequest request) {
        restClient.patch()
                .uri("/api/members/edit")
                .header("Authorization", "Bearer " + token)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteMember(String token, String password) {
        restClient.delete()
                .uri("/api/members/delete?password={password}", password)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity();
    }
}