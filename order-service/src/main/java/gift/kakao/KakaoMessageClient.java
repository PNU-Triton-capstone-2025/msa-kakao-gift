package gift.kakao;

import gift.kakao.dto.KakaoMessageDto;
import gift.common.util.JsonUtil;
import gift.kakao.exception.KakaoApiFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoMessageClient {
    private final RestClient apiClient;
    private final KakaoProperties properties;
    private final JsonUtil jsonUtil;

    public KakaoMessageClient(KakaoProperties properties, JsonUtil jsonUtil, RestClient.Builder restClientBuilder) {
        this.properties = properties;
        this.jsonUtil = jsonUtil;
        this.apiClient = restClientBuilder.clone()
                .baseUrl(properties.userApiUri())
                .build();
    }

    public void sendMessageToMe(String accessToken, KakaoMessageDto messageDto) {
        String templateJson = jsonUtil.toJson(messageDto);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("template_object", templateJson);

        apiClient.post()
                .uri("/v2/api/talk/memo/default/send")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (request, response) -> {
                    String errorMsg = "카카오 메시지 API 호출 실패. 상태 코드: " + response.getStatusCode();
                    throw new KakaoApiFailedException(errorMsg, response.getStatusCode());
                })
                .toBodilessEntity();
    }
}
