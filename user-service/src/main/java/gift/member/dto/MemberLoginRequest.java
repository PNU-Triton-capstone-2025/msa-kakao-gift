package gift.member.dto;

public record MemberLoginRequest(
        String email,
        String password
) {
    private static final String DEFAULT_EMAIL = "";
    private static final String DEFAULT_PASSWORD = "";

    public static MemberLoginRequest getEmpty() {
        return new MemberLoginRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }
}
