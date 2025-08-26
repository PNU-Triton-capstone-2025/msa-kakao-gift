package gift.product.service;

import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.dto.ProductOptionDetailDto;
import gift.product.dto.ProductOptionRequestDto;
import gift.product.dto.ProductOptionResponseDto;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductOptionRepository;
import gift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductOptionService {
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    public ProductOptionService(ProductRepository productRepository, ProductOptionRepository productOptionRepository) {
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductOptionResponseDto> getProductOptions(Long productId) {
        return productOptionRepository.findByProductId(productId)
                .stream()
                .map(ProductOptionResponseDto::from)
                .toList();
    }

    @Transactional
    public void addNewOption(Long productId, ProductOptionRequestDto optionRequestDto){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));

        product.addProductOption(optionRequestDto.toEntity());
    }

    @Transactional
    public void deleteOption(Long optionId) {
        productOptionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

        productOptionRepository.deleteById(optionId);
    }

    @Transactional
    public void subtractOption(Long optionId, Integer quantity) {
        ProductOption option = productOptionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

        option.subtractQuantity(quantity);
    }

    @Transactional(readOnly = true)
    public ProductOptionDetailDto getProductOptionDetail(Long optionId) {
        ProductOption option = productOptionRepository.findById(optionId)
                .orElseThrow(() -> new ProductNotFoundException("옵션을 찾을 수 없습니다. ID: " + optionId));

        return ProductOptionDetailDto.from(option);
    }
}
