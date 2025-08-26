package gift.wish.service;

import gift.common.page.PageResponse;
import gift.wish.dto.WishListResponse;
import gift.wish.dto.WishRequest;
import gift.wish.dto.WishResponse;
import gift.wish.dto.WishUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WishService {

    private final RestClient restClient;

    public WishService(RestClient.Builder restClientBuilder, @Value("${api.gateway.uri}") String gatewayUri) {
        this.restClient = restClientBuilder
                .baseUrl(gatewayUri)
                .build();
    }

    public WishResponse getWishResponse(Long wishId, String token) {
        WishResponse wishResponse = restClient.get()
                .uri("/api/wishes/{wishId}", wishId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(WishResponse.class);

        if (wishResponse == null) {
            throw new IllegalArgumentException("존재하지 않는 위시리스트 항목입니다.");
        }

        return wishResponse;
    }

    public Page<WishListResponse> getWishes(Pageable pageable, String token) {
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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

    public void addWish(WishRequest request, String token) {
        restClient.post()
                .uri("/api/wishes")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void updateQuantity(Long wishId, WishUpdateRequest request, String token) {
        restClient.patch()
                .uri("/api/wishes/{wishId}", wishId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteWish(Long wishId, String token) {
        restClient.delete()
                .uri("/api/wishes/{wishId}", wishId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toBodilessEntity();
    }
}