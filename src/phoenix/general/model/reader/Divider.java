package phoenix.general.model.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Divider {
    protected String selector;
    protected String lineEnd;
    protected String[] separators;
    protected String[][] regexSeparators;

    private static List<List<String>> splitText;
    private static String curLine;

    public List<List<String>> splitText(final List<String> text) {
        splitText = new ArrayList<>();
        for (String line : text) {
            curLine = line;
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
            System.out.println(regexSeparator[0]);
            curLine = curLine.replaceAll(regexSeparator[0], regexSeparator[1]);
        }
    }

    private void addEnd(){
        curLine = curLine + lineEnd;
    }

    private void saveLine(){
        addEnd();
        splitText.add(Arrays.asList(curLine.split(selector)));
    }
}
