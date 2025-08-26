package gift.order.controller;

import gift.auth.AuthUtil;
import gift.auth.Login;
import gift.member.dto.MemberTokenRequest;
import gift.order.dto.OrderRequestDto;
import gift.order.service.OrderService;
import gift.product.domain.Product;
import gift.product.service.ProductService;
import gift.wish.domain.Wish;
import gift.wish.dto.WishResponse;
import gift.wish.service.WishService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final ProductService productService;

    public OrderController(WishService wishService, OrderService orderService,  ProductService productService) {
        this.wishService = wishService;
        this.orderService = orderService;
        this.productService = productService;
    }

    @GetMapping("/form")
    public String orderForm(@RequestParam("wishId") Long wishId, Model model, HttpServletRequest request) {
        String token = AuthUtil.extractToken(request);

        WishResponse wishResponse = wishService.getWishResponse(wishId, token);
        Product productWithOptions = productService.getProductWithOptions(wishResponse.productId(), token);

        model.addAttribute("product", productWithOptions);
        model.addAttribute("order", OrderRequestDto.getDefault(wishResponse.quantity()));

        return "order/order-form";
    }

    @PostMapping("/create")
    public String createOrder(HttpServletRequest request, @ModelAttribute("order") OrderRequestDto orderRequestDto) {
        String token = AuthUtil.extractToken(request);
        orderService.createOrder(orderRequestDto, token);
        return "redirect:/orders/success";
    }

    @GetMapping("/success")
    public String orderSuccess() {
        return "order/order-success";
    }
}
