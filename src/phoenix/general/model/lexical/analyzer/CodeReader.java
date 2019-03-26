package phoenix.general.model.lexical.analyzer;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CodeReader {
    private static List<String> text;
    private static List<List<String>> splitText;

    public static String DEFAULT_CODE_PATH = "D:\\University\\Java\\translator\\src\\phoenix\\accessory\\info\\code";

    public static List<List<String>> getText() throws Exception {
        return getText(DEFAULT_CODE_PATH);
    }

    public static List<List<String>> getText(String path) throws Exception {
        readText(path);
        splitText();
        return splitText;
    }

    private static void readText(String path) throws Exception {
        text=new ArrayList<>();
        FileReader reader = new FileReader(path);
        Scanner scanner = new Scanner(reader);
        while (scanner.hasNextLine()) {
            text.add(scanner.nextLine());
        }
        reader.close();
    }

    private static void splitText() {
        splitText = CodeDivider.splitText(text);
    }
}
