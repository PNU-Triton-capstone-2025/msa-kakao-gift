package gift.auth.oauth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class KakaoApiFailedException extends RuntimeException {
    private final HttpStatus status;

    public KakaoApiFailedException(String message, HttpStatusCode statusCode) {
        super(message);
        this.status = HttpStatus.resolve(statusCode.value());
    }

    public HttpStatus getStatus() {
        return status;
    }
}
