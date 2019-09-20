package phoenix.model.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static phoenix.interfaces.Characters.EMPTY;

public abstract class Divider {
    String selector;
    String lineEnd;
    String splitRegex;
    String[] separators = new String[]{};
    String[][] regexSeparators;
    String[][] multiRegexSeparators = new String[][]{};
    String startLine;
    String stopLine;
    boolean isStarted = false;

    private static List<List<String>> splitText;
    private static String curLine;

    public List<List<String>> splitText(final List<String> text) {
        splitText = new ArrayList<>();
        for (String line : text) {
            if(!line.equals(EMPTY)) {
                if (line.equals(stopLine))
                    return splitText;
                if (isStarted) {
                    curLine = line;
                    addEnd();
                    selectSimpleSeparators();
                    selectRegexSeparators();
                    selectMultiRegexSeparators();
                    saveLine();
                }
                if (line.equals(startLine))
                    isStarted = true;
            }
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
    private void selectMultiRegexSeparators(){
        for(String[] multiRegexSeparator : multiRegexSeparators){
            Pattern pattern = Pattern.compile(multiRegexSeparator[0]);
            Matcher matcher = pattern.matcher(curLine);
            while(matcher.find()){
                curLine = curLine.replaceAll(multiRegexSeparator[0], multiRegexSeparator[1]);
                matcher = pattern.matcher(curLine);
            }
        }
    }

    private void addEnd() {
        curLine = curLine + lineEnd;
    }

    private void saveLine() {
        splitText.add(Arrays.asList(curLine.split(splitRegex)));
    }
}
