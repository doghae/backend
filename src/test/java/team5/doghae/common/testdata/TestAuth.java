package team5.doghae.common.testdata;

import team5.doghae.domain.oauth.domain.Auth;
import team5.doghae.domain.user.domain.User;

public class TestAuth {
    public static Auth createAuth(User user) {
        return Auth.create(user, "refreshToken");
    }
}
