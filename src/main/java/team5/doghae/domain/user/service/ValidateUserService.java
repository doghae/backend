package team5.doghae.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team5.doghae.domain.user.domain.SocialCode;
import team5.doghae.domain.user.domain.User;
import team5.doghae.domain.user.domain.UserRole;
import team5.doghae.domain.user.exception.UserAccessDeniedException;
import team5.doghae.domain.user.exception.UserNotFoundException;
import team5.doghae.domain.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ValidateUserService {

    private final UserRepository userRepository;

    public User validateUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public User validateRegisteredUserByEmail(String email, SocialCode socialCode) {

        return userRepository.findAllByEmail(email).stream()
                .filter(u -> u.getSocialCode() == socialCode)
                .findFirst()
                .orElse(null);
    }

    public User validateAdminUserById(Long userId) {

        User user = validateUserById(userId);
        if (user.getUserRole() == UserRole.ADMIN) {
            return user;
        } else {
            throw new UserAccessDeniedException();
        }
    }

}
