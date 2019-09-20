package phoenix.model.grammar.searcher.commands;

import phoenix.model.grammar.entities.NonTerminal;
import phoenix.model.grammar.entities.Replace;
import phoenix.model.grammar.entities.Rules;
import phoenix.model.grammar.entities.Terminal;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchFirstPlus extends SearchCommand{

    public SearchFirstPlus(Rules rules){
        super(rules);

    }

    @Override
    public Set<Terminal> search(Terminal terminal) {
        //System.out.println(terminal+"|"+(terminal instanceof NonTerminal));
        if (!(terminal instanceof NonTerminal))
            return new HashSet<>();
        collectFirstPlus((NonTerminal) terminal);
        collected.remove(terminal);
        return collected;
    }


    public void collectFirstPlus(NonTerminal nonTerminal) {
        if (!collected.contains(nonTerminal)) {
            collected.add(nonTerminal);
            firstPlus(nonTerminal);
        }
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
        if(rules.getReplaces(nonTerminal)==null){
            return new HashSet<>();
        }
        return rules.getReplaces(nonTerminal).stream()
                .filter(Objects::nonNull)
                .map(Replace::getFirst)
                .collect(Collectors.toSet());
    }


}
