package team5.doghae.common.security.jwt.exception;

import team5.doghae.common.exception.BusinessException;
import team5.doghae.common.exception.ErrorCode;


public class TokenException extends BusinessException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }
}
