package phoenix.general.model.reader;

import phoenix.accessory.constant.Characters;

public class GrammarDivider extends Divider implements Characters {
    public GrammarDivider() {
        selector = SELECTOR;
        lineEnd = EMPTY;
        separators = new String[]{GRAMMAR_EQUALITY, GRAMMAR_OR};
        regexSeparators = new String[][]{};
    }
}
