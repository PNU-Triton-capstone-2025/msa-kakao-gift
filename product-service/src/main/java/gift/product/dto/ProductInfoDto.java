package gift.product.dto;

import gift.product.domain.Product;

public record ProductInfoDto (
        Long id,
        String name,
        int price,
        String imageUrl

) {
    public static ProductInfoDto productFrom(Product product){
        return new ProductInfoDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }
}