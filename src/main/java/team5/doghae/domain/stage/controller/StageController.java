package team5.doghae.domain.stage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team5.doghae.common.resolver.AuthUser;
import team5.doghae.common.response.SuccessResponse;
import team5.doghae.common.security.jwt.JwtTokenInfo;
import team5.doghae.domain.question.dto.QuestionResponse;
import team5.doghae.domain.stage.dto.StageRequest;
import team5.doghae.domain.stage.service.StageService;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class StageController {

    private final StageService stageService;

    @GetMapping("/stage/{stageId}")
    public ResponseEntity<SuccessResponse<List<QuestionResponse.Create>>> getStage(
            @AuthUser JwtTokenInfo jwtTokenInfo,
            @PathVariable("stageId") Long stageId
    ) {
        return SuccessResponse.of(
                stageService.getStage(jwtTokenInfo.getUserId(), stageId)
        ).setStatus(HttpStatus.OK);
    }

    @PostMapping("/stage/{stageId}")
    public ResponseEntity<SuccessResponse<List<QuestionResponse.Evaluate>>> evaluateStage(
            @AuthUser JwtTokenInfo jwtTokenInfo,
            @PathVariable("stageId") Long stageId,
            @RequestBody StageRequest.Evaluate evaluateRequest
    ) {
        return SuccessResponse.of(
                stageService.evaluateStage(jwtTokenInfo.getUserId(), stageId, evaluateRequest)
        ).setStatus(HttpStatus.OK);
    }

}
