package team5.doghae.domain.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team5.doghae.domain.user.domain.User;
import team5.doghae.domain.user.service.ValidateUserService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuthService {

    private final KakaoOAuthService kakaoOAuthService;
    private final ValidateUserService validateUserService;


    @Transactional
    public void deleteAccount(Long userId) {
        User user = validateUserService.validateUserById(userId);
        kakaoOAuthService.deleteAccount(user);

    }
}
