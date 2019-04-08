package phoenix.general.model.syntax.analyzer;

import phoenix.general.interfaces.SyntaxConstants;
import phoenix.general.model.lexical.analyzer.LexemesTableElement;

public class LexemeRelation implements SyntaxConstants {
    private String name;
    private String lastRelation;

    public LexemeRelation(LexemesTableElement tableElement, String lastRelation){
        name=tableElement.lexeme();
        this.lastRelation=lastRelation;
    }

    public LexemeRelation(String name, String lastRelation){
        this.name=name;
        checkRelation(lastRelation);
        this.lastRelation=lastRelation;
    }

    public LexemeRelation(){
        name=DEFAULT_RELATION_LEXEME;
        lastRelation = null;
    }
    public String getName(){
        return name;
    }

    public String getLastRelation(){
        return lastRelation;
    }

    private void checkRelation(String relation){
        if(relation==null)
            throw new RuntimeException("You cannot use undefined relation");
    }

}
