package team5.doghae.domain.stage.domain;

import jakarta.persistence.*;
import lombok.*;
import team5.doghae.domain.quiz.domain.Quiz;

import java.time.LocalDate;

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

    private Integer rightAnswerCount;

    private LocalDate date;

    @OneToOne(fetch = FetchType.LAZY)
    private Quiz quiz;

    public static Stage of(Integer count, LocalDate date) {
        return Stage.builder()
                .rightAnswerCount(count)
                .date(date)
                .build();
    }
}
