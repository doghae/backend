package team5.doghae.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team5.doghae.domain.question.repository.QuestionRepository;
import team5.doghae.domain.review.domain.Review;
import team5.doghae.domain.review.dto.ReviewResponse;
import team5.doghae.domain.review.repository.ReviewRepository;
import team5.doghae.domain.review_question_map.repository.ReviewQuestionRepository;
import team5.doghae.domain.user.domain.User;
import team5.doghae.domain.user.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewQuestionRepository reviewQuestionRepository;
    private final QuestionRepository questionRepository;

    public List<ReviewResponse.WrongAnswer> getWrongQuestions(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

        Review review = reviewRepository.findByUser(user)
                .orElseThrow(() -> new NoSuchElementException("틀린 문제가 없습니다."));

        return reviewQuestionRepository.findAllByReview(review)
                .stream()
                .map(ReviewResponse.WrongAnswer::new)
                .sorted(Comparator.comparing(ReviewResponse.WrongAnswer::getKeyword))
                .toList();
    }
}
