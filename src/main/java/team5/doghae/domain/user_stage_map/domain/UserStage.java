package team5.doghae.domain.user_stage_map.domain;

import jakarta.persistence.*;
import lombok.*;
import team5.doghae.domain.stage.domain.Stage;
import team5.doghae.domain.stage.domain.StageResultInfo;
import team5.doghae.domain.user.domain.User;

import java.time.LocalDate;
import java.time.ZoneId;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @Embedded
    private StageResultInfo stageResultInfo;

    private LocalDate solvedDate;

    public static UserStage of(User user, Stage stage, StageResultInfo stageResultInfo) {
        return UserStage.builder()
                .user(user)
                .stage(stage)
                .stageResultInfo(stageResultInfo)
                .solvedDate(LocalDate.now(ZoneId.of("Asia/Seoul")))
                .build();
    }

    public void changeResultInfo(StageResultInfo resultInfo) {
        this.stageResultInfo = resultInfo;
    }
}
