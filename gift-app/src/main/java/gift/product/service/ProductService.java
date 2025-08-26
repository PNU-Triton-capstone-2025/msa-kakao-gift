package gift.product.service;

import gift.common.page.PageResponse;
import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.dto.ProductEditRequestDto;
import gift.product.dto.ProductOptionResponseDto;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ProductService {

    private final RestClient restClient;

    public ProductService(RestClient.Builder restClientBuilder, @Value("${api.gateway.uri}") String gatewayUri) {
        this.restClient = restClientBuilder
                .baseUrl(gatewayUri)
                .build();
    }

    public Product getProductWithOptions(Long productId, String token) {
        Product product = getProduct(productId, token);

        List<ProductOptionResponseDto> optionsDto = restClient.get()
                .uri("/api/products/{productId}/options", productId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (optionsDto != null) {
            optionsDto.stream()
                    .map(dto -> new ProductOption(dto.id(), dto.name(), dto.quantity()))
                    .forEach(product::addProductOption);
        }

        return product;
    }

    public void saveProduct(ProductRequestDto requestDto, String token) {
        restClient.post()
                .uri("/api/admin/products")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(requestDto)
                .retrieve()
                .toBodilessEntity();
    }

    public Page<Product> getProducts(Pageable pageable, String token) {
        var pageResp = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/products")
                            .queryParam("page", pageable.getPageNumber())
                            .queryParam("size", pageable.getPageSize());
                    pageable.getSort().forEach(o ->
                            uriBuilder.queryParam("sort", o.getProperty() + "," + o.getDirection())
                    );
                    return uriBuilder.build();
                })
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<
                        PageResponse<ProductResponseDto>>() {});

        if (pageResp == null) return Page.empty(pageable);

        var content = pageResp.content().stream()
                .map(dto -> new Product(dto.id(), dto.name(), dto.price(), dto.imageUrl()))
                .toList();

        return new PageImpl<>(content, pageable, pageResp.totalElements());
    }

    public Product getProduct(Long id, String token) {
        ProductResponseDto responseDto = restClient.get()
                .uri("/api/products/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(ProductResponseDto.class);

        if (responseDto == null) throw new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + id);
        return new Product(responseDto.id(), responseDto.name(), responseDto.price(), responseDto.imageUrl());
    }

    public void update(Long id, ProductEditRequestDto requestDto, String token) {
        restClient.put()
                .uri("/api/admin/products/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(requestDto)
                .retrieve()
                .toBodilessEntity();
    }

    public void delete(Long id, String token) {
        restClient.delete()
                .uri("/api/admin/products/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toBodilessEntity();
    }
}