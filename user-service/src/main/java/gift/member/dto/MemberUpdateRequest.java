package gift.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberUpdateRequest(
        @NotNull
        Long id,
        String password,
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 4, message = "비밀번호는 최소 4자리 이상이어야 합니다.")
        String newPassword
) {
    private static final String DEFAULT_PASSWORD = "";
    public static MemberUpdateRequest getEmpty(Long memberId) {
        return new MemberUpdateRequest(memberId, DEFAULT_PASSWORD, DEFAULT_PASSWORD);
    }
}
