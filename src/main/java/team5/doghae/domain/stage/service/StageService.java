package team5.doghae.domain.stage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team5.doghae.domain.question.Tag;
import team5.doghae.domain.question.domain.Question;
import team5.doghae.domain.question.dto.QuestionRequest;
import team5.doghae.domain.question.dto.QuestionResponse;
import team5.doghae.domain.review.domain.Review;
import team5.doghae.domain.review.repository.ReviewRepository;
import team5.doghae.domain.review_question_map.domain.ReviewQuestionMap;
import team5.doghae.domain.review_question_map.repository.ReviewQuestionRepository;
import team5.doghae.domain.stage.domain.Stage;
import team5.doghae.domain.stage.domain.StageResultInfo;
import team5.doghae.domain.stage.dto.StageRequest;
import team5.doghae.domain.stage.repository.StageRepository;
import team5.doghae.domain.user.domain.User;
import team5.doghae.domain.user.repository.UserRepository;
import team5.doghae.domain.user_stage_map.domain.UserStage;
import team5.doghae.domain.user_stage_map.repository.UserStageRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StageService {

    private final UserRepository userRepository;
    private final StageRepository stageRepository;
    private final UserStageRepository userStageRepository;
    private final ReviewQuestionRepository reviewQuestionRepository;
    private final ReviewRepository reviewRepository;

    public List<QuestionResponse.Create> getStage(Long userId, Long stageId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 스테이지입니다."));

        return stage.getQuestions()
                .stream()
                .map(QuestionResponse.Create::of)
                .toList();
    }

    @Transactional
    public List<QuestionResponse.Evaluate> evaluateStage(Long userId, Long stageId, StageRequest.Evaluate evaluateRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 스테이지입니다."));

        List<Question> questions = stage.getQuestions()
                .stream()
                .sorted(Comparator.comparing(Question::getId))
                .toList();

        List<QuestionRequest.Evaluate> answers = evaluateRequest.getAnswers()
                .stream()
                .sorted(Comparator.comparing(QuestionRequest.Evaluate::getQuestionId))
                .toList();

        if (answers.size() != 5 && (questions.size() != answers.size())) {
            throw new IllegalArgumentException("요청한 문제 정보가 올바르지 않습니다.");
        }

        Map<Tag, Integer> map = new HashMap<>();

        List<QuestionResponse.Evaluate> result = new ArrayList<>();

        List<Question> wrongQuestionList = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {

            Question question = questions.get(i);
            QuestionRequest.Evaluate answer = answers.get(i);

            if (!question.getId().equals(answer.getQuestionId())) {
                throw new IllegalArgumentException("요청한 문제 정보가 올바르지 않습니다.");
            }

            boolean isAnswer = question.getAnswer().equals(answer.getAnswer());

            Tag tag = question.getTag();
            if (!isAnswer) {
                wrongQuestionList.add(question);
                map.put(tag, map.getOrDefault(tag, 0) + 1);
            }

            result.add(
                    QuestionResponse.Evaluate.of(question, isAnswer)
            );

        }

        Optional<UserStage> findUserStage = userStageRepository.findByUserAndStage(user, stage);
        StageResultInfo resultInfo = StageResultInfo.of(map);

        if (findUserStage.isEmpty()) {
            userStageRepository.save(UserStage.of(user, stage, resultInfo));

            Review review = null;
            Optional<Review> findReview = reviewRepository.findByUser(user);

            if (findReview.isEmpty()) {
                Review newReview = Review.of(user);
                reviewRepository.save(newReview);
            } else {
                review = findReview.get();
            }

            for (Question question : wrongQuestionList) {
                reviewQuestionRepository.save(ReviewQuestionMap.of(review, question));
            }
        } else {
            UserStage userStage = findUserStage.get();
            userStage.changeResultInfo(resultInfo);
        }

        return result;
    }
}
