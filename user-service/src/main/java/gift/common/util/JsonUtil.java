package gift.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {
    private final ObjectMapper objectMapper;

    public JsonUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String toJson(Object object) {
        try{
            return objectMapper.writeValueAsString(object);
        }
        catch (JsonProcessingException ex) {
            throw new RuntimeException("Json Serialization 에러: " +  ex.getMessage());
        }
    }
}
