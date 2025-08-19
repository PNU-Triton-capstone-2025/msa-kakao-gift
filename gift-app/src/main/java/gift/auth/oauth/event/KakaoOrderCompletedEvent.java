package gift.auth.oauth.event;

import gift.product.domain.Product;

public record KakaoOrderCompletedEvent (
    String kakaoAccessToken,
    String kakaoRefreshToken,
    Long productId
) {

}
