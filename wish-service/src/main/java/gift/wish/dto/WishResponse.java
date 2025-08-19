package gift.wish.dto;

public record WishResponse(
        Long memberId,
        Long productId,
        int quantity
) {
}
