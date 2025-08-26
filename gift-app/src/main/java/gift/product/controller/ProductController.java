package gift.product.controller;

import gift.auth.AuthUtil;
import gift.common.enums.ProductSortProperty;
import gift.common.validation.ValidSort;
import gift.product.dto.ProductInfoDto;
import gift.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String products(
            @ValidSort(enumClass = ProductSortProperty.class)
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            HttpServletRequest request
    ) {
        String token = AuthUtil.extractToken(request);

        Page<ProductInfoDto> products = productService.getProducts(pageable, token)
                .map(product -> ProductInfoDto.productFrom(product));

        model.addAttribute("products", products);

        return "products/products";
    }
}
