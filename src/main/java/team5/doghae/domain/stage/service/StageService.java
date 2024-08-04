package team5.doghae.domain.stage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team5.doghae.domain.question.domain.Question;
import team5.doghae.domain.question.dto.QuestionResponse;
import team5.doghae.domain.stage.domain.Stage;
import team5.doghae.domain.stage.repository.StageRepository;
import team5.doghae.domain.user.domain.User;
import team5.doghae.domain.user.repository.UserRepository;
import team5.doghae.domain.user_stage_map.repository.UserStageRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StageService {

    private final UserRepository userRepository;
    private final StageRepository stageRepository;
    private final UserStageRepository userStageRepository;


    public List<QuestionResponse.Create> getStage(Long userId, Long stageId) {

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new NoSuchElementException("존재하지 않는 회원입니다.")
                );

        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(
                        () -> new NoSuchElementException("존재하지 않는 스테이지입니다.")
                );

        List<QuestionResponse.Create> questions = stage.getQuestions()
                .stream()
                .map(QuestionResponse.Create::of)
                .toList();

        for (QuestionResponse.Create question : questions) {
            System.out.println(question.getQuestionId() + ": " + question.getProblem() + " " + question.getTag());
            for (String choice : question.getChoices()) {
                System.out.println(choice);
            }
        }


        return questions;
    }

    public void evaluateStage() {

    }
}
