package gift.product.dto;

public record ProductResponseDto(
        Long id,
        String name,
        int price,
        String imageUrl
) {}
