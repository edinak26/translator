package phoenix.model.reader;


import phoenix.interfaces.MetaLanguage;

public class DataTypesGrammarDivider extends Divider implements MetaLanguage {
    DataTypesGrammarDivider() {
        selector = SELECTOR;
        lineEnd = EMPTY;
        splitRegex = SELECTOR;
        regexSeparators = new String[][]{{":;=",SELECTOR}};
        startLine=":types:";
        stopLine=":regex:";
    }
}
