package gift.product.dto;

import gift.product.domain.ProductOption;

public record ProductOptionDetailDto(
        Long optionId,
        Long productId,
        String productName,
        String optionName
) {
    public static ProductOptionDetailDto from(ProductOption option) {
        return new ProductOptionDetailDto(
                option.getId(),
                option.getProduct().getId(),
                option.getProduct().getName(),
                option.getName()
        );
    }
}
