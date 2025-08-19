package gift.product.dto;

import gift.product.domain.ProductOption;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductOptionRequestDto(
        @NotBlank
        @Size(max = 50, message = "옵션 이름은 50자 이내로 입력해주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]*$", message = "허용되지 않은 특수문자가 포함되어 있습니다.")
        String name,
        @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
        @Max(value = 99_999_999, message = "수량은 1억 미만이어야 합니다.")
        Integer quantity
) {
    private static final String DEFAULT_NAME = "";
    private static final Integer DEFAULT_QUANTITY = 1;

    public ProductOption toEntity(){
        return new ProductOption(name, quantity);
    }

    public static ProductOptionRequestDto getEmpty() {
        return new ProductOptionRequestDto(DEFAULT_NAME, DEFAULT_QUANTITY);
    }
}
