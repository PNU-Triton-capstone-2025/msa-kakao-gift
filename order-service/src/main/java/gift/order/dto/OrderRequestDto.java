package gift.order.dto;

public record OrderRequestDto(
        Long optionId,
        Integer quantity,
        String message
) {}
