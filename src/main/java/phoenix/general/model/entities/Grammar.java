package phoenix.general.model.entities;

import phoenix.general.model.reader.MetaLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;

public class Grammar implements MetaLanguage {
    private Map<String, List<List<String>>> grammar;

    Grammar(List<List<String>> splitText) {
        grammar = new HashMap<>();
        for (List<String> rule : splitText) {
            String nonTerminal = rule.remove(0);
            grammar.put(nonTerminal, splitRule(rule));
        }
    }

    private List<List<String>> splitRule(List<String> rule) {
        List<List<String>> rightParts = new ArrayList<>();
        rightParts.add(new ArrayList<>());
        for (String elem : rule) {
            if (elem.equals(GRAMMAR_OR)) {
                rightParts.add(new ArrayList<>());
            } else {
                rightParts.get(rightParts.size() - 1).add(elem);
            }
        }
        return rightParts;
    }

    public String getNonTerminal(List<String> rightPart){
        for(Entry<String,List<List<String>>> grammarEntry: grammar.entrySet()){
            for(List<String> grammarRightPart : grammarEntry.getValue()){
                if(rightPart.equals(grammarRightPart)){
                    return grammarEntry.getKey();
                }
            }
        }
        return null;
    }


}
