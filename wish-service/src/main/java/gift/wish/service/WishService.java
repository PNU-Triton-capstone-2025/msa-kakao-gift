package gift.wish.service;

import gift.product.dto.ProductResponseDto;
import gift.wish.domain.Wish;
import gift.wish.dto.WishInfo;
import gift.wish.dto.WishListResponse;
import gift.wish.dto.WishResponse;
import gift.wish.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final RestClient productRestClient;

    public WishService(WishRepository wishRepository, RestClient.Builder restClientBuilder) {
        this.wishRepository = wishRepository;
        this.productRestClient = restClientBuilder
                .baseUrl("http://localhost:8081")
                .build();
    }

    @Transactional(readOnly = true)
    public Wish getWish(Long memberId, Long wishId) {
        return checkValidWishAndMember(memberId, wishId);
    }

    @Transactional
    public WishResponse addWish(Long memberId, Long productId) {
        validateProductExists(productId);

        if (wishRepository.existsByMemberIdAndProductId(memberId, productId)) {
            throw new IllegalArgumentException("이미 위시 리스트에 추가된 상품입니다.");
        }

        Wish wish = wishRepository.save(new Wish(memberId, productId, 1));
        return new WishResponse(wish.getMemberId(), wish.getProductId(), wish.getQuantity());
    }

    @Transactional(readOnly = true)
    public Page<WishListResponse> getWishes(Long memberId, Pageable pageable) {
        Page<WishInfo> page = wishRepository.findWishesByMemberId(memberId, pageable);

        // (페이지 내 중복 product_id 캐시로 N+1 완화
        HashMap<Long, ProductResponseDto> cache = new HashMap<>();

        var content = page.getContent().stream()
                .map(info -> {
                    ProductResponseDto prod = cache.computeIfAbsent(info.product_id(), this::getProductById);
                    return WishListResponse.getWishListResponse(info, prod);
                })
                .toList();

        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @Transactional
    public void updateQuantity(Long memberId, Long wishId, Integer quantity){
        Wish wish = checkValidWishAndMember(memberId, wishId);

        wish.updateQuantity(quantity);
    }

    @Transactional
    public void deleteWish(Long memberId, Long wishId){
        checkValidWishAndMember(memberId, wishId);

        wishRepository.deleteById(wishId);
    }

    @Transactional
    public void deleteWishByMemberAndProductId(Long memberId, Long productId) {
        wishRepository.deleteByMemberIdAndProductId(memberId, productId);
    }

    private Wish checkValidWishAndMember(Long memberId, Long wishId){
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new NoSuchElementException("해당 위시 항목을 찾을 수 없습니다."));

        wish.validateOwner(memberId);
        return wish;
    }

    private void validateProductExists(Long productId) {
        productRestClient.get()
                .uri("/api/admin/products/{id}", productId)
                .retrieve()
                .toBodilessEntity();
    }

    private ProductResponseDto getProductById(Long productId) {
        return productRestClient.get()
                .uri("/api/admin/products/{id}", productId)
                .retrieve()
                .body(ProductResponseDto.class);
    }
}
