package gift.order.service;

import gift.member.dto.MemberTokenRequest;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private final RestClient restClient;

    public OrderService(RestClient.Builder restClientBuilder) {
        restClient = restClientBuilder
                .baseUrl("http://localhost:8085")
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
