package team5.doghae.domain.review.domain;

import jakarta.persistence.*;
import lombok.*;
import team5.doghae.domain.question.domain.Question;
import team5.doghae.domain.user.domain.User;

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

    @ManyToOne
    private User user;

    @OneToMany
    List<Question> wrongQuestions;


}
