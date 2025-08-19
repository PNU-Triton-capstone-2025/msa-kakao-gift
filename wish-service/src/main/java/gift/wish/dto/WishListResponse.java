package gift.wish.dto;

import gift.product.dto.ProductResponseDto;

public record WishListResponse(
        Long wishId,
        Long product_id,
        String productName,
        Integer productPrice,
        String productImageUrl,
        Integer quantity
) {
    public static WishListResponse getWishListResponse(WishInfo wishInfo, ProductResponseDto productResponseDto) {
        return new WishListResponse(
                wishInfo.wishId(),
                wishInfo.product_id(),
                productResponseDto.name(),
                productResponseDto.price(),
                productResponseDto.imageUrl(),
                wishInfo.quantity());
    }
}