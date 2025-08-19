package gift.wish.service;

import gift.member.domain.Member;
import gift.member.dto.MemberTokenRequest;
import gift.product.domain.Product;
import gift.product.service.ProductService;
import gift.wish.domain.Wish;
import gift.wish.dto.WishListResponse;
import gift.wish.dto.WishRequest;
import gift.wish.dto.WishResponse;
import gift.wish.dto.WishUpdateRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public Wish getWish(MemberTokenRequest memberTokenRequest, Long wishId) {
        WishResponse wishResponse = restClient.get()
                .uri("/api/wishes/{wishId}", wishId)
                .header("X-Member-Id", String.valueOf(memberTokenRequest.id()))
                .retrieve()
                .body(WishResponse.class);

        if (wishResponse == null) {
            throw new IllegalArgumentException("존재하지 않는 위시리스트 항목입니다.");
        }

        Product product = productService.getProduct(wishResponse.productId());
        Member member = new Member(memberTokenRequest.id(), memberTokenRequest.email(), memberTokenRequest.password(), memberTokenRequest.role());

        return new Wish(member, product, wishResponse.quantity());
    }

    public void addWish(MemberTokenRequest memberTokenRequest, Long productId) {
        restClient.post()
                .uri("/api/wishes")
                .header("X-Member-Id", String.valueOf(memberTokenRequest.id()))
                .body(new WishRequest(productId))
                .retrieve()
                .toBodilessEntity();
    }

    @Transactional(readOnly = true)
    public Page<WishListResponse> getWishes(MemberTokenRequest memberTokenRequest, Pageable pageable) {
        List<WishListResponse> basicWishes = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/wishes")
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .build())
                .header("X-Member-Id", String.valueOf(memberTokenRequest.id()))
                .retrieve()
                .body(new ParameterizedTypeReference<List<WishListResponse>>() {});

        if (basicWishes == null || basicWishes.isEmpty()) {
            return Page.empty(pageable);
        }

        List<WishListResponse> enrichedWishes = basicWishes.stream()
                .map(wish -> {
                    Product product = productService.getProduct(wish.product_id());
                    return new WishListResponse(
                            wish.wishId(),
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            product.getImageUrl(),
                            wish.quantity()
                    );
                })
                .collect(Collectors.toList());

        // ⚠️ 주의: 이 방식은 전체 데이터 수를 모르므로, 페이지네이션 UI가 정확하지 않을 수 있습니다.
        return new PageImpl<>(enrichedWishes, pageable, enrichedWishes.size());
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
