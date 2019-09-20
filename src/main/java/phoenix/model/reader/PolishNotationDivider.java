package phoenix.model.reader;

import phoenix.interfaces.MetaLanguage;

public class PolishNotationDivider extends Divider implements MetaLanguage {
    PolishNotationDivider() {
        selector = SELECTOR;
        lineEnd = EMPTY;
        splitRegex = SELECTOR;
        regexSeparators = new String[][]{
                {":=:",SELECTOR},
                {"(<[0-9]+>|#[0-9]+)", SELECTOR+"$1"+SELECTOR},
                {"([;|]|->>)", SELECTOR+"$1"+SELECTOR},

                {WHITE_SPACE_REGEX+REGEX_ONE_OR_MORE, SELECTOR},
                {SELECTOR + REGEX_ONE_OR_MORE, SELECTOR},
                {REGEX_LINE_START+SELECTOR, EMPTY},
                {SELECTOR+REGEX_LINE_END, EMPTY},};
        multiRegexSeparators =  new String[][]{
                {"(<[^<>|]*)" + SELECTOR + "([^<>|]*>)", "$1" + SPACE + "$2"},
        };
        startLine=":rpn:";
        stopLine=null;
    }
}
