package phoenix.general.model.reader;

import phoenix.general.model.SeparatorConstants;

public class CodeDivider extends Divider implements SeparatorConstants {

    public CodeDivider() {
        selector = " ";
        lineEnd = " \\n";
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
    }
}
