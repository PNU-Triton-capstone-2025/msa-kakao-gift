package gift.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.product.domain.Product;

public record KakaoMessageDto(
        @JsonProperty("object_type")
        String objectType,
        @JsonProperty("content")
        Content content,
        @JsonProperty("commerce")
        Commerce commerce,
        @JsonProperty("buttons")
        Button[] buttons
) {
    public record Content(
            String title,
            @JsonProperty("image_url")
            String imageUrl,
            @JsonProperty("image_width")
            Integer imageWidth,
            @JsonProperty("image_height")
            Integer imageHeight,
            Link link
    ) {

    }

    public record Commerce(
            @JsonProperty("regular_price")
            Integer regularPrice
    ) {

    }

    public record Button(
        String title,
        Link link
    ) {

    }

    public record Link(
        @JsonProperty("web_url")
        String webUrl,
        @JsonProperty("mobile_web_url")
        String mobileWebUrl
    ) {

    }

    public static KakaoMessageDto createCommerceTemplate(Product product) {
        String productUrl = "http://localhost:8080/products/" + product.getId();

        Content content = new Content(
                product.getName(),
                product.getImageUrl(),
                640,
                640,
                new Link(productUrl, productUrl)
        );

        Commerce commerce = new Commerce(
                product.getPrice()
        );

        Button[] buttons = new Button[]{
                new Button("상품 보러가기", new Link(productUrl, productUrl)),
                new Button("공유하기", new Link(productUrl, productUrl))
        };

        return new KakaoMessageDto("commerce", content, commerce, buttons);
    }
}
