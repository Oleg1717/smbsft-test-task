package globalsqa.com.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static void writeDataToFile(String filePath, String content) {
        File file = new File(filePath);
        try {
            PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}