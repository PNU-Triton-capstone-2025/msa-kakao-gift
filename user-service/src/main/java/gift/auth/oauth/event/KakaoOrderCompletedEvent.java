package gift.auth.oauth.event;

public record KakaoOrderCompletedEvent (
    String kakaoAccessToken,
    String kakaoRefreshToken,
    Long productId
) {

}
