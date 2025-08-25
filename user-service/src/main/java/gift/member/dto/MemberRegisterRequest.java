package gift.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberRegisterRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "유효한 이메일 형식을 입력해 주세요.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 4, message = "비밀번호는 최소 4자리 이상이어야 합니다.")
        String password
) {
    private static final String DEFAULT_EMAIL = "";
    private static final String DEFAULT_PASSWORD = "";
    public static MemberRegisterRequest getEmpty() {
            return new MemberRegisterRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }
}
