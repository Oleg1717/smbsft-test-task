package globalsqa.com.helpers;

import io.qameta.allure.Attachment;

public class AllureHelper {

    @Attachment(value = "{attachName}", type = "application/csv", fileExtension = ".csv")
    public static String attachAsCsvFile(String attachName, String data) {
        return data;
    }
}