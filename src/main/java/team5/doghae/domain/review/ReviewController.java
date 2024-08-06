package team5.doghae.domain.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team5.doghae.common.resolver.AuthUser;
import team5.doghae.common.response.SuccessResponse;
import team5.doghae.common.security.jwt.JwtTokenInfo;
import team5.doghae.domain.review.dto.ReviewResponse;
import team5.doghae.domain.review.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/reviews")
    public ResponseEntity<SuccessResponse<List<ReviewResponse.WrongAnswer>>> getWrongQuestion(
            @AuthUser JwtTokenInfo jwtTokenInfo
    ) {
        return SuccessResponse.of(
                reviewService.getWrongQuestions(jwtTokenInfo.getUserId())
        ).setStatus(HttpStatus.OK);
    }

}
