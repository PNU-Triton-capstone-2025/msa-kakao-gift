//package gift.product.controller;
//
//import gift.common.ControllerTestTemplate;
//import gift.member.domain.Member;
//import gift.member.dto.MemberRegisterRequest;
//import gift.member.dto.MemberTokenRequest;
//import gift.member.repository.MemberRepository;
//import gift.member.service.MemberService;
//import gift.product.domain.Product;
//import gift.product.dto.ProductOptionRequestDto;
//import gift.product.dto.ProductRequestDto;
//import gift.product.service.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Transactional
//@AutoConfigureMockMvc
//class ProductOptionApiControllerTest extends ControllerTestTemplate {
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
//    private Product testProduct;
//    private String userToken;
//
//    @BeforeEach
//    void setUp() {
//        userToken = memberService.register(new MemberRegisterRequest("user@test.com", "12345678")).token();
//
//        ProductOptionRequestDto option1 = new ProductOptionRequestDto("기본", 100);
//        ProductOptionRequestDto option2 = new ProductOptionRequestDto("색상-레드", 50);
//        ProductOptionRequestDto option3 = new ProductOptionRequestDto("사이즈-XL", 30);
//
//        Member member = memberRepository.findByEmail("user@test.com").get();
//        MemberTokenRequest tokenRequest = new MemberTokenRequest(member.getId(), member.getEmail(), member.getPassword(), member.getRole());
//
//
//        ProductRequestDto productRequest = new ProductRequestDto("테스트 상품", 15000, "test.jpg", List.of(option1, option2, option3));
//        testProduct = productService.saveProduct(productRequest, tokenRequest);
//    }
//
//    @Test
//    @DisplayName("특정 상품 옵션 목록 조회 - 200")
//    void getOptionsForProduct() throws Exception {
//        ResultActions result = getWithToken("/api/products/" + testProduct.getId() + "/options", userToken);
//
//        result.andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].name", is("기본")))
//                .andExpect(jsonPath("$[2].quantity", is(30)))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 상품의 옵션 목록 조회 시 빈 배열 반환- 200")
//    void getOptionsForNonExistProduct() throws Exception {
//        Long nonExistProductId = 999999L;
//
//        ResultActions result = getWithToken("/api/products/" + nonExistProductId + "/options", userToken);
//
//        result.andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(0)))
//                .andDo(print());
//    }
//}