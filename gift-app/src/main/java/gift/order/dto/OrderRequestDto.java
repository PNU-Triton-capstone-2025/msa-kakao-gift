package gift.order.dto;

public record OrderRequestDto(
        Long optionId,
        Integer quantity,
        String message
) {
    public static final Long DEFAULT_OPTION_ID = null;
    public static final String DEFAULT_MESSAGE = "";

    public static OrderRequestDto getDefault(Integer quantity) {
        return new OrderRequestDto(
                DEFAULT_OPTION_ID,
                quantity,
                DEFAULT_MESSAGE
        );
    }
}
