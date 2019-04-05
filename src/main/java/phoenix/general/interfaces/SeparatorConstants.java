package phoenix.general.interfaces;

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
}
