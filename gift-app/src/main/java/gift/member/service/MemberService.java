package gift.member.service;

import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberRegisterRequest;
import gift.member.dto.MemberTokenRequest;
import gift.member.dto.MemberTokenResponse;
import gift.member.dto.MemberUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class MemberService {

    private final RestClient restClient;

    public MemberService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8085")
                .build();
    }

    public MemberTokenResponse register(MemberRegisterRequest request) {
        return restClient.post()
                .uri("/api/members/register")
                .body(request)
                .retrieve()
                .body(MemberTokenResponse.class);
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
        return restClient.post()
                .uri("/api/members/login")
                .body(request)
                .retrieve()
                .body(MemberTokenResponse.class);
    }

    public void updatePassword(MemberTokenRequest memberTokenRequest, MemberUpdateRequest request) {
        restClient.patch()
                .uri("/api/members/edit")
                .header("Authorization", "Bearer " + memberTokenRequest.token())
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteMember(MemberTokenRequest memberTokenRequest, String password) {
        restClient.post()
                .uri("/api/members/delete?password={password}", password)
                .header("Authorization", "Bearer " + memberTokenRequest.token())
                .retrieve()
                .toBodilessEntity();
    }
}