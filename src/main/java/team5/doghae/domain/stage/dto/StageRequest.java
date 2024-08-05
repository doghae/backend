package team5.doghae.domain.stage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team5.doghae.domain.question.dto.QuestionRequest;

import java.util.List;

public class StageRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Evaluate {

        private List<QuestionRequest.Evaluate> answers;

    }
}
