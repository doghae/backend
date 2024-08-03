package team5.doghae.domain.stage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team5.doghae.domain.stage.repository.StageRepository;

@Service
@RequiredArgsConstructor
public class StageService {

    private final StageRepository stageRepository;

}
