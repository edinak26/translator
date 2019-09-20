package phoenix.model.reader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TextReader {
    private List<String> text;
    private List<List<String>> splitText;

    private String path;
    private Divider divider;

    public static String DEFAULT_CODE_PATH = "D:\\University\\Java\\translator\\src\\main\\resources\\code";
    public static String DEFAULT_GRAMMAR_PATH = "D:\\University\\Java\\translator\\src\\main\\resources\\grammar";

    public static TextReader code(){
        TextReader reader = new TextReader();
        reader.divider = new CodeDivider();
        reader.path = DEFAULT_CODE_PATH;
        return reader;
    }

    public static TextReader grammar(){
        TextReader reader = new TextReader();
        reader.divider = new GrammarDivider();
        reader.path = DEFAULT_GRAMMAR_PATH;
        return reader;
    }

    public static TextReader regexGrammar(){
        TextReader reader = new TextReader();
        reader.divider = new RegexGrammarDivider();
        reader.path = DEFAULT_GRAMMAR_PATH;
        return reader;
    }

    public static TextReader polishNotation(){
        TextReader reader = new TextReader();
        reader.divider = new PolishNotationDivider();
        reader.path = DEFAULT_GRAMMAR_PATH;
        return reader;
    }

    public static TextReader dataTypesGrammar(){
        TextReader reader = new TextReader();
        reader.divider = new DataTypesGrammarDivider();
        reader.path = DEFAULT_GRAMMAR_PATH;
        return reader;
    }

    public List<List<String>> get() throws Exception {
        readText();
        splitText();
        return splitText;
    }

    private void readText() throws Exception {
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

    public TextReader setPath(String path){
        this.path = path;
        return this;
    }
}
