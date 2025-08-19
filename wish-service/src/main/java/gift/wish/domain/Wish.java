package gift.wish.domain;

import gift.wish.exception.WishOwnerException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "member_id")
    private Long memberId;

    @Column(nullable = false, name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    protected Wish() {}

    public Wish(Long memberId, Long productId, Integer quantity) {
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void validateOwner(Long memberId) {
        if (!this.memberId.equals(memberId)) {
            throw new WishOwnerException("해당 위시 항목을 삭제할 권한이 없습니다.");
        }
    }

    public void updateQuantity(Integer quantity){
        this.quantity = quantity;
    }
}
