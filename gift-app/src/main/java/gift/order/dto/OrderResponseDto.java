package gift.order.dto;

import gift.order.domain.Order;

import java.time.LocalDateTime;

public record OrderResponseDto(
        Long id,
        Long optionId,
        Integer quantity,
        LocalDateTime orderDateTime,
        String message
) {
    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getProductOption().getId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        );
    }
}
