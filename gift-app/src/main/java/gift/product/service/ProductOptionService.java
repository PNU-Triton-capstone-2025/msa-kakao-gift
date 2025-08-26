package gift.product.service;

import gift.product.dto.ProductOptionRequestDto;
import gift.product.dto.ProductOptionResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ProductOptionService {

    private final RestClient restClient;

    public ProductOptionService(RestClient.Builder restClientBuilder, @Value("${api.gateway.uri}") String gatewayUri) {
        this.restClient = restClientBuilder
            .baseUrl(gatewayUri)
            .build();
    }

    @Transactional(readOnly = true)
    public List<ProductOptionResponseDto> getProductOptions(Long productId, String token) {
        return restClient.get()
                .uri("/api/products/{productId}/options", productId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Transactional
    public void addNewOption(Long productId, ProductOptionRequestDto optionRequestDto, String token){
        restClient.post()
                .uri("/api/products/{productId}/options", productId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(optionRequestDto)
                .retrieve()
                .toBodilessEntity();
    }

    @Transactional
    public void deleteOption(Long optionId, String token) {
        restClient.delete()
                .uri("/api/products/options/{optionId}", optionId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toBodilessEntity();
    }
}