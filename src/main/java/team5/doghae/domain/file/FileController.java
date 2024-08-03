package team5.doghae.domain.file;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final DataImportService dataImportService;

    @GetMapping("/getFile")
    public String readFile(@RequestPart MultipartFile multipartFile) throws IOException {
        dataImportService.importDataFromExcel(multipartFile);
        return "ok";
    }
}
