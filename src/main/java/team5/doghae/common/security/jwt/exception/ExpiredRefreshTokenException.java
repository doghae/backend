package team5.doghae.common.security.jwt.exception;


import team5.doghae.common.exception.ErrorCode;

public class ExpiredRefreshTokenException extends TokenException {
    public ExpiredRefreshTokenException() {
        super(ErrorCode.EXPIRED_JWT_REFRESH_TOKEN);
    }

    public ExpiredRefreshTokenException(Throwable throwable) {
        super(ErrorCode.EXPIRED_JWT_REFRESH_TOKEN, throwable);
    }
}
