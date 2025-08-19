package gift.product.service;

import gift.product.domain.Product;
import gift.product.dto.ProductEditRequestDto;
import gift.product.dto.ProductOptionRequestDto;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Transactional
    public Product saveProduct(ProductRequestDto requestDto){
        Product product = new Product(requestDto.name(), requestDto.price(), requestDto.imageUrl());
        requestDto.options()
                .stream()
                .map(ProductOptionRequestDto::toEntity)
                .forEach(product::addProductOption);

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProducts(Pageable pageable){
        Page<Product> productPage = productRepository.findAll(pageable);

        return productPage.getContent().stream()
                .map(ProductResponseDto::productFrom)
                .toList();
    }

    @Transactional(readOnly = true)
    public Product getProduct(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + id));
    }

    @Transactional
    public void update(Long id, ProductEditRequestDto requestDto){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + id));
        product.updateProduct(requestDto.name(), requestDto.price(), requestDto.imageUrl());
    }

    @Transactional
    public void delete(Long id){
        productRepository.deleteById(id);
    }
}
