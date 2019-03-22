package phoenix.general.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Divider implements SeparatorConstants {
    private static final String SPACE = " ";
    private static final String REGEX_SPACES = "\\s+";
    private static final String[] SEPARATORS = {"==", "<=", ">=", "!=", "<<", ">>",
            "\\+", "\\*", "/", "\\(", "\\)", "\\{", "}", "-", ",", ":"};
    private static final String[][] REGEX_SEPARATORS = {
            {REGEX_EQUAL, SELECTED_EQUAL},
            {REGEX_LESS, SELECTED_LESS},
            {REGEX_MORE, SELECTED_MORE},
            {REGEX_SPACE, SELECTED_SPACE},
            {REGEX_START, SELECTED_START},
            {REGEX_END, SELECTED_END}
    };

    public static List<List<String>> splitText(final List<String> text) {
        List<List<String>> splitText = new ArrayList<>();
        for (String line : text) {
            selectSimpleSeparators(line);
            selectRegexSeparators(line);
            splitText.add(Arrays.asList(line.split(SPACE)));
        }
        return splitText;
    }


    private static void selectSimpleSeparators(String line) {
        for (String separator : SEPARATORS) {
            line = line.replaceAll(separator, SPACE + separator + SPACE);
        }
    }

    private static void selectRegexSeparators(String line) {
        for (String[] regexSeparator : REGEX_SEPARATORS) {
            line = line.replaceAll(regexSeparator[0], regexSeparator[1]);
        }
    }
}

