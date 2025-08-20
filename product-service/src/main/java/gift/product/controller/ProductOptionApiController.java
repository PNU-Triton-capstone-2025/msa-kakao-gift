package gift.product.controller;

import gift.product.dto.ProductOptionRequestDto;
import gift.product.dto.ProductOptionResponseDto;
import gift.product.service.ProductOptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductOptionApiController {
    private final ProductOptionService optionService;

    public ProductOptionApiController(ProductOptionService optionService){
        this.optionService = optionService;
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<List<ProductOptionResponseDto>> getOptions(@PathVariable Long productId) {
        List<ProductOptionResponseDto> options = optionService.getProductOptions(productId);
        return ResponseEntity.ok(options);
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<Void> addOption(@PathVariable Long productId, @Valid @RequestBody ProductOptionRequestDto requestDto) {
        optionService.addNewOption(productId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/options/{optionId}/subtract-quantity")
    public ResponseEntity<Void> subtractQuantity(@PathVariable Long optionId, @RequestBody Map<String, Integer> payload) {
        Integer quantity = payload.get("quantity");
        if (quantity == null || quantity < 0) {
            return ResponseEntity.badRequest().build();
        }
        optionService.subtractOption(optionId, quantity);
        return ResponseEntity.noContent().build();
    }
}