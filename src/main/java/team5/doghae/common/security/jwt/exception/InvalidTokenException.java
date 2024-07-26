package team5.doghae.common.security.jwt.exception;


import team5.doghae.common.exception.ErrorCode;

public class InvalidTokenException extends TokenException {

    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }

    public InvalidTokenException(Throwable throwable) {
        super(ErrorCode.INVALID_TOKEN, throwable);
    }
}
