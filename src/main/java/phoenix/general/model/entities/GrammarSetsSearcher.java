package phoenix.general.model.entities;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrammarSetsSearcher {

    private static Grammar grammar;
    private Set<String> collected;
    private Set<String> searched;

    private GrammarSetsSearcher() {
        collected = new HashSet<>();
        searched = new HashSet<>();
    }

    public static void setGrammar(Grammar grammar) {
        GrammarSetsSearcher.grammar = grammar;
    }

    public static Set<String> getFirstPlus(String term) {
        return GrammarSetsSearcher.get().first(term);
    }

    public static Set<String> getLastPlus(String term) {
        return GrammarSetsSearcher.get().last(term);
    }

    public static Set<String> getAfterPlus(String term,String block){
        return GrammarSetsSearcher.get().after(term,block);
    }

    public static GrammarSetsSearcher get() {
        return new GrammarSetsSearcher();
    }

    public Set<String> first(String term) {
        for (String ter : getFirst(term)) {
            firstPlus(ter);
        }
        return collected;
    }

    public Set<String> last(String term) {
        for (String ter : getLast(term)) {
            lastPlus(ter);
        }
        return collected;
    }

    private Set<String> after(String term, String block) {
        afterFirst(term, block);
        afterEquals(term, block);
        return collected;
    }

    public void afterFirst(String term, String block) {
        if(!searched.contains(term)) {
            searched.add(term);
            collected.addAll(getAfter(term, block));
            for (String terminal : getAfter(term, block)) {
                collected.addAll(GrammarSetsSearcher.getFirstPlus(terminal));
            }
        }
    }

    public void afterEquals(String term, String block) {
        if(!searched.contains(term)) {
            searched.add(term);
            for (NonTerminal nonTerminal : getNonTermsWithTermEnd(term, block)) {
                afterFirst(nonTerminal.getName(), block);
                afterEquals(nonTerminal.getName(), block);
            }
        }
    }

    public Set<NonTerminal> getNonTermsWithTermEnd(String term, String block) {//TODO move it to Grammar class
        Set<NonTerminal> nonTerminals = new HashSet<>();
        for (Map.Entry<NonTerminal, List<List<String>>> entry : grammar.entrySet()) {
            for (List<String> rightPart : entry.getValue()) {
                boolean isTermEnd = term.equals(rightPart.get(rightPart.size() - 1));
                boolean isBlock = entry.getKey().getCurrBlock() != null && entry.getKey().getCurrBlock().equals(block);
                if (isTermEnd&&isBlock) {
                    nonTerminals.add(entry.getKey());
                }
            }
        }
        return nonTerminals;
    }

    public Set<String> getAfter(String term, String block) {
        Set<String> after = new HashSet<>();
        for (Map.Entry<NonTerminal, List<List<String>>> entry : grammar.entrySet()) {
            for (List<String> rightPart : entry.getValue()) {
                for (int i = 0; i < rightPart.size() - 1; i++) {
                    boolean isCurLexeme = rightPart.get(i).equals(term);//TODO add not null global block
                    boolean isCurBlock = entry.getKey().getCurrBlock() != null && entry.getKey().getCurrBlock().equals(block);
                    if (isCurLexeme && isCurBlock) {
                        after.add(rightPart.get(i + 1));
                    }
                }
            }
        }
        return after;
    }

    private void firstPlus(String term) {
        if (!collected.contains(term)) {
            collected.add(term);
            for (String ter : getFirst(term)) {
                firstPlus(ter);
            }
        }
    }

    private void lastPlus(String term) {
        if (!collected.contains(term)) {
            collected.add(term);
            for (String ter : getLast(term)) {
                lastPlus(ter);
            }
        }
    }

    private HashSet<String> getFirst(String term) {
        HashSet<String> first = new HashSet<>();
        List<List<String>> rightParts = grammar.getRightPart(term);
        if (rightParts != null)
            for (List<String> rightPart : rightParts) {
                first.add(rightPart.get(0));
            }
        return first;
    }

    private HashSet<String> getLast(String term) {
        HashSet<String> last = new HashSet<>();
        List<List<String>> rightParts = grammar.getRightPart(term);
        if (rightParts != null)
            for (List<String> rightPart : rightParts) {
                last.add(rightPart.get(rightPart.size() - 1));
            }
        return last;
    }

}

