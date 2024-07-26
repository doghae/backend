package team5.doghae.common.security.jwt;

import lombok.Builder;
import lombok.Getter;
import team5.doghae.domain.user.domain.UserRole;

@Getter
public class JwtTokenInfo {

    private final Long userId;
    private final UserRole userRole;

    @Builder
    public JwtTokenInfo(Long userId, UserRole userRole) {
        this.userId = userId;
        this.userRole = userRole;
    }
}
