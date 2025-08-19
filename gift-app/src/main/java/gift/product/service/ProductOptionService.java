package gift.product.service;

import gift.product.dto.ProductOptionRequestDto;
import gift.product.dto.ProductOptionResponseDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ProductOptionService {

    private final RestClient restClient;

    public ProductOptionService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
            .baseUrl("http://localhost:8081")
            .build();
    }

    @Transactional(readOnly = true)
    public List<ProductOptionResponseDto> getProductOptions(Long productId) {
        return restClient.get().uri("/api/products/{productId}/options", productId).retrieve().body(new ParameterizedTypeReference<>() {});
    }

    @Transactional
    public void addNewOption(Long productId, ProductOptionRequestDto optionRequestDto){
        restClient.post().uri("/api/products/{productId}/options", productId).body(optionRequestDto).retrieve().toBodilessEntity();
    }

    @Transactional
    public void deleteOption(Long optionId) {
        restClient.delete().uri("/api/products/options/{optionId}", optionId).retrieve().toBodilessEntity();
    }
}