package ma.ests.biblio.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVUtil {

    public static void export(String fileName, List<String[]> data) {
        try (FileWriter writer = new FileWriter(fileName)) {

            for (String[] ligne : data) {
                writer.append(String.join(";", ligne));
                writer.append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
