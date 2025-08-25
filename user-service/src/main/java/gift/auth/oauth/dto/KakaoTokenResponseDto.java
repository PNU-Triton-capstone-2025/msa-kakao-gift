package gift.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenResponseDto(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken
) {
}
