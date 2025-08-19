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

    public OrderService(OrderRepository orderRepository, RestClient.Builder restClientBuilder) {
        this.orderRepository = orderRepository;
        this.productRestClient = restClientBuilder
                .baseUrl("http://localhost:8081") // product-service의 주소
                .build();
    }

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, Long memberId) {
        // 1. product-service에 API를 호출하여 재고를 차감합니다.
        //    (존재하지 않는 옵션이거나 재고가 부족하면 product-service에서 예외를 발생시킬 것입니다)
        subtractProductStock(requestDto.optionId(), requestDto.quantity());

        // 2. 재고 차감이 성공하면, 주문을 생성하고 자신의 DB에 저장합니다.
        Order order = new Order(requestDto.optionId(), memberId, requestDto.quantity(), requestDto.message());
        orderRepository.save(order);

        // TODO: 주문 완료 후 wish-service에 API를 호출하여 해당 상품을 위시리스트에서 제거하는 로직 추가
        // TODO: 카카오 사용자인 경우, 외부 Kakao API를 호출하여 메시지를 발송하는 로직 추가

        return OrderResponseDto.from(order);
    }

    private void subtractProductStock(Long optionId, Integer quantity) {
        productRestClient.patch()
                .uri("/api/products/options/{optionId}/subtract-quantity", optionId)
                .body(Map.of("quantity", quantity))
                .retrieve()
                .toBodilessEntity();
    }
}
