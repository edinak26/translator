package phoenix.model.reader;


import phoenix.interfaces.MetaLanguage;

public class RegexGrammarDivider extends Divider implements MetaLanguage {
    RegexGrammarDivider() {
        selector = SELECTOR;
        lineEnd = EMPTY;
        splitRegex = SELECTOR;
        regexSeparators = new String[][]{{GRAMMAR_ASSIGNMENT,SELECTOR}};
        startLine=":regex:";
        stopLine=":rpn:";
    }
}
