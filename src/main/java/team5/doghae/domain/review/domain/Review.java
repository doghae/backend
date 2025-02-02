package team5.doghae.domain.review.domain;

import jakarta.persistence.*;
import lombok.*;
import team5.doghae.domain.question.domain.Question;
import team5.doghae.domain.review_question_map.domain.ReviewQuestionMap;
import team5.doghae.domain.user.domain.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewQuestionMap> reviewQuestionMaps;

    public static Review of(User user) {
        return Review.builder()
                .user(user)
                .build();
    }

}
