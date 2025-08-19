package gift.order.controller;

import gift.auth.Login;
import gift.member.dto.MemberTokenRequest;
import gift.order.dto.OrderRequestDto;
import gift.order.service.OrderService;
import gift.wish.domain.Wish;
import gift.wish.service.WishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final WishService wishService;
    private final OrderService orderService;

    public OrderController(WishService wishService, OrderService orderService) {
        this.wishService = wishService;
        this.orderService = orderService;
    }

    @GetMapping("/form")
    public String orderForm(@RequestParam("wishId") Long wishId, Model model) {
        Wish wish = wishService.getWish(wishId);

        model.addAttribute("product", wish.getProduct());
        model.addAttribute("order", OrderRequestDto.getDefault(wish.getQuantity()));

        return "order/order-form";
    }

    @PostMapping("/create")
    public String createOrder(@Login MemberTokenRequest memberTokenRequest, @ModelAttribute("order") OrderRequestDto orderRequestDto) {
        orderService.createOrder(orderRequestDto, memberTokenRequest);
        return "redirect:/orders/success";
    }

    @GetMapping("/success")
    public String orderSuccess() {
        return "order/order-success";
    }
}
