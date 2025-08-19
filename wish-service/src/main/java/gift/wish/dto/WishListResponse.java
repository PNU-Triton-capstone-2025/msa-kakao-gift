package gift.wish.dto;

public record WishListResponse(
        Long wishId,
        Long product_id,
        String productName,
        Integer productPrice,
        String productImageUrl,
        Integer quantity
) {
    public static WishListResponse getWishListResponse(WishInfo wishInfo){
        return new WishListResponse(
                wishInfo.wishId(),
                wishInfo.product_id(),
                wishInfo.productName(),
                wishInfo.productPrice(),
                wishInfo.productImageUrl(),
                wishInfo.quantity());
    }
}
