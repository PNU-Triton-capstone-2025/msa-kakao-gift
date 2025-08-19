package gift.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @Column(unique = true)
    private Long kakaoId;
    @Column(length = 1000)
    private String kakaoAccessToken;
    @Column(length = 1000)
    private String kakaoRefreshToken;

    protected Member() {}

    public Member(Long id, String email, String password, RoleType role, Long kakaoId, String kakaoAccessToken, String kakaoRefreshToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.kakaoId = kakaoId;
        this.kakaoAccessToken = kakaoAccessToken;
        this.kakaoRefreshToken = kakaoRefreshToken;
    }

    public Member(Long id, String email, String password, RoleType role){
        this(id, email, password, role, null, null, null);
    }

    public Member(String email, String password, RoleType role, Long kakaoId, String kakaoAccessToken, String kakaoRefreshToken){
        this(null, email, password, role, kakaoId, kakaoAccessToken, kakaoRefreshToken);
    }

    public Member(String email, String password, RoleType role) {
        this(null, email, password, role, null, null, null);
    }

    public Member(Long id){
        this(id, null, null, null, null, null, null);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public RoleType getRole() {
        return role;
    }

    public String getKakaoAccessToken() {
        return this.kakaoAccessToken;
    }

    public String getKakaoRefreshToken() {
        return this.kakaoRefreshToken;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public boolean validateMemberId(Long memberId) {
        if (id == null) {
            return false;
        }
        return id.equals(memberId);
    }

    public boolean isKakaoUser() {
        return this.kakaoId != null;
    }
}