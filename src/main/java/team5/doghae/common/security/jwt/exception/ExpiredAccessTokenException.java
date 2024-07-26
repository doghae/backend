package team5.doghae.common.security.jwt.exception;


import team5.doghae.common.exception.ErrorCode;

public class ExpiredAccessTokenException extends TokenException {

    public ExpiredAccessTokenException() {
        super(ErrorCode.EXPIRED_JWT_ACCESS_TOKEN);
    }
}
