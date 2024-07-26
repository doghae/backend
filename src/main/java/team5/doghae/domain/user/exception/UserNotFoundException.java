package team5.doghae.domain.user.exception;

import team5.doghae.common.exception.BusinessException;
import team5.doghae.common.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
