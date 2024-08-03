package team5.doghae.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team5.doghae.domain.auth.domain.Auth;
import team5.doghae.domain.auth.repository.AuthRepository;
import team5.doghae.domain.user.domain.SocialCode;
import team5.doghae.domain.user.domain.User;
import team5.doghae.domain.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    @Transactional
    public User registerWithOAuth(String email, SocialCode socialCode, String refreshToken) {

        User user = userRepository.save(User.create(email, socialCode));
        Auth auth = Auth.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();

        authRepository.save(auth);
        return user;
    }

}
