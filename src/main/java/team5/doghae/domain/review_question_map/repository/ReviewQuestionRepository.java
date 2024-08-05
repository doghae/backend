package team5.doghae.domain.review_question_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.doghae.domain.review_question_map.domain.ReviewQuestionMap;

public interface ReviewQuestionRepository extends JpaRepository<ReviewQuestionMap, Long> {
}
