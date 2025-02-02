package team5.doghae.common.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import team5.doghae.common.exception.BusinessException;
import team5.doghae.common.exception.ErrorCode;
import team5.doghae.common.security.jwt.exception.ExpiredAccessTokenException;
import team5.doghae.common.security.jwt.exception.ExpiredRefreshTokenException;
import team5.doghae.common.security.jwt.exception.InvalidTokenException;
import team5.doghae.domain.user.domain.UserRole;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Profile({"local","prod"})
public class JwtProvider {

    private final String CLAIM_USER_ID = JwtProperties.USER_ID;
    private final String CLAIM_USER_ROLE = JwtProperties.USER_ROLE;

    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;

    private final Key key;
    private final ObjectMapper objectMapper;

    public JwtProvider(@Value("${jwt.access-token-expire-time}") long accessTime,
                       @Value("${jwt.refresh-token-expire-time}") long refreshTime,
                       @Value("${jwt.secret}") String secretKey) {
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTime;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.objectMapper = new ObjectMapper();
    }

    protected String createToken(Long userId, UserRole userRole, long tokenValid) {

        Claims claims = Jwts.claims();
        claims.put(CLAIM_USER_ID, userId);
        claims.put(CLAIM_USER_ROLE, userRole.toString());

        Date date = new Date();

        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenValid))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createAccessToken(Long userId, UserRole userRole) {
        return createToken(userId, userRole, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String createRefreshToken(Long userId, UserRole userRole) {
        return createToken(userId, userRole, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String expireToken(String jwtToken) {
        Claims claims = parseClaims(jwtToken);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createNewAccessTokenFromRefreshToken(String refreshToken) {

        String payload = new String(Base64.getUrlDecoder().decode(refreshToken.split("\\.")[1]));
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            Long userId = jsonNode.get(CLAIM_USER_ID).asLong();
            UserRole role = UserRole.valueOf(jsonNode.get(CLAIM_USER_ROLE).asText());
            return createAccessToken(userId, role);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 쿠키 maxAge는 초단위 설정이라 1000으로 나눈값으로 설정
     */
    public long getExpireTime() {
        return REFRESH_TOKEN_EXPIRE_TIME / 1000;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        String userRole = claims.get("userRole", String.class);
        if (!StringUtils.hasText(userRole)) {
            throw new BusinessException(ErrorCode.AUTHORITY_NOT_FOUND);
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(userRole.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new JwtAuthenticationToken(claims, "", authorities);
    }


    public void validAccessToken(String token) {
        try {
            parseClaims(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredAccessTokenException();
        } catch (Exception e) {
            throw new InvalidTokenException(e);
        }
    }

    public void validRefreshToken(String token) {
        try {
            parseClaims(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredRefreshTokenException();
        } catch (Exception e) {
            throw new InvalidTokenException(e);
        }
    }

    public Claims parseClaims(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }


}