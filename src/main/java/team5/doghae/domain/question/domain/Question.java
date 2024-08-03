package team5.doghae.domain.question.domain;

import jakarta.persistence.*;
import lombok.*;
import team5.doghae.domain.question.domain.converter.QuestionChoicesConverter;
import team5.doghae.domain.quiz.domain.Quiz;
import team5.doghae.domain.review.domain.Review;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    private String problem;

    @Convert(converter = QuestionChoicesConverter.class)
    private List<String> choices;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    private int answer;
}
