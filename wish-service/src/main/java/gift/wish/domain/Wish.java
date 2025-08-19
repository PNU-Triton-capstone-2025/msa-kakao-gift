package gift.wish.domain;

import gift.member.domain.Member;
import gift.product.domain.Product;
import gift.wish.exception.WishOwnerException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_wish_member_id_ref_member_id"))
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_wish_product_id_ref_product_id"))
    private Product product;
    @Column(nullable = false)
    private Integer quantity;

    protected Wish() {}

    public Wish(Member member, Product product, Integer quantity) {
        this.member = member;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void validateOwner(Long memberId) {
        boolean isValid = this.member.validateMemberId(memberId);
        if (!isValid) {
            throw new WishOwnerException("해당 위시 항목을 삭제할 권한이 없습니다.");
        }
    }

    public void updateQuantity(Integer quantity){
        this.quantity = quantity;
    }
}
