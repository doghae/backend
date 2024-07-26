package team5.doghae.domain.user.domain;

import lombok.Getter;
import team5.doghae.common.exception.BusinessException;
import team5.doghae.common.exception.ErrorCode;

@Getter
public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    private final String key;

    UserRole(String key) {
        this.key = key;
    }

    public static UserRole fromKey(String key) {
        for (UserRole role : UserRole.values()) {
            if (role.getKey().equals(key)) {
                return role;
            }
        }
        throw new BusinessException(ErrorCode.USER_ROLE_ERROR);
    }
}
