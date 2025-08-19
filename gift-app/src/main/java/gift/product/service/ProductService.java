package gift.product.service;

import gift.product.domain.Product;
import gift.product.dto.ProductEditRequestDto;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.exception.ProductNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final RestClient restClient;

    public ProductService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:8081") // 환경변수로 바꿀 예정
                .build();
    }

    @Transactional
    public void saveProduct(ProductRequestDto requestDto) {
        restClient.post().uri("/api/admin/products").body(requestDto).retrieve().toBodilessEntity();
    }

    @Transactional(readOnly = true)
    public Page<Product> getProducts(Pageable pageable) {
        // 이제 product-service는 List<DTO>를 반환합니다.
        List<ProductResponseDto> responseList = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/admin/products")
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProductResponseDto>>() {});

        if (responseList == null) {
            return Page.empty();
        }

        List<Product> productList = responseList.stream()
                .map(dto -> new Product(dto.id(), dto.name(), dto.price(), dto.imageUrl()))
                .collect(Collectors.toList());

        return new PageImpl<>(productList, pageable, productList.size());
    }

    @Transactional(readOnly = true)
    public Product getProduct(Long id) {
        ProductResponseDto responseDto = restClient.get().uri("/api/admin/products/{id}", id).retrieve().body(ProductResponseDto.class);

        if (responseDto == null) throw new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + id);
        return new Product(responseDto.id(), responseDto.name(), responseDto.price(), responseDto.imageUrl());
    }

    @Transactional
    public void update(Long id, ProductEditRequestDto requestDto) {
        restClient.put().uri("/api/admin/products/{id}", id).body(requestDto).retrieve().toBodilessEntity();
    }

    @Transactional
    public void delete(Long id) {
        restClient.delete().uri("/api/admin/products/{id}", id).retrieve().toBodilessEntity();
    }
}