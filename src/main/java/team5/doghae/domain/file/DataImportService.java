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

    private final StageRepository stageRepository;
    private final QuestionRepository questionRepository;

    public void importDataFromExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Stage> stages = new HashMap<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                Result result = getResult(row);


                if (result.idCell() == null || result.keywordCell() == null || result.problemCell() == null || result.answerCell() == null || result.tagCell() == null || result.stageIdCell() == null) {
                    continue;
                }

                String keyword = result.keywordCell().getStringCellValue();
                String problem = result.problemCell().getStringCellValue();
                String answer = result.answerCell().getStringCellValue();
                String tag = result.tagCell().getStringCellValue();

                List<String> choices = new ArrayList<>();
                choices.add(answer);
                if (result.choice2Cell() != null) choices.add(result.choice2Cell().getStringCellValue());
                if (result.choice3Cell() != null) choices.add(result.choice3Cell().getStringCellValue());
                if (result.choice4Cell() != null) choices.add(result.choice4Cell().getStringCellValue());

                Collections.shuffle(choices);

                Tag questionTag = Tag.valueOf(tag.toUpperCase());
                int stageId = (int) result.stageIdCell().getNumericCellValue();
                Stage stage = getStageForId(stageId, stages);

                Question question = Question.builder()
                        .id((long) result.idCell().getNumericCellValue())
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

    private Result getResult(Row row) {
        Cell idCell = row.getCell(0);
        Cell keywordCell = row.getCell(1);
        Cell problemCell = row.getCell(2);
        Cell answerCell = row.getCell(3);
        Cell choice2Cell = row.getCell(4);
        Cell choice3Cell = row.getCell(5);
        Cell choice4Cell = row.getCell(6);
        Cell tagCell = row.getCell(7);
        Cell stageIdCell = row.getCell(8);
        return new Result(idCell, keywordCell, problemCell, answerCell, choice2Cell, choice3Cell, choice4Cell, tagCell, stageIdCell);
    }

    private record Result(Cell idCell, Cell keywordCell, Cell problemCell, Cell answerCell, Cell choice2Cell,
                          Cell choice3Cell, Cell choice4Cell, Cell tagCell, Cell stageIdCell) {
    }

    private Stage getStageForId(int stageId, Map<String, Stage> stages) {
        String stageTitle = stageId + "번째 스테이지";
        if (!stages.containsKey(stageTitle)) {
            Stage stage = Stage.builder()
                    .id((long) stageId)
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
