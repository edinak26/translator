package phoenix.general.model.reader;

import phoenix.accessory.constant.Characters;

public class GrammarDivider extends Divider implements Characters {
    public GrammarDivider() {
        selector = SELECTOR;
        lineEnd = EMPTY;
        splitRegex = SELECTOR;
        separators = new String[]{GRAMMAR_OR};
        regexSeparators = new String[][]{
                {"(<[^<>|]*>)",SELECTOR+"$1"+SELECTOR},
                {"::=",SELECTOR},
                {"\\s+",SELECTOR},
                {"(<[^<>|]*)"+SELECTOR+"([^<>|]*>)","$1"+SPACE+"$2"},
                {"(<[^<>|]*)"+SELECTOR+"([^<>|]*>)","$1"+SPACE+"$2"},
                {SELECTOR+"+",SELECTOR},
                {"^@", ""},
                {"@$", ""},
        };
        startLine=":grammar:";
        stopLine=":types:";
    }
}
