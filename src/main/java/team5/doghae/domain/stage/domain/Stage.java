package team5.doghae.domain.stage.domain;

import jakarta.persistence.*;
import lombok.*;
import team5.doghae.domain.question.domain.Question;
import team5.doghae.domain.user_stage_map.domain.UserStage;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "stages")
public class Stage {

    @Id
    private Long id;

    private String title;

    private int timeLimit;

    @OneToMany(mappedBy = "stage")
    private List<UserStage> userStages;

    @OneToMany(mappedBy = "stage")
    private List<Question> questions;
}
