package phoenix.general.model.lexical.analyzer.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Divider {
    protected String selector;
    protected String lineEnd;
    protected String splitRegex;
    protected String[] separators;
    protected String[][] regexSeparators;

    private static List<List<String>> splitText;
    private static String curLine;

    public List<List<String>> splitText(final List<String> text) {
        splitText = new ArrayList<>();
        System.out.println(text);
        for (String line : text) {
            curLine = line;
            addEnd();
            selectSimpleSeparators();
            selectRegexSeparators();
            saveLine();
        }
        return splitText;
    }

    private void selectSimpleSeparators() {
        for (String separator : separators) {
            curLine = curLine.replaceAll(separator, selector + separator + selector);
        }
    }

    private void selectRegexSeparators() {
        for (String[] regexSeparator : regexSeparators) {
            curLine = curLine.replaceAll(regexSeparator[0], regexSeparator[1]);
        }
    }

    private void addEnd(){
        curLine = curLine + lineEnd;
    }

    private void saveLine(){
        System.out.println(curLine);
        splitText.add(Arrays.asList(curLine.split(splitRegex)));
    }
}
