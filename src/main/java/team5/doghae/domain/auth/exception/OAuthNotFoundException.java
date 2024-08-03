package team5.doghae.domain.auth.exception;

import team5.doghae.common.exception.BusinessException;
import team5.doghae.common.exception.ErrorCode;

public class OAuthNotFoundException extends BusinessException {

    public OAuthNotFoundException() {
        super(ErrorCode.OAUTH_NOT_FOUND);
    }

}
