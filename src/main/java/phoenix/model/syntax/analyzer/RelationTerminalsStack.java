package phoenix.model.syntax.analyzer;

import phoenix.model.grammar.entities.Terminal;
import phoenix.model.syntax.analyzer.relations.Relation;
import phoenix.model.syntax.analyzer.relations.RelationsTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class RelationTerminalsStack {
    private Stack<RelationTerminal> terminals;

    public List<RelationTerminal> getLastRelationRightPart() {
        return lastRightPart;
    }

    private List<RelationTerminal> lastRightPart;


    public RelationTerminalsStack(RelationsTable relationsTable) {
        terminals = new Stack<>();
        terminals.add(new RelationTerminal());
    }

    public Terminal peek() {
        return terminals.peek().getTerminal();
    }

    public RelationTerminal peekRelationTerminal(){ return terminals.peek();}
    public void push(Terminal terminal, Relation relation) {
        terminals.push(new RelationTerminal(terminal, relation));
    }

    public List<Terminal> popLastRightPart() {
        List<Terminal> rightPart = new ArrayList<>();
        List<RelationTerminal> relationRightPart = new ArrayList<>();
        while(!terminals.isEmpty()){
            RelationTerminal relationTerminal = terminals.pop();
            rightPart.add(relationTerminal.getTerminal());
            relationRightPart.add(relationTerminal);
            if(relationTerminal.getLastRelation()==Relation.LESS){
                Collections.reverse(rightPart);
                Collections.reverse(relationRightPart);
                lastRightPart = relationRightPart;
                return rightPart;
            }
        }
        throw new RuntimeException("Relation lexeme exception: Uncorrect grammar");
    }

    @Override
    public String toString() {
        return terminals.toString();
    }

    public void setLastValue(List<RelationTerminal> polishNotationLine) {
        terminals.peek().setValue(polishNotationLine);
    }

    public int size() {
        return terminals.size();
    }

    public void setIdentifiers(List<String> identifiers) {
    }
}
