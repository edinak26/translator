package phoenix.model.grammar.searcher.commands;

import phoenix.model.grammar.entities.NonTerminal;
import phoenix.model.grammar.entities.Rules;
import phoenix.model.grammar.entities.Terminal;
import phoenix.model.grammar.entities.WideTerminal;
import phoenix.model.grammar.searcher.SetsSearcher;

import java.util.Set;

public class SearchAfterMinus extends SearchCommand {
    public SearchAfterMinus(Rules rules) {
        super(rules);
    }

    @Override
    public Set<Terminal> search(Terminal terminal) {
        return after(terminal);
    }


    private Set<Terminal> after(Terminal terminal) {
        if (rules.hasTerminal(terminal)) {
            afterFirst(terminal);
            afterEquals(terminal);
            if(collected.isEmpty()){
                collected.add(null);
            }
        }
        return collected;
    }

    private void afterFirst (Terminal terminal) {
        for (WideTerminal wideTerminal : rules.getWideTerminals(terminal)) {
            Terminal before = wideTerminal.getBefore();
            if(before==null){

            }else if(wideTerminal.getBefore().equals(before))


            if(terminal.isAxiom()){
                collected.add(wideTerminal.getAfter());
            }
            else if(wideTerminal.getBefore().equals(before)){
                if(wideTerminal.getAfter()==null){

                }

                //collected.add(wideTerminal.getAfter());
            }
        }
    }

    private void afterEquals(Terminal terminal) {
        if (!searched.contains(terminal)) {
            searched.add(terminal);
            for (NonTerminal nonTerminal : rules.getNonTerminalsByLastMinus(terminal,before)) {
                after(nonTerminal);
                if (nonTerminal.isAxiom())
                    collected.add(null);
            }
        }
    }
}
