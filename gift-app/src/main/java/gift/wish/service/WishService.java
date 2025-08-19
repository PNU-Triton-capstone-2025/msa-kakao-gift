package gift.wish.service;

import gift.member.domain.Member;
import gift.member.dto.MemberTokenRequest;
import gift.member.repository.MemberRepository;
import gift.product.domain.Product;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import gift.wish.domain.Wish;
import gift.wish.dto.WishListResponse;
import gift.wish.dto.WishResponse;
import gift.wish.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public Wish getWish(Long wishId) {
        return wishRepository.findById(wishId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 위시리스트 항목입니다."));
    }

    @Transactional
    public WishResponse addWish(MemberTokenRequest memberTokenRequest, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));
        Member member = memberRepository.findById(memberTokenRequest.id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (wishRepository.existsByMemberIdAndProductId(memberTokenRequest.id(), productId)) {
            throw new IllegalArgumentException("이미 위시 리스트에 추가된 상품입니다.");
        }
        Wish wish = wishRepository.save(new Wish(member, product, 1));

        return new WishResponse(wish.getMember().getId(), wish.getProduct().getId(), 1);
    }

    @Transactional(readOnly = true)
    public Page<WishListResponse> getWishes(MemberTokenRequest memberTokenRequest, Pageable pageable) {
        return wishRepository.findWishesByMemberId(memberTokenRequest.id(), pageable)
                .map(WishListResponse::getWishListResponse);
    }

    @Transactional
    public void updateQuantity(MemberTokenRequest memberTokenRequest, Long wishId, Integer quantity){
        Wish wish = checkValidWishAndMember(memberTokenRequest,wishId);

        wish.updateQuantity(quantity);
    }

    @Transactional
    public void deleteWish(MemberTokenRequest memberTokenRequest, Long wishId){
        checkValidWishAndMember(memberTokenRequest ,wishId);

        wishRepository.deleteById(wishId);
    }

    private Wish checkValidWishAndMember(MemberTokenRequest memberTokenRequest, Long wishId){
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new NoSuchElementException("해당 위시 항목을 찾을 수 없습니다."));

        wish.validateOwner(memberTokenRequest.id());
        return wish;
    }
}
