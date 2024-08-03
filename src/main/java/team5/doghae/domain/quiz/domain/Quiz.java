package team5.doghae.domain.quiz.domain;

import jakarta.persistence.*;
import lombok.*;
import team5.doghae.domain.question.domain.Question;
import team5.doghae.domain.quiz.domain.enums.Tag;
import team5.doghae.domain.stage.domain.Stage;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "quiz")
public class Quiz {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    private String title;

    private int limitSeconds;

    @OneToOne(mappedBy = "quiz", cascade = CascadeType.ALL)
    private Stage stage;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

}
