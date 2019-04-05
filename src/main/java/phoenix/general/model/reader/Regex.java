package phoenix.general.model.reader;

public interface Regex extends Numbers{
    String OPEN_REGEX_CHARACTER_CLASS = "[";
    String REGEX_NOT = "^";
    String CLOSE_REGEX_CHARACTER_CLASS = "]";

    String OPEN_REGEX_GROUP = "(";
    String CLOSE_REGEX_GROUP = ")";

    String REGEX_ONE_OR_MORE = "+";
    String REGEX_NONE_OR_MORE = "*";

    String REGEX_GROUP = "$";

    String REGEX_NONE = "\\";

    String WHITE_SPACE_REGEX = "\\s";

    String REGEX_LINE_START = "^";
    String REGEX_LINE_END = "$";
}
