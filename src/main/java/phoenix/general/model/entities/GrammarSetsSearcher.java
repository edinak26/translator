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

    public static Set<String> getAfterMinus(String term, String block) {
        return GrammarSetsSearcher.get().after(term, block);
    }

    public static Set<String> getBeforePlus(String term, String block) {
        return GrammarSetsSearcher.get().before(term, block);
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


    private Set<String> after(String term, String block) {
        afterFirst(term, block);
        afterEquals(term, block);
        return collected;
    }

    private Set<String> before(String term, String block) {
        beforeLast(term, block);
        beforeEquals(term, block);
        return collected;
    }

    public void afterFirst(String term, String block) {
        for (String terminal : getAfter(term, block)) {
            if (!grammar.isNonTerminal(terminal)) {
                collected.add(terminal);
            }
            collected.addAll(GrammarSetsSearcher.getFirstPlus(terminal));
        }
    }

    public void beforeLast(String term, String block) {
        for (String terminal : getBefore(term, block)) {
            collected.add(terminal);
            collected.addAll(GrammarSetsSearcher.getLastPlus(terminal));
        }
    }

    public void afterEquals(String term, String block) {
        if (!searched.contains(term)) {
            searched.add(term);
            for (NonTerminal nonTerminal : grammar.getNonTermsByEnd(term, block)) {
                after(nonTerminal.getName(), block);
            }
        }
    }

    public void beforeEquals(String term, String block) {
        if (!searched.contains(term)) {
            searched.add(term);
            for (NonTerminal nonTerminal : grammar.getNonTermsByStart(term, block)) {
                before(nonTerminal.getName(), block);
            }
        }
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

    public Set<String> getBefore(String term, String block) {
        Set<String> before = new HashSet<>();
        for (Map.Entry<NonTerminal, List<List<String>>> entry : grammar.entrySet()) {
            for (List<String> rightPart : entry.getValue()) {
                for (int i = 1; i < rightPart.size(); i++) {
                    boolean isCurLexeme = rightPart.get(i).equals(term);//TODO add not null global block
                    boolean isCurBlock = entry.getKey().getCurrBlock() != null && entry.getKey().getCurrBlock().equals(block);
                    if (isCurLexeme && isCurBlock) {
                        before.add(rightPart.get(i - 1));
                    }
                }
            }
        }
        return before;
    }


}

