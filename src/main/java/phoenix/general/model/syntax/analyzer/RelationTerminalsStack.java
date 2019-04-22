package phoenix.general.model.syntax.analyzer;

import phoenix.accessory.constant.Characters;
import phoenix.general.model.entities.Terminal;
import phoenix.general.model.lexical.analyzer.LexemesTableElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class RelationTerminalsStack implements Characters {
    private Stack<RelationTerminal> terminals;

    public RelationTerminalsStack() {
        terminals = new Stack<>();
        terminals.add(new RelationTerminal());
    }

    public Terminal peek() {
        return terminals.peek().getTerminal();
    }

    public void push(Terminal terminal, String relation) {
        terminals.push(new RelationTerminal(terminal, relation));
    }

    public List<Terminal> popLastRightPart() {
        List<Terminal> rightPart = new ArrayList<>();
        while(!terminals.isEmpty()){
            RelationTerminal relationTerminal = terminals.pop();
            rightPart.add(relationTerminal.getTerminal());
            if(relationTerminal.getLastRelation().equals(RELATION_LESS)){
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
