package team5.doghae.common.security.jwt.exception;


import team5.doghae.common.exception.ErrorCode;

public class TokenNotFoundException extends TokenException {

    public TokenNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenNotFoundException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }
}
