package gift.order.service;

import gift.kakao.KakaoMessageClient;
import gift.kakao.dto.KakaoMessageDto;
import gift.order.domain.Order;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.repository.OrderRepository;
import gift.product.dto.ProductOptionDetailDto;
import gift.product.dto.ProductResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final KakaoMessageClient kakaoMessageClient;
    private final RestClient productRestClient;
    private final RestClient wishRestClient;
    private final RestClient userRestClient;
    private final String productBaseUrl;

    public OrderService(OrderRepository orderRepository,
                        RestClient.Builder restClientBuilder,
                        KakaoMessageClient kakaoMessageClient,
                        @Value("${service.product.uri}") String productUri,
                        @Value("${service.wish.uri}") String wishUri,
                        @Value("${service.user.uri}") String userUri,
                        @Value("${spring.front.domain}") String frontDomain) {
        this.orderRepository = orderRepository;
        this.kakaoMessageClient = kakaoMessageClient;
        this.productRestClient = restClientBuilder.clone().baseUrl(productUri).build();
        this.wishRestClient = restClientBuilder.clone().baseUrl(wishUri).build();
        this.userRestClient = restClientBuilder.clone().baseUrl(userUri).build();
        this.productBaseUrl = frontDomain;
    }

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, Long memberId) {
        subtractProductStock(requestDto.optionId(), requestDto.quantity());

        Long productId = getProductIdForOption(requestDto.optionId());

        Order order = new Order(requestDto.optionId(), memberId, requestDto.quantity(), requestDto.message());
        orderRepository.save(order);

        deleteWish(memberId, productId);

        String kakaoAccessToken = getKakaoAccessToken(memberId);

        if (kakaoAccessToken != null) {
            ProductResponseDto product = getProductInfo(productId);

            KakaoMessageDto message = KakaoMessageDto.createCommerceTemplate(product, this.productBaseUrl);
            kakaoMessageClient.sendMessageToMe(kakaoAccessToken, message);
        }

        return OrderResponseDto.from(order);
    }

    private String getKakaoAccessToken(Long memberId) {
        try {
            var response = userRestClient.get()
                    .uri("/api/members/{id}/kakao-token", memberId)
                    .retrieve()
                    .body(Map.class);
            return (response != null) ? (String) response.get("accessToken") : null;
        } catch (Exception e) {
            return null;
        }
    }

    private Long getProductIdForOption(Long optionId) {
        try {
            ProductOptionDetailDto optionDetail = productRestClient.get()
                    .uri("/api/products/options/{optionId}/detail", optionId)
                    .retrieve()
                    .body(ProductOptionDetailDto.class);

            if (optionDetail == null) {
                throw new IllegalStateException("상품 옵션 정보를 찾을 수 없습니다: " + optionId);
            }
            return optionDetail.productId();
        } catch (Exception e) {
            log.error("product-service에서 옵션 정보 조회 실패 - optionId: {}", optionId, e);
            throw new IllegalStateException("상품 정보를 조회하는 데 실패했습니다.", e);
        }
    }

    private ProductResponseDto getProductInfo(Long productId) {
        return productRestClient.get()
                .uri("/api/products/{id}", productId)
                .retrieve()
                .body(ProductResponseDto.class);
    }

    private void subtractProductStock(Long optionId, Integer quantity) {
        productRestClient.patch()
                .uri("/api/products/options/{optionId}/subtract-quantity", optionId)
                .body(Map.of("quantity", quantity))
                .retrieve()
                .toBodilessEntity();
    }

    private void deleteWish(Long memberId, Long productId) {
        try {
            wishRestClient.delete()
                    .uri("/api/wishes/member/{memberId}/product/{productId}", memberId, productId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("주문 완료 후 위시리스트 삭제 요청 실패 - memberId: {}, productId: {}", memberId, productId, e);
        }
    }
}
