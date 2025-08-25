package gift.wish.service;

import gift.common.page.PageResponse;
import gift.member.domain.Member;
import gift.member.dto.MemberTokenRequest;
import gift.product.domain.Product;
import gift.product.service.ProductService;
import gift.wish.domain.Wish;
import gift.wish.dto.WishListResponse;
import gift.wish.dto.WishRequest;
import gift.wish.dto.WishResponse;
import gift.wish.dto.WishUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WishService {

    private final RestClient restClient;
    private final ProductService productService;

    public WishService(RestClient.Builder restClientBuilder, ProductService productService) {
        this.productService = productService;
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:8082")
                .build();
    }

    public Wish getWish(MemberTokenRequest memberTokenRequest, Long wishId) {
        WishResponse wishResponse = restClient.get()
                .uri("/api/wishes/{wishId}", wishId)
                .header("X-Member-Id", String.valueOf(memberTokenRequest.id()))
                .retrieve()
                .body(WishResponse.class);

        if (wishResponse == null) {
            throw new IllegalArgumentException("존재하지 않는 위시리스트 항목입니다.");
        }

        Product product = productService.getProductWithOptions(wishResponse.productId());
        Member member = new Member(memberTokenRequest.id(), memberTokenRequest.email(), memberTokenRequest.password(), memberTokenRequest.role());

        return new Wish(member, product, wishResponse.quantity());
    }

    public Page<WishListResponse> getWishes(MemberTokenRequest memberTokenRequest, Pageable pageable) {
        var pageResp = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/wishes")
                            .queryParam("page", pageable.getPageNumber())
                            .queryParam("size", pageable.getPageSize());
                    pageable.getSort().forEach(o ->
                            uriBuilder.queryParam("sort", o.getProperty() + "," + o.getDirection())
                    );
                    return uriBuilder.build();
                })
                .header("X-Member-Id", String.valueOf(memberTokenRequest.id()))
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<
                        PageResponse<WishListResponse>>() {});

        if (pageResp == null) return Page.empty(pageable);

        return new PageImpl<>(
                pageResp.content(),
                pageable,
                pageResp.totalElements()
        );
    }

    public void addWish(MemberTokenRequest memberTokenRequest, Long productId) {
        restClient.post()
                .uri("/api/wishes")
                .header("X-Member-Id", String.valueOf(memberTokenRequest.id()))
                .body(new WishRequest(productId))
                .retrieve()
                .toBodilessEntity();
    }

    public void updateQuantity(MemberTokenRequest memberTokenRequest, Long wishId, Integer quantity) {
        restClient.patch()
                .uri("/api/wishes/{wishId}", wishId)
                .header("X-Member-Id", String.valueOf(memberTokenRequest.id()))
                .body(new WishUpdateRequest(quantity))
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteWish(MemberTokenRequest memberTokenRequest, Long wishId) {
        restClient.delete()
                .uri("/api/wishes/{wishId}", wishId)
                .header("X-Member-Id", String.valueOf(memberTokenRequest.id()))
                .retrieve()
                .toBodilessEntity();
    }
}