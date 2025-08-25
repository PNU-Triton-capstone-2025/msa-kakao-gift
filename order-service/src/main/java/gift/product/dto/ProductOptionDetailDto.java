package gift.product.dto;

public record ProductOptionDetailDto(
        Long optionId,
        Long productId,
        String productName,
        String optionName
) {}
