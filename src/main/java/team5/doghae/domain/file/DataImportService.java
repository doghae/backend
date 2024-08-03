package team5.doghae.domain.file;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team5.doghae.domain.question.Tag;
import team5.doghae.domain.question.domain.Question;
import team5.doghae.domain.question.repository.QuestionRepository;
import team5.doghae.domain.stage.domain.Stage;
import team5.doghae.domain.stage.domain.StageResultInfo;
import team5.doghae.domain.stage.repository.StageRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DataImportService {

    private StageRepository stageRepository;

    private QuestionRepository questionRepository;

    public void importDataFromExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Stage> stages = new HashMap<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                Cell idCell = row.getCell(0);
                Cell keywordCell = row.getCell(1);
                Cell problemCell = row.getCell(2);
                Cell answerCell = row.getCell(3);
                Cell choice2Cell = row.getCell(4);
                Cell choice3Cell = row.getCell(5);
                Cell choice4Cell = row.getCell(6);
                Cell tagCell = row.getCell(7);

                if (idCell == null || keywordCell == null || problemCell == null || answerCell == null) {
                    continue;
                }

                String keyword = keywordCell.getStringCellValue();
                String problem = problemCell.getStringCellValue();
                String answer = answerCell.getStringCellValue();
                String tag = tagCell.getStringCellValue();

                List<String> choices = new ArrayList<>();
                choices.add(answer);
                choices.add(choice2Cell != null ? choice2Cell.getStringCellValue() : "");
                choices.add(choice3Cell != null ? choice3Cell.getStringCellValue() : "");
                choices.add(choice4Cell != null ? choice4Cell.getStringCellValue() : "");

                Collections.shuffle(choices);

                Tag questionTag = Tag.valueOf(tag.toUpperCase());

                Stage stage = getStageForId((int) idCell.getNumericCellValue(), stages);

                Question question = Question.builder()
                        .keyword(keyword)
                        .problem(problem)
                        .choices(choices)
                        .answer(answer)
                        .tag(questionTag)
                        .stage(stage)
                        .build();

                questionRepository.save(question);
            }
        }
    }

    private Stage getStageForId(int stageId, Map<String, Stage> stages) {
        String stageTitle = "Stage " + stageId;
        if (!stages.containsKey(stageTitle)) {
            Stage stage = Stage.builder()
                    .title(stageTitle)
                    .stageResultInfo(new StageResultInfo(0, 0, 0, 0))
                    .timeLimit(15)
                    .isSolved(false)
                    .build();
            stages.put(stageTitle, stageRepository.save(stage));
        }
        return stages.get(stageTitle);
    }
}
