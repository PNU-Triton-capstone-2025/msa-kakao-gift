package gift.wish.repository;

import gift.wish.domain.Wish;
import gift.wish.dto.WishInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    @Query(value = "SELECT new gift.wish.dto.WishInfo(w.id, w.productId, w.quantity) " +
            "FROM Wish w WHERE w.memberId = :memberId")
    Page<WishInfo> findWishesByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    boolean existsByMemberIdAndProductId(Long memberId, Long productId);

    @Modifying
    @Query("DELETE FROM Wish w WHERE w.memberId = :memberId AND w.productId = :productId")
    void deleteByMemberIdAndProductId(@Param("memberId") Long memberId, @Param("productId") Long productId);
}
