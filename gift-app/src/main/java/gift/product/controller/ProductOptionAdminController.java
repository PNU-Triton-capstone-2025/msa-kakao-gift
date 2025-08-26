package gift.product.controller;

import gift.auth.AuthUtil;
import gift.product.domain.Product;
import gift.product.dto.ProductOptionRequestDto;
import gift.product.service.ProductOptionService;
import gift.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products/{productId}/options")
public class ProductOptionAdminController {
    private final ProductService productService;
    private final ProductOptionService optionService;

    public ProductOptionAdminController(ProductService productService, ProductOptionService optionService){
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping
    public String optionsPage(
            @PathVariable Long productId,
            HttpServletRequest request,
            Model model) {

        String token = AuthUtil.extractToken(request);
        Product product = productService.getProduct(productId, token);
        model.addAttribute("product", product);
        model.addAttribute("newOption", ProductOptionRequestDto.getEmpty());

        return "admin/product-options";
    }

    @PostMapping("/add")
    public String addOption(
            @PathVariable Long productId,
            @Valid @ModelAttribute("newOption") ProductOptionRequestDto optionRequestDto,
            HttpServletRequest request,
            BindingResult bindingResult,
            Model model
    ) {

        String token = AuthUtil.extractToken(request);

        if (bindingResult.hasErrors()) {
            Product product = productService.getProduct(productId, token);
            model.addAttribute("product", product);
            return "admin/product-options";
        }

        optionService.addNewOption(productId, optionRequestDto, token);

        return "redirect:/admin/products/" + productId + "/options";
    }

    @PostMapping("/delete/{optionId}")
    public String deleteOption(
            @PathVariable Long productId,
            @PathVariable Long optionId,
            HttpServletRequest request) {
        String token = AuthUtil.extractToken(request);
        optionService.deleteOption(optionId, token);

        return "redirect:/admin/products/" + productId + "/options";
    }
}
