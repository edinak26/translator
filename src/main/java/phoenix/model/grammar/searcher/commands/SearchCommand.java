package phoenix.model.grammar.searcher.commands;

import phoenix.model.grammar.entities.Rules;
import phoenix.model.grammar.entities.Terminal;

import java.util.HashSet;
import java.util.Set;

public abstract class SearchCommand {
    final Rules rules;

    Set<Terminal> collected;
    Set<Terminal> searched;
    Terminal before;

    SearchCommand (Rules rules){
        this.rules=rules;
        collected = new HashSet<>();
        searched = new HashSet<>();
    }

    public void setBefore(Terminal before){
        this.before=before;
    }


    public abstract Set<Terminal> search(Terminal terminal);


}
