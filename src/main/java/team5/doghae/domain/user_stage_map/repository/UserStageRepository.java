package team5.doghae.domain.user_stage_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.doghae.domain.user_stage_map.domain.UserStage;

public interface UserStageRepository extends JpaRepository<UserStage, Long> {

}
