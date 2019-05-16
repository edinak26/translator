package phoenix.model.syntax.analyzer;

import phoenix.model.grammar.entities.Terminal;
import phoenix.model.syntax.analyzer.relations.Relation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class RelationTerminalsStack {
    private Stack<RelationTerminal> terminals;

    public RelationTerminalsStack() {
        terminals = new Stack<>();
        terminals.add(new RelationTerminal());
    }

    public Terminal peek() {
        return terminals.peek().getTerminal();
    }

    public void push(Terminal terminal, Relation relation) {
        terminals.push(new RelationTerminal(terminal, relation));
    }

    public List<Terminal> popLastRightPart() {
        List<Terminal> rightPart = new ArrayList<>();
        while(!terminals.isEmpty()){
            RelationTerminal relationTerminal = terminals.pop();
            rightPart.add(relationTerminal.getTerminal());
            if(relationTerminal.getLastRelation()==Relation.LESS){
                Collections.reverse(rightPart);
                return rightPart;
            }
        }
        throw new RuntimeException("Relation lexeme exception: Uncorrect grammar");
    }

    @Override
    public String toString() {
        return terminals.toString();
    }
}
