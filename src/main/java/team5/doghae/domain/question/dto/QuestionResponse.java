package team5.doghae.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team5.doghae.domain.question.Tag;
import team5.doghae.domain.question.domain.Question;
import team5.doghae.domain.stage.domain.Stage;
import team5.doghae.domain.stage.domain.StageResultInfo;
import team5.doghae.domain.stage.dto.StageResponse;

import java.util.List;

public class QuestionResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create {
        private StageResponse.Create stage;
        private Long questionId;
        private String keyword;
        private String problem;
        private List<String> choices;
        private Tag tag;

        public static QuestionResponse.Create of(Question question) {
            return Create.builder()
                    .questionId(question.getId())
                    .keyword(question.getKeyword())
                    .problem(question.getProblem())
                    .choices(question.getChoices())
                    .tag(question.getTag())
                    .stage(StageResponse.Create.of(question.getStage()))
                    .build();
        }

    }
}
