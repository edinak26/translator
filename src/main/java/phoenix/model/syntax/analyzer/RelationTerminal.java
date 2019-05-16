package phoenix.model.syntax.analyzer;

import phoenix.interfaces.SyntaxConstants;
import phoenix.model.grammar.entities.Terminal;
import phoenix.model.lexical.analyzer.LexemesTableElement;
import phoenix.model.syntax.analyzer.relations.Relation;

import static phoenix.interfaces.Characters.END_TERMINAL;


public class RelationTerminal implements SyntaxConstants {
    private Terminal terminal;
    private Relation lastRelation;

    public RelationTerminal(LexemesTableElement tableElement, Relation lastRelation){
        terminal=new Terminal(tableElement.lexeme());
        this.lastRelation=lastRelation;
    }

    public RelationTerminal(Terminal terminal, Relation lastRelation){
        this.terminal=terminal;
        checkRelation(lastRelation);
        this.lastRelation=lastRelation;
    }

    public RelationTerminal(){
        terminal=new Terminal(END_TERMINAL);
        lastRelation = null;
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
}
