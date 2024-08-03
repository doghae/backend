package team5.doghae.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.doghae.domain.question.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
