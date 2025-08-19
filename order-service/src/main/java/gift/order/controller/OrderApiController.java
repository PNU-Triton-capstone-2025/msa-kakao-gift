package gift.order.controller;

import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderApiController {
    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestHeader("X-Member-Id") Long memberId,
            @Valid @RequestBody OrderRequestDto requestDto) {

        OrderResponseDto responseDto = orderService.createOrder(requestDto, memberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
