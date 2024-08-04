package team5.doghae.domain.stage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import team5.doghae.common.resolver.AuthUser;
import team5.doghae.common.response.SuccessResponse;
import team5.doghae.common.security.jwt.JwtTokenInfo;
import team5.doghae.domain.stage.service.StageService;

@RestController
@RequiredArgsConstructor
public class StageController {

    private final StageService stageService;

    @GetMapping("/stage/{stageId}")
    public ResponseEntity<?> getStage(
            @AuthUser JwtTokenInfo jwtTokenInfo,
            @PathVariable("stageId") Long stageId
    ) {
        return SuccessResponse.of(
                stageService.getStage(jwtTokenInfo.getUserId(), stageId)
        ).setStatus(HttpStatus.OK);
    }

}
