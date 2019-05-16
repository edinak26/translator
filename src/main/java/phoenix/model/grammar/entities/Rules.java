package phoenix.model.grammar.entities;

import java.util.*;
import java.util.stream.Collectors;

public class Rules {
    private List<Rule> rules;

    public Rules() {
        this.rules = new ArrayList<>();
    }

    public Rules(List<Rule> rules) {
        this.rules = rules;
    }

    public void add(Rule rule) {
        rules.add(rule);
    }

    public void add(Rules newRules){
        this.rules.addAll(newRules.rules);
    }

    public List<Replace> getReplaces(NonTerminal nonTerminal) {
        for (Rule rule : rules) {
            if (rule.getNonTerminal().equals(nonTerminal))
                return rule.getReplaces();
        }
        return null;
    }


    public List<Rule> getRules() {
        return rules;
    }

    public Set<Terminal> getAfter(Terminal terminal){
        Set<Terminal> after = new HashSet<>();
        rules.forEach(rule -> after.addAll(rule.getAfter(terminal)));
        return after;
    }

    public Set<Terminal> getAfterMinus(Terminal terminal, Terminal before){
        Set<Terminal> after = new HashSet<>();
        rules.forEach(rule -> after.addAll(rule.getAfterMinus(terminal,before, this)));
        return after;
    }

    public Set<Terminal> getBefore(Terminal terminal){
        Set<Terminal> before = new HashSet<>();
        rules.forEach(rule -> before.addAll(rule.getBefore(terminal)));
        return before;
    }

    public List<NonTerminal> getNonTerminalsByFirst(Terminal terminal){
        List<NonTerminal> nonTerminals = new ArrayList<>();
        return rules.stream()
                .filter(rule->rule.hasFirst(terminal))
                .map(Rule::getNonTerminal)
                .collect(Collectors.toList());
    }

    public List<NonTerminal> getNonTerminalsByLast(Terminal terminal){
        return rules.stream()
        .filter(rule->rule.hasLast(terminal))
        .map(Rule::getNonTerminal)
                .collect(Collectors.toList());
    }

    public List<NonTerminal> getNonTerminalsByLastMinus(Terminal terminal, Terminal before){
        return rules.stream()
                .filter(rule->rule.hasLastMinus(terminal,before))
                .map(Rule::getNonTerminal)
                .collect(Collectors.toList());
    }

    public boolean hasTerminal(Terminal terminal) {
        return rules.stream().anyMatch(rule->rule.hasTerminal(terminal));
    }


    public void show() {
        rules.forEach(System.out::println);
    }

    public boolean contains(Terminal terminal) {
        return rules.stream().anyMatch(r->r.contains(terminal));
    }

    public void replace(Terminal terminal) {
        rules.forEach(r->{
            r.replace(terminal);
        });
    }

    public List<NonTerminal> getNonTerminals(Replace replace) {
        List<NonTerminal> result = new ArrayList<>();
        for(Rule rule: rules){
            if(rule.getReplaces().contains(replace)){
                result.add(rule.getNonTerminal());
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return rules.toString();
    }
}
