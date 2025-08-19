package gift.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfoResponseDto(
        Long id,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {
    public record KakaoAccount(
       Profile profile,
       String email
    ) {}

    public record Profile(
            String nickname
    ) {}
}
