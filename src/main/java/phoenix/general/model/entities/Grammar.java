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

    public NonTerminal getBlockNonTerminal(List<String> rightPart, String currVisibilityBlock) {
        NonTerminal result = null;
        for (Entry<NonTerminal, List<List<String>>> grammarEntry : grammar.entrySet()) {
            for (List<String> grammarRightPart : grammarEntry.getValue()) {
                boolean hasVisibilityBlock = grammarEntry.getKey().getCurrBlock() != null;
                if (hasVisibilityBlock) {
                    boolean isCurrVisibilityBlock = grammarEntry.getKey().getCurrBlock().equals(currVisibilityBlock);
                    boolean isNonTerminalFound = rightPart.equals(grammarRightPart);
                    if (isCurrVisibilityBlock && isNonTerminalFound) {
                        checkUniqueness(result, rightPart);
                        result = grammarEntry.getKey();
                    }
                }
            }
        }
        return result;
    }

    public NonTerminal getGlobalNonTerminal(List<String> rightPart) {
        NonTerminal result = null;
        for (Entry<NonTerminal, List<List<String>>> grammarEntry : grammar.entrySet()) {
            for (List<String> grammarRightPart : grammarEntry.getValue()) {
                if (rightPart.equals(grammarRightPart)) {
                    checkUniqueness(result, rightPart);
                    result = grammarEntry.getKey();
                }
            }
        }
        checkResult(result, rightPart);
        return result;
    }

    private void checkUniqueness(NonTerminal result, List<String> rightPart) {
        if (result != null)
            throw new RuntimeException(
                    "Un correct grammar, right part:"
                            + rightPart.toString()
                            + " has more then one replace"
                            + "\n second non terminal is:" + result.getName());
    }

    private void checkResult(NonTerminal result, List<String> rightPart) {
        if (result == null)
            throw new RuntimeException("Non terminal is not found for:" + rightPart.toString());
    }

    public NonTerminal getNonTerminal(List<String> rightPart, String currVisibilityBlock) {
        NonTerminal res = null;
        int counter = 0;
        for (Entry<NonTerminal, List<List<String>>> grammarEntry : grammar.entrySet()) {
            for (List<String> grammarRightPart : grammarEntry.getValue()) {
                if (rightPart.equals(grammarRightPart)) {
                    counter++;
                    res = grammarEntry.getKey();
                }
            }
        }

        if (counter > 1) {
            counter = 0;
            for (Entry<NonTerminal, List<List<String>>> grammarEntry : grammar.entrySet()) {
                for (List<String> grammarRightPart : grammarEntry.getValue()) {
                    if (rightPart.equals(grammarRightPart) && grammarEntry.getKey().getCurrBlock().equals(currVisibilityBlock)) {
                        counter++;
                        res = grammarEntry.getKey();
                    }
                }
            }
            if (counter > 1)
                throw new RuntimeException("Un correct grammar, right part:" + rightPart.toString() + " has more then one replace");
        }
        return res;
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

        for (NonTerminal nonTerminal : grammar.keySet()) {
            terminals.add(nonTerminal.getName());
        }

        for (List<List<String>> rightParts : grammar.values()) {
            for (List<String> rightPart : rightParts) {
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
                    String nextTerminal = rightPart.get(i + 1);
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

    public void show() {
        for (Entry<NonTerminal, List<List<String>>> entry : grammar.entrySet()) {
            System.out.print(entry.getKey().getName() + GRAMMAR_EQUAL);
            for (List<String> rightPart : entry.getValue()) {
                for (String elem : rightPart) {
                    System.out.print(elem);
                }
                System.out.print(GRAMMAR_OR);
            }
            System.out.println();
        }
    }

    public String getStartVisibilityBlocks() {
        for (NonTerminal nonTerminal : grammar.keySet()) {
            if (nonTerminal.getCurrBlock() != null) {
                return nonTerminal.getCurrBlock();
            }
        }
        return null;
    }

    public Iterable<? extends Entry<NonTerminal, List<List<String>>>> entrySet() {
        return grammar.entrySet();
    }

    public boolean isNonTerminal(String terminal) {
        for (NonTerminal nonTerminal : grammar.keySet()) {
            if (nonTerminal.getName().equals(terminal)) {
                return true;
            }
        }
        return false;
    }

    public Set<NonTerminal> getNonTermsByEnd(String term, String block) {
        Set<NonTerminal> nonTerminals = new HashSet<>();
        for (Map.Entry<NonTerminal, List<List<String>>> entry : grammar.entrySet()) {
            for (List<String> rightPart : entry.getValue()) {
                boolean isTermEnd = term.equals(rightPart.get(rightPart.size() - 1));
                boolean isBlock = entry.getKey().getCurrBlock() != null && entry.getKey().getCurrBlock().equals(block);
                if (isTermEnd && isBlock) {
                    nonTerminals.add(entry.getKey());
                }
            }
        }
        return nonTerminals;
    }

    public Set<NonTerminal> getNonTermsByStart(String term, String block) {
        Set<NonTerminal> nonTerminals = new HashSet<>();
        for (Map.Entry<NonTerminal, List<List<String>>> entry : grammar.entrySet()) {
            for (List<String> rightPart : entry.getValue()) {
                boolean isTermStart = term.equals(rightPart.get(0));
                boolean isBlock = entry.getKey().getCurrBlock() != null && entry.getKey().getCurrBlock().equals(block);
                if (isTermStart && isBlock) {
                    nonTerminals.add(entry.getKey());
                }
            }
        }
        return nonTerminals;
    }
}
