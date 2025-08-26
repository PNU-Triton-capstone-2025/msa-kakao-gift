package gift.product.controller;

import gift.common.enums.ProductSortProperty;
import gift.common.page.PageResponse;
import gift.common.validation.ValidSort;
import gift.product.domain.Product;
import gift.product.dto.ProductEditRequestDto;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class ProductApiAdminController {

    private final ProductService productService;

    public ProductApiAdminController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(@RequestBody @Valid ProductRequestDto requestDto) {
        productService.saveProduct(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping
    public ResponseEntity<PageResponse<ProductResponseDto>> getProducts(@PageableDefault(size = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<ProductResponseDto> page = productService.getProducts(pageable);

        return ResponseEntity.ok(PageResponse.from(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id){
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(ProductResponseDto.productFrom(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductEditRequestDto requestDto) {

        productService.update(id, requestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
