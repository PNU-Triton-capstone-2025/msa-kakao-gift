package gift.auth.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.oauth.dto.KakaoTokenResponseDto;
import gift.auth.oauth.dto.KakaoUserInfoResponseDto;
import gift.common.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@EnableConfigurationProperties(KakaoProperties.class)
@RestClientTest(KakaoApiClient.class)
class KakaoLoginApiClientTest {

    @TestConfiguration
    static class TestConfig{
        @Bean
        public JsonUtil jsonUtil(ObjectMapper objectMapper) {
            return new JsonUtil(objectMapper);
        }
    }

    @Autowired
    private KakaoApiClient kakaoLoginApiClient;
    @Autowired
    private MockRestServiceServer mockRestServiceServer;
    @Autowired
    private JsonUtil jsonUtil;

    @Test
    void fetchAccessTokenTest() throws Exception {
        String expectedToken = jsonUtil.toJson(new KakaoTokenResponseDto("test_token", "test_re"));
        mockRestServiceServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andRespond(withSuccess(expectedToken, MediaType.APPLICATION_JSON));

        KakaoTokenResponseDto token = kakaoLoginApiClient.fetchAccessToken("auth_code");
        assertThat(token.accessToken()).isEqualTo("test_token");

        mockRestServiceServer.verify();
    }

    @Test
    void fetchUserInfoTest() throws Exception {
        KakaoUserInfoResponseDto userInfo = new KakaoUserInfoResponseDto(
                12345L,
                new KakaoUserInfoResponseDto.KakaoAccount(
                        new KakaoUserInfoResponseDto.Profile("test_user"),
                        "test@example.com"
                )
        );

        String expectedUser = jsonUtil.toJson(userInfo);

        mockRestServiceServer.expect(requestTo("https://kapi.kakao.com/v2/user/me"))
                .andExpect(header("Authorization", "Bearer test_token"))
                .andRespond(withSuccess(expectedUser, MediaType.APPLICATION_JSON));

        KakaoUserInfoResponseDto response = kakaoLoginApiClient.fetchUserInfo("test_token");
        assertThat(response.id()).isEqualTo(12345L);
        assertThat(response.kakaoAccount().email()).isEqualTo("test@example.com");

        mockRestServiceServer.verify();
    }

}