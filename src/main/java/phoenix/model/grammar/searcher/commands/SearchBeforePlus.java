package phoenix.model.grammar.searcher.commands;

import phoenix.model.grammar.entities.NonTerminal;
import phoenix.model.grammar.entities.Rules;
import phoenix.model.grammar.entities.Terminal;
import phoenix.model.grammar.searcher.SetsSearcher;

import java.util.Set;

public class SearchBeforePlus extends  SearchCommand{
    public SearchBeforePlus(Rules rules) { super(rules); }

    @Override
    public Set<Terminal> search(Terminal terminal) {
        return before(terminal);
    }

    private Set<Terminal> before(Terminal terminal) {
        if(rules.hasTerminal(terminal)){
            beforeLast(terminal);
            beforeEquals(terminal);
        }
        return collected;
    }

    public void beforeLast(Terminal terminal) {
        for (Terminal beforeTerminal : rules.getBefore(terminal)) {
            collected.add(beforeTerminal);
            if (beforeTerminal instanceof NonTerminal)
                collected.addAll(SetsSearcher.getLastPlus(rules,beforeTerminal));
        }
    }

    public void beforeEquals(Terminal terminal) {
        if (!searched.contains(terminal)) {
            searched.add(terminal);
            for (NonTerminal nonTerminal : rules.getNonTerminalsByFirst(terminal)) {
                before(nonTerminal);
                if(nonTerminal.isAxiom())
                    collected.add(null);
            }
        }
    }
}
