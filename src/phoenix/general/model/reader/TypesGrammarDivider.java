package phoenix.general.model.reader;

import phoenix.accessory.constant.Characters;

public class TypesGrammarDivider extends Divider implements Characters {
    TypesGrammarDivider() {
        selector = SELECTOR;
        lineEnd = EMPTY;
        splitRegex = SELECTOR;
        regexSeparators = new String[][]{};
        startLine=":types:";
        stopLine=":regex:";
    }
}
