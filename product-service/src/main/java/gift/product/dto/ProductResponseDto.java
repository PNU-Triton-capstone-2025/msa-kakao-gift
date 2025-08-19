package gift.product.dto;

import gift.product.domain.Product;

public record ProductResponseDto(
        Long id,
        String name,
        int price,
        String imageUrl
) {
    public static ProductResponseDto productFrom(Product product){
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }
}
