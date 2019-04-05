package phoenix.general.model.reader;


import phoenix.accessory.constant.Characters;
import phoenix.general.model.entities.Grammar;

public class RegexGrammarDivider extends Divider implements Characters, MetaLanguage {
    RegexGrammarDivider() {
        selector = SELECTOR;
        lineEnd = EMPTY;
        splitRegex = SELECTOR;
        regexSeparators = new String[][]{{GRAMMAR_ASSIGNMENT,SELECTOR}};
        startLine=":regex:";
        stopLine=null;
    }
}
