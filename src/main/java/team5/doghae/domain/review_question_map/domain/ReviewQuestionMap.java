package team5.doghae.domain.review_question_map.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team5.doghae.domain.question.domain.Question;
import team5.doghae.domain.review.domain.Review;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewQuestionMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    private boolean resolved;

    public static ReviewQuestionMap of(Review review, Question question) {
        return ReviewQuestionMap.builder()
                .review(review)
                .question(question)
                .resolved(false)
                .build();
    }
}
