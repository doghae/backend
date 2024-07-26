package team5.doghae.common.security.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final String credentials;

    public JwtAuthenticationToken(Object principal, String credentials) {
        super(null);
        super.setAuthenticated(false);
        this.principal = principal;
        this.credentials = credentials;
    }


    public JwtAuthenticationToken(Object principal, String credentials,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }


    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) throw new IllegalArgumentException();
        super.setAuthenticated(false);
    }
}
