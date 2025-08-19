package gift.order.controller;

import gift.auth.Login;
import gift.member.dto.MemberTokenRequest;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderApiController {
    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Login MemberTokenRequest memberTokenRequest, @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.createOrder(requestDto, memberTokenRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
