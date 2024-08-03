package team5.doghae.domain.stage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.doghae.domain.stage.domain.Stage;

public interface StageRepository extends JpaRepository<Stage, Long> {
}
