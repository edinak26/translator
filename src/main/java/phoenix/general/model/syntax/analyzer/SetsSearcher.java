package phoenix.general.model.syntax.analyzer;

import phoenix.general.model.entities.Grammar;

import java.util.HashSet;
import java.util.List;

public class SetsSearcher {
    private static Grammar grammar;
    private HashSet<String> collected;

    private SetsSearcher() {
        collected = new HashSet<>();
    }

    public static void setGrammar(Grammar grammar) {
        SetsSearcher.grammar = grammar;
    }

    public static SetsSearcher get() {
        return new SetsSearcher();
    }

    public HashSet<String> first(String term) {
        for (String ter : getFirst(term)) {
            firstPlus(ter);
        }
        return collected;
    }

    private void firstPlus(String term) {
        if (!collected.contains(term)) {
            collected.add(term);
            for (String ter : getFirst(term)) {
                firstPlus(ter);
            }
        }
    }

    private HashSet<String> getFirst(String term) {
        HashSet<String> first = new HashSet<>();
        List<List<String>> rightParts = grammar.getRightPart(term);
        for(List<String> rightPart: rightParts){
            first.add(rightPart.get(0));
        }
        return first;
    }

    public HashSet<String> last(String term) {
        for (String ter : getLast(term)) {
            lastPlus(ter);
        }
        return collected;
    }

    private void lastPlus(String term){
        if(!collected.contains(term)) {
            collected.add(term);
            for (String ter : getLast(term)) {
                lastPlus(ter);
            }
        }
    }

    private HashSet<String> getLast(String term){
        HashSet<String> last = new HashSet<>();
        List<List<String>> rightParts = grammar.getRightPart(term);
        for(List<String> rightPart: rightParts){
            last.add(rightPart.get(rightPart.size()-1));
        }
        return last;
    }


}
