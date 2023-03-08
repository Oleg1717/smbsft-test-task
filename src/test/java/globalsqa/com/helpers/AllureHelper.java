package globalsqa.com.helpers;

import io.qameta.allure.Attachment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AllureHelper {

    @Attachment(value = "{attachName}", type = "application/csv", fileExtension = ".csv")
    public static byte[] attachCsvFile(String attachName, String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }
}