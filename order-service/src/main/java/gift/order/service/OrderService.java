package gift.order.service;

import gift.order.domain.Order;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class OrderService {

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

        Order order = new Order(requestDto.optionId(), memberId, requestDto.quantity(), requestDto.message());
        orderRepository.save(order);

        deleteWish(memberId, requestDto.optionId());

        // TODO: 카카오 메시지 발송 로직은 향후 'notification-service'로 분리될 예정입니다.

        return OrderResponseDto.from(order);
    }

    private void subtractProductStock(Long optionId, Integer quantity) {
        productRestClient.patch()
                .uri("/api/products/options/{optionId}/subtract-quantity", optionId)
                .body(Map.of("quantity", quantity))
                .retrieve()
                .toBodilessEntity();
    }

    private void deleteWish(Long memberId, Long optionId) {
        try {
            // wish-service에 optionId가 아닌 productId로 삭제를 요청해야 합니다.
            // 지금은 optionId로 productId를 알 수 없으므로, 이 부분은 향후 리팩토링이 필요합니다.
            // 우선은 wish-service에 productId를 받는 삭제 API가 있다고 가정하고 호출합니다.
            // wishRestClient.delete()
            //     .uri("/api/wishes/product/{productId}", productId)
            //     .header("X-Member-Id", String.valueOf(memberId))
            //     .retrieve()
            //     .toBodilessEntity();
            // -> 이 부분은 wish-service에 해당 API가 없으므로 지금은 주석 처리합니다.
            //    실제 구현 시에는 product-service에서 optionId로 productId를 조회한 후 호출해야 합니다.
        } catch (Exception e) {
            // 위시리스트에 없어도 주문은 성공해야 하므로, 오류를 로깅만 하고 무시합니다.
            System.err.println("Wishlist item deletion failed: " + e.getMessage());
        }
    }
}
