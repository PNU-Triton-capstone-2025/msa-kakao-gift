package gift.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.FakeJwtUtil;
import gift.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ControllerTestTemplate {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        JwtUtil jwtUtil() {
            return new FakeJwtUtil();
        }
    }

    protected ResultActions getWithoutToken(String url) throws Exception {
        return mockMvc.perform(get(url));
    }

    protected ResultActions getWithToken(String url, String token) throws Exception {
        return mockMvc.perform(get(url)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON));
    }

    protected ResultActions postWithToken(String url, String token, Object body) throws Exception {
        return mockMvc.perform(post(url)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body)));
    }

    protected ResultActions postWithoutToken(String url, Object body) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body)));
    }

    protected ResultActions putWithToken(String url, String token, Object body) throws Exception {
        return mockMvc.perform(put(url)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(body)));
    }

    protected ResultActions deleteWithToken(String url, String token) throws Exception {
        return mockMvc.perform(delete(url)
                .header("Authorization", "Bearer " + token));
    }


    private String toJson(Object data) throws Exception {
        return objectMapper.writeValueAsString(data);
    }
}
