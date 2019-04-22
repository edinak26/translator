package phoenix.general.model.syntax.analyzer;

import phoenix.general.interfaces.SyntaxConstants;
import phoenix.general.model.entities.Terminal;
import phoenix.general.model.lexical.analyzer.LexemesTableElement;

public class RelationTerminal implements SyntaxConstants {
    private Terminal terminal;
    private String lastRelation;

    public RelationTerminal(LexemesTableElement tableElement, String lastRelation){
        terminal=new Terminal(tableElement.lexeme());
        this.lastRelation=lastRelation;
    }

    public RelationTerminal(Terminal terminal, String lastRelation){
        this.terminal=terminal;
        checkRelation(lastRelation);
        this.lastRelation=lastRelation;
    }

    public RelationTerminal(){
        terminal=new Terminal(DEFAULT_TERMINAL);
        lastRelation = null;
    }

    public Terminal getTerminal(){
        return terminal;
    }

    public String getLastRelation(){
        return lastRelation;
    }

    private void checkRelation(String relation){
        if(relation==null)
            throw new RuntimeException("You cannot use undefined relation");
    }

    @Override
    public String toString() {
        return terminal.toString();
    }
}
