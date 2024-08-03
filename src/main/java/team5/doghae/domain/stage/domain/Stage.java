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
@Table(name = "stage")
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private StageResultInfo stageResultInfo;

    private LocalDate solvedDate;

    private String title;

    private int timeLimit;

    private boolean isSolved;

    @OneToMany(mappedBy = "stage")
    private List<UserStage> userStages;

    @OneToMany(mappedBy = "stage")
    private List<Question> questions;
}
