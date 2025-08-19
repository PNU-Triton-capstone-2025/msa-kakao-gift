package gift.product.dto;

import gift.product.validation.ValidProductName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProductRequestDto(
        @ValidProductName
        String name,

        @NotNull(message = "가격은 필수입니다.")
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        @Max(value = 200_000_000, message = "가격은 20억 이하이어야 합니다.")
        Integer price,

        @NotBlank(message = "이미지 URL은 필수입니다.")
        @Size(max = 2000, message = "이미지 URL은 2000자 이내여야 합니다.")
        String imageUrl,

        @NotEmpty(message = "상품에는 최소 1개 이상의 옵션이 필요합니다.")
        @Valid
        List<ProductOptionRequestDto> options
){
    private static final String DEFAULT_NAME = "Default";
    private static final int DEFAULT_PRICE = 0;
    private static final String DEFAULT_IMAGE_URL = "";

    public static ProductRequestDto getEmpty() {
        return new ProductRequestDto(
                DEFAULT_NAME,
                DEFAULT_PRICE,
                DEFAULT_IMAGE_URL,
                List.of(ProductOptionRequestDto.getEmpty()));
    }
}
