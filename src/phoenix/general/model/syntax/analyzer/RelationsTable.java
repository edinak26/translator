package phoenix.general.model.syntax.analyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RelationsTable {
    String[][] relations;
    List<List<String>> grammar;

    public RelationsTable(List<List<String>> grammar) {
        this.grammar = grammar;
        //relations = new ArrayList<>();
    }

    public HashSet<String> getFirstPlus(String term) {
        HashSet<String> firstPlus = new HashSet<>(getFirst(term));
        for (String ter : getFirst(term)) {
            if (!ter.equals(term))
                firstPlus.addAll(getFirstPlus(ter));
        }
        return firstPlus;
    }

    public HashSet<String> getLastPlus(String term) {
        HashSet<String> lastPlus = new HashSet<>(getLast(term));
        for (String ter : getLast(term)) {
            if (!ter.equals(term))
                lastPlus.addAll(getLastPlus(ter));
        }
        return lastPlus;
    }

    public HashSet<String> getFirst(String term) {
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

    public HashSet<String> getLast(String term) {
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
}
