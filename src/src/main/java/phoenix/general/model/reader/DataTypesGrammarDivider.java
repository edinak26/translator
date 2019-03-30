package src.main.java.phoenix.general.model.reader;

import src.main.java.phoenix.accessory.constant.Characters;

public class DataTypesGrammarDivider extends Divider implements Characters {
    DataTypesGrammarDivider() {
        selector = SELECTOR;
        lineEnd = EMPTY;
        splitRegex = SELECTOR;
        regexSeparators = new String[][]{{":;=",SELECTOR}};
        startLine=":types:";
        stopLine=":regex:";
    }
}
