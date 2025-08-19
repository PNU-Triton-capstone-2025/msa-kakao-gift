package gift.product.dto;

import gift.product.domain.ProductOption;

public record ProductOptionResponseDto(
        Long id,
        String name,
        Integer quantity
) {
    public static ProductOptionResponseDto from(ProductOption productOption){
        return new ProductOptionResponseDto(productOption.getId(), productOption.getName(), productOption.getQuantity());
    }
}
