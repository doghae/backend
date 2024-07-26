package team5.doghae.common.controller;

import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import team5.doghae.common.security.jwt.JwtAuthenticationToken;
import team5.doghae.common.security.jwt.JwtProperties;

public class WithCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomUser customUser) {

        DefaultClaims claims = new DefaultClaims();
        claims.put(JwtProperties.USER_ID, customUser.userId());
        claims.put(JwtProperties.USER_ROLE, customUser.userRole().getKey());

        JwtAuthenticationToken authentication = new JwtAuthenticationToken(claims, "", null);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}