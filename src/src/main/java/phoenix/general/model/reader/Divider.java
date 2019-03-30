package src.main.java.phoenix.general.model.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Divider {
    String selector;
    String lineEnd;
    String splitRegex;
    String[] separators = new String[]{};
    String[][] regexSeparators;
    String startLine;
    String stopLine;
    boolean isStarted = false;

    private static List<List<String>> splitText;
    private static String curLine;

    public List<List<String>> splitText(final List<String> text) {
        splitText = new ArrayList<>();
        for (String line : text) {
            if (line.equals(stopLine))
                return splitText;
            if (isStarted) {
                curLine = line;
                addEnd();
                selectSimpleSeparators();
                selectRegexSeparators();
                saveLine();
            }
            if (line.equals(startLine))
                isStarted = true;
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

    private void addEnd() {
        curLine = curLine + lineEnd;
    }

    private void saveLine() {
        splitText.add(Arrays.asList(curLine.split(splitRegex)));
    }
}
