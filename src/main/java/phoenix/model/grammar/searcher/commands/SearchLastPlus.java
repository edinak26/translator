package phoenix.model.grammar.searcher.commands;

import phoenix.model.grammar.entities.NonTerminal;
import phoenix.model.grammar.entities.Replace;
import phoenix.model.grammar.entities.Rules;
import phoenix.model.grammar.entities.Terminal;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchLastPlus extends SearchCommand{

    public SearchLastPlus(Rules rules){
        super(rules);

    }

    @Override
    public Set<Terminal> search(Terminal terminal) {
        if (!(terminal instanceof NonTerminal))
            return new HashSet<>();
        collectLastPlus((NonTerminal) terminal);
        collected.remove(terminal);
        return collected;
    }

    public void collectLastPlus(NonTerminal nonTerminal) {
        if (!collected.contains(nonTerminal)) {
            collected.add(nonTerminal);
            lastPlus(nonTerminal);
        }
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
        return rules.getReplaces(nonTerminal).stream()
                .filter(Objects::nonNull)
                .map(Replace::getLast)
                .collect(Collectors.toSet());
    }

}
