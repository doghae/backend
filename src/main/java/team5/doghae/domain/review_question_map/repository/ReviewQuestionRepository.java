package team5.doghae.domain.review_question_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.doghae.domain.question.domain.Question;
import team5.doghae.domain.review.domain.Review;
import team5.doghae.domain.review_question_map.domain.ReviewQuestionMap;

import java.util.List;

public interface ReviewQuestionRepository extends JpaRepository<ReviewQuestionMap, Long> {

    List<ReviewQuestionMap> findAllByReview(Review review);

    List<ReviewQuestionMap> findAllByReviewAndQuestion(Review review, Question question);
}
