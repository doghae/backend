package team5.doghae.domain.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.doghae.domain.oauth.domain.Auth;
import team5.doghae.domain.user.domain.User;

import java.util.Optional;


public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByUser(User user);
}
