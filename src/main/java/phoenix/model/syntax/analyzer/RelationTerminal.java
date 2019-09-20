package phoenix.model.syntax.analyzer;

import phoenix.interfaces.SyntaxConstants;
import phoenix.model.grammar.entities.Terminal;
import phoenix.model.lexical.analyzer.LexemesTableElement;
import phoenix.model.syntax.analyzer.relations.Relation;

import java.util.ArrayList;
import java.util.List;

import static phoenix.interfaces.Characters.END_TERMINAL;


public class RelationTerminal implements SyntaxConstants {
    private Terminal terminal;
    private Relation lastRelation;
    private List<RelationTerminal> value;

    public RelationTerminal(LexemesTableElement tableElement, Relation lastRelation){
        terminal=new Terminal(tableElement.lexeme());
        this.lastRelation=lastRelation;
    }

    public RelationTerminal(Terminal terminal, Relation lastRelation){
        this.terminal=terminal;
        checkRelation(lastRelation);
        this.lastRelation=lastRelation;
    }

    public RelationTerminal(Terminal terminal){
        this.terminal=terminal;
    }

    public RelationTerminal(){
        terminal=new Terminal(END_TERMINAL);
        lastRelation = null;
    }

    public RelationTerminal(Terminal replaceTerminal, List<RelationTerminal> replaceValue) {
        this.terminal=replaceTerminal;
        this.value=replaceValue;
    }

    public Terminal getTerminal(){
        return terminal;
    }

    public Relation getLastRelation(){
        return lastRelation;
    }

    private void checkRelation(Relation relation){
        if(relation==Relation.UNDEFINED)
            throw new RuntimeException("You cannot use undefined relation");
    }

    @Override
    public String toString() {
        return terminal.toString();
    }

    public void setValue(List<RelationTerminal> value) {
        this.value = value;
    }

    public List<RelationTerminal> getValue() {
        return value;
    }

    public List<String> getShow(){
        List<String> result = new ArrayList<>();
        for(RelationTerminal terminal: value){
            if(terminal.value!=null){
                result.addAll(terminal.getShow());
            } else {
                result.add(terminal.getTerminal().getName());
            }
        }
        return result;
    }
}
