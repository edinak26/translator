package phoenix.general.model.entities;

import phoenix.general.interfaces.MetaLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;

public class Grammar implements MetaLanguage {
    private Map<NonTerminal, List<List<String>>> grammar;

    Grammar(List<List<String>> splitText) {
        grammar = GrammarConstructor.getGrammar(splitText);
    }

    public NonTerminal getNonTerminal(List<String> rightPart) {
        for (Entry<NonTerminal, List<List<String>>> grammarEntry : grammar.entrySet()) {
            for (List<String> grammarRightPart : grammarEntry.getValue()) {
                if (rightPart.equals(grammarRightPart)) {
                    return grammarEntry.getKey();
                }
            }
        }
        return null;
    }

    public List<List<String>> getRightPart(String nonTerminal) {
        for (Entry<NonTerminal,List<List<String>>> entry : grammar.entrySet()){
            if(entry.getKey().getName().equals(nonTerminal)){
                return entry.getValue();
            }
        }
        return null;
    }


}
