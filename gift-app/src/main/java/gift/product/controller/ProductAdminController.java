package gift.product.controller;

import gift.auth.AuthUtil;
import gift.auth.Login;
import gift.common.enums.ProductSortProperty;
import gift.common.validation.ValidSort;
import gift.member.dto.MemberTokenRequest;
import gift.product.domain.Product;
import gift.product.dto.ProductEditRequestDto;
import gift.product.dto.ProductInfoDto;
import gift.product.dto.ProductRequestDto;
import gift.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService){
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

        return "admin/products";
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model){
        Product product = productService.getProduct(id);

        model.addAttribute("product", ProductInfoDto.productFrom(product));

        return "admin/product-detail";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", ProductRequestDto.getEmpty());
        return "admin/product-add-form";
    }

    @PostMapping("/add")
    public String addProduct(
            @ModelAttribute("product") @Valid ProductRequestDto requestDto,
            BindingResult bindingResult,
            @Login MemberTokenRequest memberToken
    ){
        if(bindingResult.hasErrors()){
            return "admin/product-add-form";
        }

        productService.saveProduct(requestDto, memberToken);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model){
        Product product = productService.getProduct(id);

        model.addAttribute("product", new ProductEditRequestDto(
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        ));
        model.addAttribute("productId", id);

        return "admin/product-edit-form";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(
            @PathVariable("id") Long id,
            @ModelAttribute("product") @Valid ProductEditRequestDto requestDto,
            @Login MemberTokenRequest memberToken,
            BindingResult bindingResult,
            Model model
    ){
        if(bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            return "/admin/product-edit-form";
        }

        productService.update(id, requestDto, memberToken);

        return "redirect:/admin/products/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(
            @Login MemberTokenRequest memberToken,
            @PathVariable("id") Long id) {
        productService.delete(id, memberToken);

        return "redirect:/admin/products";
    }
}
