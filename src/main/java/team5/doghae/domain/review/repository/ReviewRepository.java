package team5.doghae.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.doghae.domain.review.domain.Review;
import team5.doghae.domain.user.domain.User;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUser(User user);

    Optional<Review> findByUserId(Long userId);
}
