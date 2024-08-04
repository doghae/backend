package team5.doghae.domain.file;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final DataImportService dataImportService;

    @PostMapping("/getFile")
    public String readFile(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        dataImportService.importDataFromExcel(multipartFile);
        return "ok";
    }
}
