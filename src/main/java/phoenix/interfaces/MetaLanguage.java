package phoenix.interfaces;


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
                    + COLON
                    + SEMICOLON
                    + GRAMMAR_OR
                    + CLOSE_REGEX_CHARACTER_CLASS;

    String GRAMMAR_TERMINAL_REGEX = "";//TODO addChild terminal check in future

    String GRAMMAR_NON_TERMINAL_GROUP_REGEX =
            OPEN_REGEX_GROUP
                    + NON_TERMINAL_START
                    + GRAMMAR_ELEMENT_NAME_REGEX
                    + REGEX_NONE_OR_MORE
                    + NON_TERMINAL_END
                    + CLOSE_REGEX_GROUP;

    String GRAMMAR_SECTION_START = COLON;
    String GRAMMAR_SECTION_END = COLON;

    String GRAMMAR_SECTION_REGEX = //TODO addChild checking of sections
            GRAMMAR_SECTION_START
                    + GRAMMAR_ELEMENT_NAME_REGEX
                    + REGEX_ONE_OR_MORE
                    + GRAMMAR_SECTION_END;

    String OPEN_BLOCK_START = "<:";
    String OPEN_BLOCK_END = ":>";

    String GRAMMAR_OPEN_BLOCK_REGEX =
            OPEN_BLOCK_START
                    + GRAMMAR_ELEMENT_NAME_REGEX
                    + REGEX_ONE_OR_MORE
                    + OPEN_BLOCK_END;

    String CLOSE_BLOCK_START = "<;";
    String CLOSE_BLOCK_END = ";>";

    String GRAMMAR_CLOSE_BLOCK_REGEX =
            CLOSE_BLOCK_START
                    + GRAMMAR_ELEMENT_NAME_REGEX
                    + REGEX_ONE_OR_MORE
                    + CLOSE_BLOCK_END;

    String GRAMMAR_DEFAULT_BLOCK = OPEN_BLOCK_START+OPEN_BLOCK_END;

    String SELECTOR = "∫";
}
