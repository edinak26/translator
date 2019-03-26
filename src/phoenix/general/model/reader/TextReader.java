package phoenix.general.model.reader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TextReader {
    private List<String> text;
    private List<List<String>> splitText;

    private String path;
    private Divider divider;

    public static String DEFAULT_CODE_PATH = "D:\\University\\Java\\translator\\src\\phoenix\\accessory\\info\\code";

    public TextReader(){
        this.divider = new CodeDivider();
        this.path = DEFAULT_CODE_PATH;
    }

    public List<List<String>> getText() throws Exception {
        readText(path);
        splitText();
        return splitText;
    }

    private void readText(String path) throws Exception {
        text=new ArrayList<>();
        FileReader reader = new FileReader(path);
        Scanner scanner = new Scanner(reader);
        while (scanner.hasNextLine()) {
            text.add(scanner.nextLine());
        }
        reader.close();
    }

    private void splitText() {
        splitText = divider.splitText(text);
    }

    public TextReader setPath(String path) {
        this.path = path;
        return this;
    }

    public TextReader setDivider(Divider divider) {
        this.divider = divider;
        return this;
    }
}
