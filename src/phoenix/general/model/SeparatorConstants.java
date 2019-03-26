package phoenix.general.model;

public interface SeparatorConstants {
    String REGEX_EQUAL = "([^=^>^<^!])=([^=])";
    String SELECTED_EQUAL = "$1 = $2";
    String REGEX_LESS = "([^<])<([^=^<])";
    String SELECTED_LESS = "$1 < $2";
    String REGEX_MORE = "([^>])>([^=^>])";
    String SELECTED_MORE = "$1 > $2";
    String REGEX_SPACE = "\\s+";
    String SELECTED_SPACE = " ";
    String REGEX_START = "^ ";
    String SELECTED_START = "";
    String REGEX_END = " $";
    String SELECTED_END = "";

    String REGEX_IDENTIFIER_LEXEME = "^[a-zA-Z_][a-zA-Z0-9_]*$";
    String REGEX_POINTER_LEXEME = "^#[a-zA-Z0-9_]+$";
    String REGEX_VAL_TYPE = "int|double|bool";
    String REGEX_SEPARATOR = "^(\\n|[+*/\\(\\){}=><,#:-]|==|<=|>=|!=|<<|>>|cin|cout|if|then|while|for|to|by|true|false)$";
    String REGEX_CONSTANT =
            "^-?[1-9][0-9]*\\.[0-9]*$|" +
                    "^-?0\\.[0-9]*$|" +
                    "^-?\\.[0-9]+$|" +
                    "^-?[1-9][0-9]*$|" +
                    "^-?0$";
    String REGEX_MINUS_IS_CONSTANT = "\\+$|\\*$|/$|\\($|\\{$|}$|,$|\\.$|=$";
}
