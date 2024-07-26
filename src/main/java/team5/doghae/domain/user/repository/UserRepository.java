package team5.doghae.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.doghae.domain.user.domain.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByEmail(String email);

}
