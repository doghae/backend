package team5.doghae.domain.question.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team5.doghae.domain.question.Tag;
import team5.doghae.domain.stage.domain.Stage;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class QuestionResponse {

    public static class Create {
        private Long id;
        private String keyword;
        private String problem;
        private List<String> choices;
        private Tag tag;
        private String answer;
        private Stage stage;
    }
}
