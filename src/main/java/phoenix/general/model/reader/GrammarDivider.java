package phoenix.general.model.reader;


import phoenix.accessory.constant.Characters;

public class GrammarDivider extends Divider implements Characters, MetaLanguage {
    public GrammarDivider() {
        selector = SELECTOR;
        lineEnd = EMPTY;
        splitRegex = SELECTOR;
        separators = new String[]{
                REGEX_NONE + GRAMMAR_OR
        };
        regexSeparators = new String[][]{
                {GRAMMAR_NON_TERMINAL_GROUP_REGEX, SELECTOR + REGEX_GROUP + ONE + SELECTOR},
                {GRAMMAR_EQUAL, SELECTOR},
                {WHITE_SPACE_REGEX+REGEX_ONE_OR_MORE, SELECTOR},
                {SELECTOR + REGEX_ONE_OR_MORE, SELECTOR},
                {REGEX_LINE_START+SELECTOR, EMPTY},
                {SELECTOR+REGEX_LINE_END, EMPTY},
        };
        multiRegexSeparators =  new String[][]{
                {"(<[^<>|]*)" + SELECTOR + "([^<>|]*>)", "$1" + SPACE + "$2"},
        };
        startLine = ":grammar:";
        stopLine = ":types:";
    }
}
