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
    public String productDetail(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            Model model){

        String token = AuthUtil.extractToken(request);
        Product product = productService.getProduct(id, token);

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
            HttpServletRequest request
    ){
        if(bindingResult.hasErrors()){
            return "admin/product-add-form";
        }

        String token = AuthUtil.extractToken(request);

        productService.saveProduct(requestDto, token);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(
            @PathVariable("id") Long id,
            HttpServletRequest reqeust,
            Model model){

        String token = AuthUtil.extractToken(reqeust);
        Product product = productService.getProduct(id, token);

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
            HttpServletRequest request,
            BindingResult bindingResult,
            Model model
    ){
        if(bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            return "/admin/product-edit-form";
        }
        String token = AuthUtil.extractToken(request);
        productService.update(id, requestDto, token);

        return "redirect:/admin/products/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(
            HttpServletRequest reqeust,
            @PathVariable("id") Long id) {

        String token = AuthUtil.extractToken(reqeust);
        productService.delete(id, token);

        return "redirect:/admin/products";
    }
}
