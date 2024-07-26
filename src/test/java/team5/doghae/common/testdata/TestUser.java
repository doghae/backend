package team5.doghae.common.testdata;

import org.springframework.test.util.ReflectionTestUtils;
import team5.doghae.domain.user.domain.SocialCode;
import team5.doghae.domain.user.domain.User;
import team5.doghae.domain.user.domain.UserRole;

public class TestUser {
    public static User createUser() {
        return User.create("email@gmail.com", SocialCode.KAKAO);
    }

    public static User createUserWithId(Long userId) {
        User user = createUser();
        ReflectionTestUtils.setField(user, "id", userId);
        return user;
    }

    public static User createAdminUserWithId(Long userId) {
        User user = createUserWithId(userId);
        ReflectionTestUtils.setField(user, "userRole", UserRole.ADMIN);
        return user;
    }

    public static User createUserWithSocialCode(SocialCode socialCode) {
        return User.create("email@gmail.com", socialCode);
    }
}
