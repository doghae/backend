package team5.doghae.domain.stage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team5.doghae.domain.stage.domain.Stage;

public class StageResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        private Long stageId;
        private String title;
        private int timeLimit;

        public static Create of(Stage stage) {
            return Create.builder()
                    .stageId(stage.getId())
                    .title(stage.getTitle())
                    .timeLimit(stage.getTimeLimit())
                    .build();
        }

    }
}
