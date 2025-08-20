package gift.order.service;

import gift.auth.oauth.event.KakaoOrderCompletedEvent;
import gift.member.domain.Member;
import gift.member.dto.MemberTokenRequest;
import gift.member.repository.MemberRepository;
import gift.order.domain.Order;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.repository.OrderRepository;
import gift.product.domain.ProductOption;
import gift.product.repository.ProductOptionRepository;
import gift.wish.repository.WishRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private final RestClient restClient;

    public OrderService(RestClient.Builder restClientBuilder) {
        restClient = restClientBuilder
                .baseUrl("http://localhost:8083")
                .build();
    }

    public OrderResponseDto createOrder(OrderRequestDto requestDto, MemberTokenRequest memberTokenRequest) {
        return restClient.post()
                .uri("/api/orders")
                .header("X-Member-Id", String.valueOf(memberTokenRequest.id()))
                .body(requestDto)
                .retrieve()
                .body(OrderResponseDto.class);
    }
}
