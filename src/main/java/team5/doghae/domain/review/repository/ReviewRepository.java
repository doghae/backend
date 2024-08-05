package team5.doghae.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.doghae.domain.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
