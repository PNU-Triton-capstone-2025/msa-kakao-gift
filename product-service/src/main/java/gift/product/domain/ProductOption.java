package gift.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected ProductOption() {

    }

    public ProductOption(String name, Integer quantity){
        this.name = name;
        this.quantity = quantity;
    }

    void assignProduct(Product product) {
        if(this.product != null) {
            this.product.removeOption(this);
        }
        this.product = product;
    }

    public void subtractQuantity(int amount){
        if(this.quantity < amount) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 남은 수량: " + this.quantity);
        }
        this.quantity -= amount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}
