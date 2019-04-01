package phoenix.general.model.syntax.analyzer;

import java.util.HashSet;
import java.util.List;

public class Searcher {
    private static List<List<String>> grammar;
    private HashSet<String> collected;

    public static Searcher get() {
        Searcher searcher = new Searcher();
        searcher.collected = new HashSet<>();
        return searcher;
    }

    public HashSet<String> first(String term){
        for(String ter:getFirst(term)){
            firstPlus(ter);
        }
        return collected;
    }
    public HashSet<String> last(String term){
        for(String ter:getLast(term)){
            lastPlus(ter);
        }
        return collected;
    }

    public void firstPlus(String term){
        if(!collected.contains(term)) {
            collected.add(term);
            for (String ter : getFirst(term)) {
                firstPlus(ter);
            }
        }
    }

    public void lastPlus(String term){
        if(!collected.contains(term)) {
            collected.add(term);
            for (String ter : getLast(term)) {
                lastPlus(ter);
            }
        }
    }

    private HashSet<String> getFirst(String term){
        HashSet<String> first = new HashSet<>();
        for (List<String> rule : grammar) {
            if (rule.get(0).equals(term)) {
                for (int i = 1; i < rule.size(); i++) {
                    if (i == 1 || rule.get(i - 1).equals("|")) {
                        first.add(rule.get(i));
                    }
                }
            }
        }
        return first;
    }

    private HashSet<String> getLast(String term){
        HashSet<String> last = new HashSet<>();
        for (List<String> rule : grammar) {
            if (rule.get(0).equals(term)) {
                for (int i = 1; i < rule.size(); i++) {
                    if (i == rule.size() - 1 || rule.get(i + 1).equals("|")) {
                        last.add(rule.get(i));
                    }
                }
            }
        }
        return last;
    }

    public static void setGrammar(List<List<String>> grammar) {
        Searcher.grammar = grammar;
    }
}
