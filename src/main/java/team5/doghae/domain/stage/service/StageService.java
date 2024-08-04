package team5.doghae.domain.stage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team5.doghae.domain.question.dto.QuestionResponse;
import team5.doghae.domain.stage.repository.StageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StageService {

    private final StageRepository stageRepository;


    public List<QuestionResponse> getStage(Long userId, Long stageId) {


        return null;
    }
}
