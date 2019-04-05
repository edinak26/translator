package phoenix.general.model.entities;

import phoenix.general.interfaces.MetaLanguage;
import phoenix.general.model.syntax.analyzer.RelationsTable;
import phoenix.general.model.syntax.analyzer.SetsSearcher;

import java.util.*;
import java.util.Map.Entry;

public class Grammar implements MetaLanguage {
    private Map<NonTerminal, List<List<String>>> grammar;

    public Grammar(List<List<String>> splitText) {
        grammar = GrammarConstructor.getGrammar(splitText);
        SetsSearcher.setGrammar(this);
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
        for (Entry<NonTerminal, List<List<String>>> entry : grammar.entrySet()) {
            if (entry.getKey().getName().equals(nonTerminal)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public List<String> getUniqueTerminals() {
        Set<String> terminals = new LinkedHashSet<>();
        for (Entry<NonTerminal, List<List<String>>> entry : grammar.entrySet()) {
            terminals.add(entry.getKey().getName());
            for (List<String> rightPart : entry.getValue()) {
                terminals.addAll(rightPart);
            }
        }
        return new ArrayList<>(terminals);
    }


    public void configRelations(RelationsTable relations) {
        for (Entry<NonTerminal, List<List<String>>> entry : grammar.entrySet()) {
            for (List<String> rightPart : entry.getValue()) {
                for (int i = 0; i < rightPart.size() - 1; i++) {
                    String currTerminal = rightPart.get(i);
                    String nextTerminal = rightPart.get(i+1);
                    Set<String> curLast = SetsSearcher.get().last(currTerminal);
                    Set<String> nextFirst = SetsSearcher.get().first(nextTerminal);

                    relations.setEqualRel(currTerminal, nextTerminal);
                    relations.setMoreRel(curLast, nextTerminal);
                    relations.setLessRel(currTerminal, nextFirst);
                    relations.setMoreRel(curLast, nextFirst);
                }
            }
        }
    }
}
