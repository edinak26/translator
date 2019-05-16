package phoenix.interfaces;

import java.util.regex.Pattern;

public interface Patterns extends MetaLanguage{
    Pattern GRAMMAR_OPEN_BLOCK_PATTERN = Pattern.compile(GRAMMAR_OPEN_BLOCK_REGEX );
    Pattern GRAMMAR_CLOSE_BLOCK_PATTERN = Pattern.compile(GRAMMAR_CLOSE_BLOCK_REGEX );

    Pattern GRAMMAR_NON_TERMINAL_GROUP_PATTERN = Pattern.compile(GRAMMAR_NON_TERMINAL_GROUP_REGEX);
}
