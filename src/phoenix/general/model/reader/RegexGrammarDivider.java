package phoenix.general.model.reader;


import phoenix.accessory.constant.Characters;

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
