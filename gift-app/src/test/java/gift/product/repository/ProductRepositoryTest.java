package gift.product.repository;

import gift.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productJpaRepository;


    @Test
    @DisplayName("상품 저장 후 조회")
    void saveAndFindById() {
        System.out.println(productJpaRepository.getClass());

        Product savedProduct = productJpaRepository.save(new Product(null, "테스트", 0, "test.jpg"));
        Product foundProduct = productJpaRepository.findById(savedProduct.getId()).orElse(null);

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo("테스트");
    }
}
