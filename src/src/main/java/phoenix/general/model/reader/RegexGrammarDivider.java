package src.main.java.phoenix.general.model.reader;

import src.main.java.phoenix.accessory.constant.Characters;

public class RegexGrammarDivider extends Divider implements Characters {
    RegexGrammarDivider() {
        selector = SELECTOR;
        lineEnd = EMPTY;
        splitRegex = SELECTOR;
        regexSeparators = new String[][]{{";;=",SELECTOR}};
        startLine=":regex:";
        stopLine=null;
    }
}
