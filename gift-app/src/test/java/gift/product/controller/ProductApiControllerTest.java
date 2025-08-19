//package gift.product.controller;
//
//import gift.common.ControllerTestTemplate;
//import gift.member.domain.Member;
//import gift.member.dto.MemberRegisterRequest;
//import gift.member.dto.MemberTokenRequest;
//import gift.member.repository.MemberRepository;
//import gift.member.service.MemberService;
//import gift.product.domain.Product;
//import gift.product.dto.ProductEditRequestDto;
//import gift.product.dto.ProductOptionRequestDto;
//import gift.product.dto.ProductRequestDto;
//import gift.product.service.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.is;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@Transactional
//@AutoConfigureMockMvc
//@DisplayName("ProductApiController 테스트")
//public class ProductApiControllerTest extends ControllerTestTemplate {
//
//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private MemberService memberService;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    private Product product1;
//    private Product product2;
//    private String adminToken;
//    private String mdToken;
//
//    @BeforeEach
//    void setUp() {
//
//        MemberRegisterRequest adminRequest = new MemberRegisterRequest("test@gmail.com", "12345678");
//        MemberRegisterRequest mdRequest = new MemberRegisterRequest("md@gmail.com", "12345678");
//
//        adminToken = memberService.registerAdmin(adminRequest).token();
//        mdToken = memberService.registerMd(mdRequest).token();
//
//        Member mdMember = memberRepository.findByEmail("md@gmail.com").get();
//        MemberTokenRequest mdTokenRequest = new MemberTokenRequest(mdMember.getId(), mdMember.getEmail(), mdMember.getPassword(), mdMember.getRole());
//
//
//        product1 = productService.saveProduct(getProductRequest("Test1", 1000, "Test1.jpg"), mdTokenRequest);
//        product2 = productService.saveProduct(getProductRequest("Test2", 1200, "Test2.jpg"), mdTokenRequest);
//    }
//
//    @Test
//    @DisplayName("상품 등록 테스트 - 201")
//    void addProduct() throws Exception {
//        postWithToken("/api/admin/products", adminToken, getProductRequest("new", 1500, "new.png"))
//                .andExpect(status().isCreated())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("전체 상품 조회 테스트 - 200")
//    void getProducts() throws Exception {
//        getWithToken("/api/admin/products", adminToken)
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.content[0].name", is("Test2")))
//                .andExpect(jsonPath("$.content[1].name", is("Test1")))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("단일 상품 조회 테스트 - 200")
//    void getProductById() throws Exception{
//        Long productId = product1.getId();
//
//        getWithToken("/api/admin/products/" + productId, adminToken)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(productId.intValue())))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 상품 조회 테스트 - 404")
//    void getProductNotFound() throws Exception{
//        Long notFoundId = 999999L;
//
//        getWithToken("/api/admin/products/" + notFoundId, adminToken)
//                .andExpect(status().isNotFound())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("상품 수정 테스트 - 204")
//    void updateProduct() throws Exception{
//        Long productId = product1.getId();
//
//        putWithToken("/api/admin/products/" + productId, adminToken, getProductEditRequest("수정된 이름", 12000, "updated"))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//
//        getWithToken("/api/admin/products/" + productId, adminToken)
//                .andExpect(jsonPath("$.name", is("수정된 이름")))
//                .andExpect(jsonPath("$.price", is(12000)));
//    }
//
//    @Test
//    @DisplayName("상품 삭제 테스트 - 204")
//    void deleteProduct() throws Exception {
//        Long productId = product1.getId();
//
//        deleteWithToken("/api/admin/products/" + productId, adminToken)
//                .andExpect(status().isNoContent())
//                .andDo(print());
//
//        getWithToken("/api/admin/products/" + productId, adminToken)
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @DisplayName("ADMIN 카카오 상품 등록 실패 - 유효하지 않은 상품명(카카오) - 400")
//    void addKakaoProduct_fail() throws Exception {
//
//        postWithToken("/api/admin/products", adminToken, getProductRequest("카카오 인형", 15000, "a.jpg"))
//                .andExpect(status().isBadRequest())
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("MD 카카오 상품 등록 성공 - 201")
//    void addKakaoProduct() throws Exception {
//
//        postWithToken("/api/admin/products", mdToken, getProductRequest("카카오 인형", 15000, "a.jpg"))
//                .andExpect(status().isCreated())
//                .andDo(print());
//    }
//
//    ProductRequestDto getProductRequest(String name, Integer price, String imageUrl){
//        ProductOptionRequestDto defaultOption = new ProductOptionRequestDto("기본 옵션", 100);
//        return new ProductRequestDto(name, price, imageUrl, List.of(defaultOption));
//    }
//
//    ProductEditRequestDto getProductEditRequest(String name, Integer price, String imageUrl){
//        return new ProductEditRequestDto(name, price, imageUrl);
//    }
//}
