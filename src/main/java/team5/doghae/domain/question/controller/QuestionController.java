package team5.doghae.domain.question.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team5.doghae.common.resolver.AuthUser;
import team5.doghae.common.response.SuccessResponse;
import team5.doghae.common.security.jwt.JwtTokenInfo;
import team5.doghae.domain.question.dto.QuestionRequest;
import team5.doghae.domain.question.dto.QuestionResponse;
import team5.doghae.domain.question.service.QuestionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;


    @GetMapping("/{questionId}")
    public ResponseEntity<SuccessResponse<QuestionResponse.Create>> getSingleWrongedQuestion(
            @AuthUser JwtTokenInfo jwtTokenInfo,
            @PathVariable("questionId") Long questionId
    ) {
        return SuccessResponse.of(
                questionService.getSingleWrongedQuestion(jwtTokenInfo.getUserId(), questionId)
        ).setStatus(HttpStatus.OK);
    }

    @PostMapping("/{questionId}")
    public ResponseEntity<SuccessResponse<QuestionResponse.Evaluate>> retryWrongedQuestion(
            @AuthUser JwtTokenInfo jwtTokenInfo,
            @PathVariable("questionId") Long questionId,
            @RequestBody QuestionRequest.Evaluate retryRequest
    ) {
        return SuccessResponse.of(
                questionService.retryWrongedQuestion(jwtTokenInfo.getUserId(), questionId, retryRequest)
        ).setStatus(HttpStatus.OK);
    }
}
