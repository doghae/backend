package team5.doghae.domain.stage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team5.doghae.domain.stage.service.StageService;

@RestController
@RequiredArgsConstructor
public class StageController {

    private final StageService stageService;

    @GetMapping("/stage/{stageId}")
    public ResponseEntity<?> getStage() {


        return null;
    }
}
