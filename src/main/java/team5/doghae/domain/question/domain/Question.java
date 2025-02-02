package team5.doghae.domain.question.domain;

import jakarta.persistence.*;
import lombok.*;
import team5.doghae.domain.question.Tag;
import team5.doghae.domain.question.domain.converter.QuestionChoicesConverter;
import team5.doghae.domain.review.domain.Review;
import team5.doghae.domain.review_question_map.domain.ReviewQuestionMap;
import team5.doghae.domain.stage.domain.Stage;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "question")
public class Question {

    @Id
    private Long id;

    private String keyword;

    private String problem;

    @Convert(converter = QuestionChoicesConverter.class)
    private List<String> choices;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewQuestionMap> reviewQuestionMaps;

}
