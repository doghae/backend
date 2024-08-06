package team5.doghae.domain.review.dto;


import lombok.Getter;
import team5.doghae.domain.question.Tag;
import team5.doghae.domain.review_question_map.domain.ReviewQuestionMap;

public class ReviewResponse {

    @Getter
    public static class WrongAnswer {

        private final Long questionId;
        private final String keyword;
        private final Tag tag;

        public WrongAnswer(ReviewQuestionMap reviewQuestionMap) {
            this.questionId = reviewQuestionMap.getQuestion().getId();
            this.keyword = reviewQuestionMap.getQuestion().getKeyword();
            this.tag = reviewQuestionMap.getQuestion().getTag();
        }
    }
}
