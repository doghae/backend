package team5.doghae.domain.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team5.doghae.domain.question.domain.Question;
import team5.doghae.domain.question.dto.QuestionRequest;
import team5.doghae.domain.question.dto.QuestionResponse;
import team5.doghae.domain.question.repository.QuestionRepository;
import team5.doghae.domain.review.domain.Review;
import team5.doghae.domain.review.repository.ReviewRepository;
import team5.doghae.domain.review_question_map.domain.ReviewQuestionMap;
import team5.doghae.domain.review_question_map.repository.ReviewQuestionRepository;
import team5.doghae.domain.user.repository.UserRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewQuestionRepository reviewQuestionRepository;


    public QuestionResponse.Create getSingleWrongedQuestion(Long userId, Long questionId) {

        Review review = reviewRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("틀린 문제가 없습니다"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("해당 문제가 존재하지 않습니다"));

        ReviewQuestionMap reviewQuestionMap = reviewQuestionRepository.findByReviewAndQuestion(review, question)
                .orElseThrow(() -> new NoSuchElementException("틀리지 않은 문제입니다."));

        return QuestionResponse.Create.of(reviewQuestionMap.getQuestion());
    }

    @Transactional
    public QuestionResponse.Evaluate retryWrongedQuestion(Long userId, Long questionId, QuestionRequest.Evaluate retryRequest) {

        Review review = reviewRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("틀린 문제가 없습니다"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("해당 문제가 존재하지 않습니다"));

        ReviewQuestionMap reviewQuestionMap = reviewQuestionRepository.findByReviewAndQuestion(review, question)
                .orElseThrow(() -> new NoSuchElementException("잘못된 요청입니다."));

        boolean isAnswer = question.getAnswer().equals(retryRequest.getAnswer());

        if (isAnswer) {
            reviewQuestionRepository.delete(reviewQuestionMap);
        }

        return QuestionResponse.Evaluate.of(question, isAnswer);
    }
}
