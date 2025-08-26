package gift.order.service;

import gift.order.domain.Order;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.repository.OrderRepository;
import gift.product.dto.ProductOptionDetailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final RestClient productRestClient;
    private final RestClient wishRestClient;

    public OrderService(OrderRepository orderRepository, RestClient.Builder restClientBuilder) {
        this.orderRepository = orderRepository;
        this.productRestClient = restClientBuilder
                .clone()
                .baseUrl("http://localhost:8081")
                .build();
        this.wishRestClient = restClientBuilder
                .clone()
                .baseUrl("http://localhost:8082")
                .build();
    }

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, Long memberId) {
        subtractProductStock(requestDto.optionId(), requestDto.quantity());

        Long productId = getProductIdForOption(requestDto.optionId());

        Order order = new Order(requestDto.optionId(), memberId, requestDto.quantity(), requestDto.message());
        orderRepository.save(order);

        deleteWish(memberId, productId);

        // TODO: 카카오 메시지 발송 로직은 향후 'notification-service'로 분리될 예정입니다.

        return OrderResponseDto.from(order);
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
