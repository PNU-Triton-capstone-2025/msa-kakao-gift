package gift.member.dto;

import gift.member.domain.RoleType;

public record MemberTokenRequest (
        Long id,
        String email,
        String password,
        RoleType role
) {}
