package phoenix.model.grammar.searcher.commands;

import phoenix.model.grammar.entities.NonTerminal;
import phoenix.model.grammar.entities.Rules;
import phoenix.model.grammar.entities.Terminal;
import phoenix.model.grammar.searcher.SetsSearcher;

import java.util.Set;

public class SearchAfterPlus extends SearchCommand {
    public SearchAfterPlus(Rules rules) {
        super(rules);
    }

    @Override
    public Set<Terminal> search(Terminal terminal) {
        return after(terminal);
    }


    private Set<Terminal> after(Terminal terminal) {
        if(rules.hasTerminal(terminal)){
            afterFirst(terminal);
            afterEquals(terminal);
        }
        return collected;
    }

    public void afterFirst(Terminal terminal) {
        for (Terminal afterTerminal : rules.getAfter(terminal)) {
            collected.add(afterTerminal);
            if (afterTerminal instanceof NonTerminal)
                collected.addAll(SetsSearcher.getLastPlus(rules,afterTerminal));
        }
    }

    public void afterEquals(Terminal terminal) {
        if (!searched.contains(terminal)) {
            searched.add(terminal);
            for (NonTerminal nonTerminal : rules.getNonTerminalsByLast(terminal)) {
                after(nonTerminal);
                if(nonTerminal.isAxiom())
                    collected.add(null);
            }
        }
    }
}
