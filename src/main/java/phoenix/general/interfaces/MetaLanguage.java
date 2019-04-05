package phoenix.general.interfaces;


public interface MetaLanguage extends Regex, Characters {
    String GRAMMAR_OR = "|";
    String GRAMMAR_EQUAL = "::=";
    String GRAMMAR_ASSIGNMENT = ";;=";

    String NON_TERMINAL_START = "<";
    String NON_TERMINAL_END = ">";
    String GRAMMAR_ELEMENT_NAME_REGEX =
            OPEN_REGEX_CHARACTER_CLASS + REGEX_NOT
                    + NON_TERMINAL_START
                    + NON_TERMINAL_END
                    + GRAMMAR_OR
                    + CLOSE_REGEX_CHARACTER_CLASS;

    String GRAMMAR_TERMINAL_REGEX = "";//TODO add terminal check in future

    String GRAMMAR_NON_TERMINAL_GROUP_REGEX =
            OPEN_REGEX_GROUP
                    + NON_TERMINAL_START
                    + GRAMMAR_ELEMENT_NAME_REGEX
                    + REGEX_NONE_OR_MORE
                    + NON_TERMINAL_END
                    + CLOSE_REGEX_GROUP;

    String GRAMMAR_SECTION_REGEX =
            COLON
                    + GRAMMAR_ELEMENT_NAME_REGEX
                    + COLON;

    String OPEN_BLOCK_START = "<:";
    String OPEN_BLOCK_END = ":>";

    String GRAMMAR_OPEN_BLOCK_REGEX =
            OPEN_BLOCK_START
            + GRAMMAR_ELEMENT_NAME_REGEX
            + OPEN_BLOCK_END;

    String CLOSE_BLOCK_START = "<;";
    String CLOSE_BLOCK_END = ";>";

    String GRAMMAR_CLOSE_BLOCK_REGEX =
            CLOSE_BLOCK_START
                    + GRAMMAR_ELEMENT_NAME_REGEX
                    + CLOSE_BLOCK_END;

    String SELECTOR = "∫";
}
