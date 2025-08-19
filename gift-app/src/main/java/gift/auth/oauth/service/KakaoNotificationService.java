package gift.auth.oauth.service;

import gift.auth.oauth.KakaoApiClient;
import gift.auth.oauth.dto.KakaoMessageDto;
import gift.auth.oauth.event.KakaoOrderCompletedEvent;
import gift.product.domain.Product;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
@Service
public class KakaoNotificationService {
    private final KakaoApiClient kakaoApiClient;
    private final ProductRepository productRepository;

    public KakaoNotificationService(KakaoApiClient kakaoApiClient, ProductRepository productRepository) {
        this.kakaoApiClient = kakaoApiClient;
        this.productRepository = productRepository;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderMessageToMe(KakaoOrderCompletedEvent event) {
        Product product = productRepository.findById(event.productId())
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + event.productId()));;
        // access token 만료 시 refresh token을 사용해서 갱신하도록 추후 보완 필요.
        KakaoMessageDto messageDto = KakaoMessageDto.createCommerceTemplate(product);
        kakaoApiClient.sendMessageToMe(event.kakaoAccessToken(), messageDto);
    }
}
