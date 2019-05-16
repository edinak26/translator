package phoenix.model.grammar.searcher;

import phoenix.model.grammar.entities.*;
import phoenix.model.grammar.searcher.commands.*;

import java.util.*;

public class SetsSearcher {
    public static Set<Terminal> getFirstPlus(Rules rules, Terminal terminal){
        return (new SearchFirstPlus(rules)).search(terminal);
    }

    public static Set<Terminal> getLastPlus(Rules rules, Terminal terminal){
        return (new SearchLastPlus(rules)).search(terminal);
    }

    public static Set<Terminal> getAfterPlus(Rules rules, NonTerminal nonTerminal) {
        return (new SearchAfterPlus(rules)).search(nonTerminal);
    }

    public static Set<Terminal> getBeforePlus(Rules rules, NonTerminal nonTerminal) {
        return (new SearchBeforePlus(rules)).search(nonTerminal);
    }

    public static Set<Terminal> getAfterMinus(Terminal before, Rules rules, NonTerminal nonTerminal) {
        SearchCommand command = new SearchAfterMinus(rules);
        command.setBefore(before);
        return command.search(nonTerminal);
    }
}
