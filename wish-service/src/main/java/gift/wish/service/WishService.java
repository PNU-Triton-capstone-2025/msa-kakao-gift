package gift.wish.service;

import gift.wish.domain.Wish;
import gift.wish.dto.WishListResponse;
import gift.wish.dto.WishResponse;
import gift.wish.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Transactional(readOnly = true)
    public Wish getWish(Long memberId, Long wishId) {
        return checkValidWishAndMember(memberId, wishId);
    }

    @Transactional
    public WishResponse addWish(Long memberId, Long productId) {
        // TODO: product-service에 productId 유효성 검증 API 호출 필요
        // 지금은 항상 유효하다고 가정

        if (wishRepository.existsByMemberIdAndProductId(memberId, productId)) {
            throw new IllegalArgumentException("이미 위시 리스트에 추가된 상품입니다.");
        }
        Wish wish = wishRepository.save(new Wish(memberId, productId, 1));

        return new WishResponse(wish.getMemberId(), wish.getProductId(), 1);
    }

    public List<WishListResponse> getWishes(Long memberId, Pageable pageable) {
        return wishRepository.findWishesByMemberId(memberId, pageable)
                .map(WishListResponse::getWishListResponse)
                .getContent(); // .getContent()를 추가하여 리스트만 추출합니다.
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

    private Wish checkValidWishAndMember(Long memberId, Long wishId){
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new NoSuchElementException("해당 위시 항목을 찾을 수 없습니다."));

        wish.validateOwner(memberId);
        return wish;
    }
}
