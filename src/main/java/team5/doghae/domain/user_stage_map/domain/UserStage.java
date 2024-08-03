package team5.doghae.domain.user_stage_map.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import team5.doghae.domain.stage.domain.Stage;
import team5.doghae.domain.user.domain.User;

@Entity
@Getter
@Setter
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

}
