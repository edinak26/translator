package phoenix.general.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

public class CodeReader {
    private List<String> text;
    private List<List<String>> splitText;

    private static String DEFAULT_CODE_PATH = "D:\\University\\J1ava\\translator\\src\\phoenix\\accessory\\info\\code";

    public CodeReader() throws Exception {
        saveText(DEFAULT_CODE_PATH);
    }

    public CodeReader(String path) throws Exception {
        saveText(path);
    }
    private void saveText(String path) throws Exception{
        readText(path);
        splitText();
    }
    private void readText(String path) throws Exception {

        FileReader reader = new FileReader(path);
        Scanner scanner = new Scanner(reader);
        while (scanner.hasNext()) {
            text.add(scanner.nextLine());
        }
        reader.close();
    }

    private void splitText(){
        Divider.splitText(text);
    }

}
