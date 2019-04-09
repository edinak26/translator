package phoenix.general.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Axiom extends NonTerminal{
    private Map<String,List<String>[]> blocksSets;
    public Axiom(String name) {
        super(name);
    }

    public Axiom(NonTerminal nonTerminal){
        super(nonTerminal.getName(),nonTerminal.getCurrBlock());
    }

    public void addSets(String block, Set<String> after,Set<String> before){
        List[] sets = new List[2];
        sets[0]=new ArrayList<>(after);
        sets[1]=new ArrayList<>(before);
        blocksSets.put(block,sets);
    }

}
