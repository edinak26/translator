package phoenix.general.model.lexical.analyzer;

import phoenix.general.model.SeparatorConstants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeDivider implements SeparatorConstants {
    private static final String SPACE = " ";
    private static final String[] SEPARATORS = {"==", "<=", ">=", "!=", "<<", ">>",
            "\\+", "\\*", "/", "\\(", "\\)", "\\{", "}", "-", ",", ":"};
    private static final String[][] REGEX_SEPARATORS = {
            {REGEX_EQUAL, SELECTED_EQUAL},
            {REGEX_LESS, SELECTED_LESS},
            {REGEX_MORE, SELECTED_MORE},
            {REGEX_START, SELECTED_START},
            {REGEX_END, SELECTED_END},
            {REGEX_SPACE, SELECTED_SPACE}
    };

    private static List<List<String>> splitText;
    private static String curLine;

    public static List<List<String>> splitText(final List<String> text) {
        splitText = new ArrayList<>();
        for (String line : text) {
            curLine = line;
            selectSimpleSeparators();
            selectRegexSeparators();
            saveLine();
        }
        return splitText;
    }

    private static void selectSimpleSeparators() {
        for (String separator : SEPARATORS) {
            curLine = curLine.replaceAll(separator, SPACE + separator + SPACE);
        }
    }

    private static void selectRegexSeparators() {
        for (String[] regexSeparator : REGEX_SEPARATORS) {
            System.out.println(regexSeparator[0]);
            curLine = curLine.replaceAll(regexSeparator[0], regexSeparator[1]);
        }
    }

    private static void addEnd(){
        curLine = curLine + " \\n";
    }

    private static void saveLine(){
        addEnd();
        splitText.add(Arrays.asList(curLine.split(SPACE)));
    }
}

