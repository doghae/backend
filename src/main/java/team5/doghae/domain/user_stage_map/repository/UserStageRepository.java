package team5.doghae.domain.user_stage_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.doghae.domain.stage.domain.Stage;
import team5.doghae.domain.user.domain.User;
import team5.doghae.domain.user_stage_map.domain.UserStage;

import java.util.Optional;

public interface UserStageRepository extends JpaRepository<UserStage, Long> {


    Optional<UserStage> findByUserAndStage(User user, Stage stage);
}
