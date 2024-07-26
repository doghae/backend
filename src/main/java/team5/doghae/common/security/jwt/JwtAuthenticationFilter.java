package team5.doghae.common.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import team5.doghae.common.utils.HeaderUtils;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final String[] permitAlls;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = HeaderUtils.getJwtToken(request, JwtType.ACCESS);
        jwtProvider.validAccessToken(accessToken);

        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        for (String permitAllEndpoint : permitAlls) {
            if (pathMatcher.match(permitAllEndpoint, requestURI)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @throws TokenNotFoundException      - 헤더에 토큰이 없는 경우
     * @throws InvalidTokenException       - 헤더에 토큰이 있지만 유효하지 않은 경우
     * @throws ExpiredAccessTokenException - 헤더에 토큰이 있지만 만료된 경우
     */
//    private void authentication() {
//        String accessToken = HeaderUtils.getJwtToken(getRequest(), JwtType.ACCESS);
//
//        jwtProvider.validAccessToken(accessToken);
//
//        Authentication authentication = jwtProvider.getAuthentication(accessToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//    private HttpServletRequest getRequest() {
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        return servletRequestAttributes.getRequest();
//    }
}
