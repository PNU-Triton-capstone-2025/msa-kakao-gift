package gift.wish.dto;

public record WishListResponse(
        Long wishId,
        Long product_id,
        Integer quantity
) {
    public static WishListResponse getWishListResponse(WishInfo wishInfo){
        return new WishListResponse(
                wishInfo.wishId(),
                wishInfo.product_id(),
                wishInfo.quantity());
    }
}
