package gift.member.controller;

import gift.common.ControllerTestTemplate;
import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberRegisterRequest;
import gift.member.dto.MemberTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
public class MemberApiControllerTest extends ControllerTestTemplate {
    private String testEmail;
    private String testPassword;

    @BeforeEach
    void setUp() {
        testEmail = "test" + System.currentTimeMillis() + "@test.com";
        testPassword = "12345678";
    }

    @Test
    @DisplayName("회원가입 후 로그인 - 200")
    void registerAndLogin() throws Exception{

        postWithoutToken("/api/members/register", getRegisterRequest())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isString());

        postWithoutToken("/api/members/login", getRegisterRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString());
    }

//    @Test
//    @DisplayName("로그인 후 product list 조회 - 200")
//    void loginAndAccessResource() throws Exception {
//
//        postWithoutToken("/api/members/register/admin", getRegisterRequest())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.token").isString());
//
//        MvcResult loginResult = postWithoutToken("/api/members/login", getRegisterRequest())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String jsonResponse = loginResult.getResponse().getContentAsString();
//        String token = objectMapper.readValue(jsonResponse, MemberTokenResponse.class).token();
//
//        getWithToken("/api/admin/products", token)
//                .andExpect(status().isOk());
//    }

    @Test
    @DisplayName("토큰 없이 product list 조회 - 401")
    void accessResourceWithoutToken() throws Exception{
        getWithoutToken("/api/products")
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 product list 조회 - 401")
    void accessResourceWithNotValidToken() throws Exception{
        getWithToken("/api/products", "testToken")
                .andExpect(status().isUnauthorized());
    }

    MemberRegisterRequest getRegisterRequest(){
        return new MemberRegisterRequest(testEmail, testPassword);
    }

    MemberLoginRequest getLoginRequest(){
        return new MemberLoginRequest(testEmail, testPassword);
    }
}
