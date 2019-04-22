package phoenix.general.model.grammar;

import phoenix.general.model.entities.NonTerminal;
import phoenix.general.model.entities.Terminal;
import phoenix.general.model.entities.VisibilityBlock;

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
        Set<Terminal> firstPlus;
        if (terminal instanceof NonTerminal)
            firstPlus = GrammarSetsSearcher.create().collectFirstPlus((NonTerminal) terminal).get();
        else
            firstPlus = new HashSet<>(Collections.singleton(terminal));
        firstPlus.remove(terminal);
        return firstPlus;
    }

    public static Set<Terminal> getLastPlus(Terminal terminal) {
        Set<Terminal> lastPlus;
        if (terminal instanceof NonTerminal)
            lastPlus = GrammarSetsSearcher.create().collectLastPlus((NonTerminal) terminal).get();
        else
            lastPlus = new HashSet<>(Collections.singleton(terminal));
        lastPlus.remove(terminal);
        return lastPlus;
    }

    public static Set<Terminal> getAfterMinus(NonTerminal nonTerminal, VisibilityBlock block) {
        System.out.println("A search: " + nonTerminal + " " + nonTerminal.isAxiom() + " |" + block);
        return GrammarSetsSearcher.create().after(nonTerminal, block);
    }

    public static Set<Terminal> getBeforePlus(NonTerminal nonTerminal, VisibilityBlock block) {
        System.out.println("B search: " + nonTerminal + " " + nonTerminal.isAxiom() + " |" + block);
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
        if(grammar.hasBlockTerminal(terminal,block)){
        afterFirst(terminal, block);
        afterEquals(terminal, block);
        return collected;
        }
        return null;
    }

    public void afterFirst(Terminal terminal, VisibilityBlock block) {
        for (Terminal afterTerminal : getAfter(terminal, block)) {
            collected.add(afterTerminal);
            if (afterTerminal instanceof NonTerminal)
                collected.addAll(GrammarSetsSearcher.getFirstPlus(afterTerminal));
        }
    }

    public Set<Terminal> getAfter(Terminal terminal, VisibilityBlock block) {
        return grammar.getOnlyBlockRules(block).entrySet().stream()
                .flatMap(e -> e.getValue().stream()
                        .filter(rightPart -> rightPart.contains(terminal))
                        .map(rightPart -> get(rightPart, rightPart.indexOf(terminal) + 1)))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public void afterEquals(Terminal terminal, VisibilityBlock block) {
        System.out.println(collected);
        if (!searched.contains(terminal)) {
            searched.add(terminal);
            for (NonTerminal nonTerminal : grammar.getNonTermsByEnd(terminal, block)) {
                after(nonTerminal, block);
                if(nonTerminal.isAxiom())
                    collected.add(null);
                /*if (nonTerminal.isAxiom()) {
                    for (VisibilityBlock uniqueBlock : grammar.getUniqueBlocks()) {
                        if (!uniqueBlock.equals(block));
                            //collected.addAll(GrammarSetsSearcher.getAfterMinus(nonTerminal, uniqueBlock));
                    }
                }*/
            }
        }
    }

    private Set<Terminal> before(Terminal terminal, VisibilityBlock block) {
        if(grammar.hasBlockTerminal(terminal,block)) {
        beforeLast(terminal, block);
        beforeEquals(terminal, block);
        return collected;
        }
        return null;
    }

    public void beforeLast(Terminal terminal, VisibilityBlock block) {
        for (Terminal beforeTerminal : getBefore(terminal, block)) {
            collected.add(beforeTerminal);
            if (beforeTerminal instanceof NonTerminal)
                collected.addAll(GrammarSetsSearcher.getLastPlus(beforeTerminal));

        }
    }

    public void beforeEquals(Terminal terminal, VisibilityBlock block) {
        if (!searched.contains(terminal)) {
            searched.add(terminal);
            for (NonTerminal nonTerminal : grammar.getNonTermsByStart(terminal, block)) {
                before(nonTerminal, block);
                if(nonTerminal.isAxiom())
                    collected.add(null);
                /*if (nonTerminal.isAxiom()) {
                    for (VisibilityBlock uniqueBlock : grammar.getUniqueBlocks()) {
                        if (!uniqueBlock.equals(block));
                            //collected.addAll(GrammarSetsSearcher.getBeforePlus(nonTerminal, uniqueBlock));
                    }
                }*/
            }
        }
    }

    public Set<Terminal> getBefore(Terminal terminal, VisibilityBlock block) {//TODO getBefore and gerAfter are so similar
        return grammar.getOnlyBlockRules(block).entrySet().stream()
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

