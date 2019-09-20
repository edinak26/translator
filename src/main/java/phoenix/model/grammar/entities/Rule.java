package phoenix.model.grammar.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static phoenix.interfaces.Characters.GRAMMAR_EQUALITY;
import static phoenix.interfaces.MetaLanguage.GRAMMAR_OR;

public class Rule {
    private NonTerminal nonTerminal;
    private List<Replace> replaces;

    public Rule(NonTerminal nonTerminal, List<Replace> replaces) {
        this.nonTerminal = nonTerminal;
        this.replaces = replaces;
    }

    public NonTerminal getNonTerminal() {
        return nonTerminal;
    }

    public List<Replace> getReplaces(){
        return replaces;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(nonTerminal.toString())
                .append(GRAMMAR_EQUALITY);
        replaces.forEach(r ->
                str.append(r).append(GRAMMAR_OR));
        str.deleteCharAt(str.length()-1);
        return str.toString();
    }

    public Set<Terminal> getAfter(Terminal terminal){
        Set<Terminal> after = new HashSet<>();
        replaces.forEach(replace -> after.add(replace.getAfter(terminal)));
        after.remove(null);
        return after;
    }

    /*public Set<Terminal> getAfterMinus(Terminal terminal, Terminal before, Rules rules){
        Set<Terminal> after = new HashSet<>();
        replaces.forEach(replace ->after.add(replace.getAfterMinus(terminal,before, rules)));
        after.remove(null);
        return after;
    }*/

    public List<WideTerminal> getWideTerminals(Terminal terminal){
        List<WideTerminal> result = new ArrayList<>();
        for(Replace replace:replaces){
            List<WideTerminal> replaceTerminals = replace.getWideTerminals(terminal);
            replaceTerminals.forEach(t->t.setNonTerminal(nonTerminal));
            result.addAll(replaceTerminals);
        }

        return result;
    }

    public Set<Terminal> getBefore(Terminal terminal){
        Set<Terminal> before = new HashSet<>();
        replaces.forEach(replace -> before.add(replace.getBefore(terminal)));
        before.remove(null);
        return before;
    }

    public boolean hasFirst(Terminal terminal){
        for(Replace replace:replaces){
            if(replace.isFirst(terminal))
                return true;
        }
        return false;
    }

    public boolean hasLast(Terminal terminal){
        for(Replace replace:replaces){
            if(replace.isLast(terminal))
                return true;
        }
        return false;
    }

    public boolean hasLastMinus(Terminal terminal,Terminal before) {
        for(Replace replace:replaces){
            if(replace.isLastMinus(terminal,before))
                return true;
        }
        return false;
    }

    public boolean hasTerminal(Terminal terminal) {
        return replaces.stream().anyMatch(replace -> replace.has(terminal));
    }

    public boolean contains(Terminal terminal) {
        return replaces.stream().anyMatch(r->r.contains(terminal));
    }

    public void replace(Terminal terminal) {
        replaces.forEach(replace -> {
            replace.replace(terminal);
        });
    }


}
