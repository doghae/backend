package team5.doghae.domain.user.exception;

import team5.doghae.common.exception.BusinessException;
import team5.doghae.common.exception.ErrorCode;

public class UserAccessDeniedException extends BusinessException {

    public UserAccessDeniedException() {
        super(ErrorCode.USER_ACCESS_DENIED);
    }

}
