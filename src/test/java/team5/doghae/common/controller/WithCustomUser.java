package team5.doghae.common.controller;

import org.springframework.security.test.context.support.WithSecurityContext;
import team5.doghae.domain.user.domain.UserRole;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomUserSecurityContextFactory.class)
public @interface WithCustomUser {
    long userId() default 1L;

    UserRole userRole() default UserRole.USER;

}
