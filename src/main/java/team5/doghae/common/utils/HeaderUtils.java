package team5.doghae.common.utils;


import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import team5.doghae.common.exception.ErrorCode;
import team5.doghae.common.security.jwt.JwtProperties;
import team5.doghae.common.security.jwt.JwtType;
import team5.doghae.common.security.jwt.exception.InvalidTokenException;
import team5.doghae.common.security.jwt.exception.TokenNotFoundException;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderUtils {

    public static String getJwtToken(HttpServletRequest request, JwtType jwtType) {

        String authorization = request.getHeader(JwtProperties.JWT_HEADER);
        if (Objects.isNull(authorization)) {
            if (jwtType == JwtType.ACCESS) {
                throw new TokenNotFoundException(ErrorCode.JWT_TOKEN_NOT_FOUND);
            } else if (jwtType == JwtType.REFRESH) {
                throw new TokenNotFoundException(ErrorCode.JWT_REFRESH_TOKEN_NOT_FOUND);
            } else if (jwtType == JwtType.BOTH) {
                throw new TokenNotFoundException(ErrorCode.JWT_TOKEN_NOT_FOUND);
            }
        }

        String[] tokens = StringUtils.delimitedListToStringArray(authorization, " ");
        if (tokens.length != 2 || !(tokens[0].equals("Bearer"))) {
            throw new InvalidTokenException();
        }

        return tokens[1];
    }

    public static String getAuthCode(HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");
        if (!StringUtils.hasText(authorization)) {
            throw new TokenNotFoundException(ErrorCode.AUTH_CODE_NOT_FOUND);
        }

        String[] tokens = StringUtils.delimitedListToStringArray(authorization, " ");
        if (tokens.length != 2 || !(tokens[0].equals("Bearer"))) {
            throw new InvalidTokenException();
        }

        return tokens[1];
    }

}