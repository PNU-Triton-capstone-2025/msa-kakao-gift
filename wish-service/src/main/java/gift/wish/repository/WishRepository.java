package gift.wish.repository;

import gift.member.domain.Member;
import gift.product.domain.Product;
import gift.wish.domain.Wish;
import gift.wish.dto.WishInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    @Query(value = "SELECT new gift.wish.dto.WishInfo(w.id, p.id, p.name, p.price, p.imageUrl, w.quantity) " +
            "FROM Wish w JOIN w.product p WHERE w.member.id = :memberId",
            countQuery = "SELECT count(w) FROM Wish w where w.member.id = :memberId")
    Page<WishInfo> findWishesByMemberId(@Param("memberId") Long memberId, Pageable pageable);
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
    Optional<Wish> findByMemberAndProduct(Member member, Product product);
}
