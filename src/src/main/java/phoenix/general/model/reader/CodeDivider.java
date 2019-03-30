package src.main.java.phoenix.general.model.reader;

import src.main.java.phoenix.general.model.lexical.analyzer.SeparatorConstants;

public class CodeDivider extends Divider implements SeparatorConstants {

    public CodeDivider() {
        selector = " ";
        lineEnd = " \\n";
        splitRegex = selector;
        separators = new String[]{"==", "<=", ">=", "!=", "<<", ">>",
                "\\+", "\\*", "/", "\\(", "\\)", "\\{", "}", "-", ",", ":"};
        regexSeparators = new String[][]{
                {REGEX_EQUAL, SELECTED_EQUAL},
                {REGEX_LESS, SELECTED_LESS},
                {REGEX_MORE, SELECTED_MORE},
                {REGEX_START, SELECTED_START},
                {REGEX_END, SELECTED_END},
                {REGEX_SPACE, SELECTED_SPACE}
        };
        startLine=null;
        stopLine=null;
        isStarted=true;
    }
}
