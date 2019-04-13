package phoenix.general.model.entities;

import java.util.*;
import java.util.stream.Collectors;

public class GrammarSetsSearcher {

    private static Grammar grammar;
    private Set<Terminal> collected;
    private Set<Terminal> searched;

    private GrammarSetsSearcher() {
        collected = new HashSet<>();
        searched = new HashSet<>();
    }

    public static void setGrammar(Grammar grammar) {
        GrammarSetsSearcher.grammar = grammar;
    }

    public static Set<Terminal> getFirstPlus(Terminal terminal) {
        if (terminal instanceof NonTerminal)
            return GrammarSetsSearcher.create().collectFirstPlus((NonTerminal) terminal).get();
        else
            return new HashSet<>(Collections.singleton(terminal));
    }

    public static Set<Terminal> getLastPlus(Terminal terminal) {
        if (terminal instanceof NonTerminal)
            return GrammarSetsSearcher.create().collectLastPlus((NonTerminal) terminal).get();
        else
            return new HashSet<>(Collections.singleton(terminal));
    }

    public static Set<Terminal> getAfterMinus(NonTerminal nonTerminal, VisibilityBlock block) {
        return GrammarSetsSearcher.create().after(nonTerminal, block);
    }

    public static Set<Terminal> getBeforePlus(NonTerminal nonTerminal, VisibilityBlock block) {
        return GrammarSetsSearcher.create().before(nonTerminal, block);
    }

    public static GrammarSetsSearcher create() {
        return new GrammarSetsSearcher();
    }

    public Set<Terminal> get() {
        return collected;
    }

    public GrammarSetsSearcher collectFirstPlus(NonTerminal nonTerminal) {
        if (!collected.contains(nonTerminal)) {
            collected.add(nonTerminal);
            firstPlus(nonTerminal);
        }
        return this;
    }

    private void firstPlus(NonTerminal nonTerminal) {
        for (Terminal firstTerminal : getFirst(nonTerminal)) {
            if (firstTerminal instanceof NonTerminal)
                collectFirstPlus((NonTerminal) firstTerminal);
            else
                collected.add(firstTerminal);
        }
    }

    private Set<Terminal> getFirst(NonTerminal nonTerminal) {
        return grammar.getRightParts(nonTerminal).stream()
                .filter(Objects::nonNull)
                .map(rightPart -> rightPart.get(0))
                .collect(Collectors.toSet());
    }

    public GrammarSetsSearcher collectLastPlus(NonTerminal nonTerminal) {
        if (!collected.contains(nonTerminal)) {
            collected.add(nonTerminal);
            lastPlus(nonTerminal);
        }
        return this;
    }

    private void lastPlus(NonTerminal nonTerminal) {
        for (Terminal lastTerminal : getLast(nonTerminal)) {
            if (lastTerminal instanceof NonTerminal)
                collectLastPlus((NonTerminal) lastTerminal);
            else
                collected.add(lastTerminal);
        }

    }

    private Set<Terminal> getLast(NonTerminal nonTerminal) {
        return grammar.getRightParts(nonTerminal).stream()
                .filter(Objects::nonNull)
                .map(rightPart -> rightPart.get(rightPart.size() - 1))
                .collect(Collectors.toSet());
    }


    private Set<Terminal> after(Terminal terminal, VisibilityBlock block) {
        afterFirst(terminal, block);
        afterEquals(terminal, block);
        return collected;
    }

    public void afterFirst(Terminal terminal, VisibilityBlock block) {
        for (Terminal afterTerminal : getAfter(terminal, block)) {
            if (afterTerminal instanceof NonTerminal)
                collected.addAll(GrammarSetsSearcher.getFirstPlus((NonTerminal) afterTerminal));
            else
                collected.add(afterTerminal);
        }
    }

    public void afterEquals(Terminal terminal, VisibilityBlock block) {
        if (!searched.contains(terminal)) {
            searched.add(terminal);
            for (NonTerminal nonTerminal : grammar.getNonTermsByEnd(terminal, block)) {
                after(terminal, block);
                if (nonTerminal.isAxiom()) {
                    for (NonTerminal nonTer : grammar.getAllBlockNonTerminals(block)) {
                        after(nonTer, nonTer.getBlock());
                    }
                }
            }
        }
    }

    public Set<Terminal> getAfter(Terminal terminal, VisibilityBlock block) {
        return grammar.getBlockRules(block).entrySet().stream()
                .flatMap(e -> e.getValue().stream()
                        .filter(rightPart -> rightPart.contains(terminal))
                        .map(rightPart -> get(rightPart, rightPart.indexOf(terminal) + 1)))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<Terminal> before(Terminal terminal, VisibilityBlock block) {
        beforeLast(terminal, block);
        beforeEquals(terminal, block);
        return collected;
    }

    public void beforeLast(Terminal terminal, VisibilityBlock block) {
        for (Terminal beforeTerminal : getBefore(terminal, block)) {
            if (terminal instanceof NonTerminal)
                collected.addAll(GrammarSetsSearcher.getLastPlus((NonTerminal) beforeTerminal));
            else
                collected.add(beforeTerminal);
        }
    }

    public void beforeEquals(Terminal terminal, VisibilityBlock block) {
        if (!searched.contains(terminal)) {
            searched.add(terminal);
            for (NonTerminal nonTerminal : grammar.getNonTermsByStart(terminal, block)) {
                before(nonTerminal, block);
                if (nonTerminal.isAxiom()) {
                    for (NonTerminal nonTer : grammar.getAllBlockNonTerminals(block)) {
                        before(nonTer, nonTer.getBlock());
                    }
                }
            }
        }
    }

    public Set<Terminal> getBefore(Terminal terminal, VisibilityBlock block) {//TODO getBefore and gerAfter are so similar
        return grammar.getBlockRules(block).entrySet().stream()
                .flatMap(e -> e.getValue().stream()
                        .filter(rightPart -> rightPart.contains(terminal))
                        .map(rightPart -> get(rightPart, rightPart.indexOf(terminal) - 1)))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Terminal get(List<Terminal> terminals, int index) {
        if (index < 0 || index >= terminals.size()) {
            return null;
        } else
            return terminals.get(index);
    }
}

