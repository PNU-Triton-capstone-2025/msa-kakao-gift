package gift.order.service;

import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private final RestClient restClient;

    public OrderService(RestClient.Builder restClientBuilder, @Value("${api.gateway.uri}") String gatewayUri) {
        restClient = restClientBuilder
                .baseUrl(gatewayUri)
                .build();
    }

    public OrderResponseDto createOrder(OrderRequestDto requestDto, String token) {
        return restClient.post()
                .uri("/api/orders")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(requestDto)
                .retrieve()
                .body(OrderResponseDto.class);
    }
}
