package team5.doghae.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestionRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Evaluate {
        private Long questionId;
        private String answer;
    }
}
