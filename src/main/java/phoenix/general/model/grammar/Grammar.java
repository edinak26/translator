package phoenix.general.model.grammar;

import phoenix.general.interfaces.MetaLanguage;
import phoenix.general.model.entities.NonTerminal;
import phoenix.general.model.entities.Terminal;
import phoenix.general.model.entities.VisibilityBlock;
import phoenix.general.model.syntax.analyzer.RelationsTable;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Grammar implements MetaLanguage {
    private Map<NonTerminal, List<List<Terminal>>> grammar;
    private NonTerminal grammarAxiom;

    public Grammar(Map<NonTerminal, List<List<Terminal>>> grammar) {
        this.grammar = grammar;
        GrammarSetsSearcher.setGrammar(this);
    }

    public void setAxiom(NonTerminal axiom) {
        this.grammarAxiom = axiom;
    }

    public boolean isAxiom(NonTerminal nonTerminal) {
        return nonTerminal.equals(grammarAxiom);
    }

    public Map<NonTerminal, List<List<Terminal>>> getBlockRules(VisibilityBlock block) {
        return grammar.entrySet().stream()
                .filter(rule -> rule.getKey().isInBlock(block))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    public Map<NonTerminal, List<List<Terminal>>> getOnlyBlockRules(VisibilityBlock block) {
        return grammar.entrySet().stream()
                .filter(rule -> rule.getKey().getBlock().equals(block))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    public Set<NonTerminal> getAllBlockNonTerminals(VisibilityBlock block) {
        return getBlockRules(block).keySet();
    }

    public List<NonTerminal> getBlockNonTerminals(List<Terminal> rightPart, VisibilityBlock block) {
        return getBlockRules(block).entrySet().stream()
                .filter(rule -> rule.getValue().stream()
                        .anyMatch(right -> equal(right,rightPart)))
                .map(Entry::getKey)
                .collect(Collectors.toList());
    }

    private boolean equal(List<Terminal> l1,List<Terminal> l2){
        if(l1.size()!=l2.size()){
            return false;
        }
        for(int i =0; i<l2.size();i++){
            if(!l1.get(i).equals(l2.get(i))){
                return false;
            }
        }
        return true;
    }

    public List<List<Terminal>> getRightParts(NonTerminal nonTerminal) {
        return grammar.entrySet().stream()
                .filter(e -> e.getKey().equals(nonTerminal))
                .map(Entry::getValue).findFirst()
                .orElse(null);
    }

    public NonTerminal getNonTerminal(String name) {
        return grammar.keySet().stream()
                .filter(ter -> ter.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<String> getUniqueTerminals() {
        Set<String> terminals = new LinkedHashSet<>();

        for (NonTerminal nonTerminal : grammar.keySet()) {
            terminals.add(nonTerminal.getName());
        }
        for (List<List<Terminal>> rightParts : grammar.values()) {
            for (List<Terminal> rightPart : rightParts) {
                for (Terminal terminal : rightPart) {
                    terminals.add(terminal.getName());
                }
            }
        }

        return new ArrayList<>(terminals);
    }


    public void configRelations(RelationsTable relations) {
        for (Entry<NonTerminal, List<List<Terminal>>> entry : grammar.entrySet()) {
            for (List<Terminal> rightPart : entry.getValue()) {
                for (int i = 0; i < rightPart.size() - 1; i++) {
                    Terminal currTerminal = rightPart.get(i);
                    Terminal nextTerminal = rightPart.get(i + 1);
                    Set<Terminal> curLast = GrammarSetsSearcher.getLastPlus(currTerminal);
                    Set<Terminal> nextFirst = GrammarSetsSearcher.getFirstPlus(nextTerminal);

                    relations.setEqualRel(currTerminal, nextTerminal);
                    relations.setMoreRel(curLast, nextTerminal);
                    relations.setLessRel(currTerminal, nextFirst);
                    relations.setMoreRel(curLast, nextFirst);
                }
            }
        }
    }

    public Set<NonTerminal> getAllNonTerminals() {
        return grammar.keySet();
    }

    public VisibilityBlock getStartVisibilityBlocks() {
        return grammarAxiom.getBlock();
    }

    public Set<Entry<NonTerminal, List<List<Terminal>>>> entrySet() {
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

    public Set<NonTerminal> getNonTermsByEnd(Terminal terminal, VisibilityBlock block) {
        return getBlockRules(block).entrySet().stream()
                .filter(e->e.getValue().stream()
                        .anyMatch(rightPart->rightPart.get(rightPart.size()-1).equals(terminal)))
                .map(Entry::getKey)
                .collect(Collectors.toSet());
    }

    public Set<NonTerminal> getNonTermsByStart(Terminal terminal, VisibilityBlock block) {
        return getBlockRules(block).entrySet().stream()
                .filter(e->e.getValue().stream()
                        .anyMatch(rightPart->rightPart.get(0).equals(terminal)))
                .map(Entry::getKey)
                .collect(Collectors.toSet());
    }

    public Set<VisibilityBlock> getUniqueBlocks() {
        return grammar.keySet().stream()
                .map(NonTerminal::getBlock)
                .collect(Collectors.toSet());
    }

    public void show() {
        grammar.forEach((key, value) -> {
            System.out.print(key.getName() + "::=");
            value.forEach(rp -> System.out.print(rp.toString()));
            System.out.println();
        });
    }

    public boolean hasBlockTerminal(Terminal terminal, VisibilityBlock block){
        return getBlockRules(block).values().stream()
                .anyMatch(rightParts->rightParts.stream()
                        .anyMatch(rightPart->rightPart.contains(terminal)));
    }
}
