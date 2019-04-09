package phoenix.general.model.syntax.analyzer;

import phoenix.accessory.constant.Characters;
import phoenix.general.model.lexical.analyzer.LexemesTableElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class LexemesStack implements Characters {
    private List<LexemeRelation> lexemes;

    LexemesStack() {
        lexemes = new Stack<>();
        lexemes.add(new LexemeRelation());
    }

    public String getLastLexemeName() {
        return lexemes.get(lastIndex()).getName();
    }

    public List<String> getLexemeNames() {
        return createNamesList();
    }

    private List<String> createNamesList() {
        List<String> names = new ArrayList<>();
        for (LexemeRelation lexeme : lexemes) {
            names.add(lexeme.getName());
        }
        return names;
    }

    public void push(LexemesTableElement lexeme, String relation) {
        lexemes.add(new LexemeRelation(lexeme, relation));
    }

    public void push(String lexeme, String relation) {
        lexemes.add(new LexemeRelation(lexeme, relation));
    }

    public String pop(LexemesTableElement lexeme) {
        return lexemes.remove(lastIndex()).getName();
    }

    private int lastIndex() {
        return lexemes.size() - 1;
    }

    public List<String> popLastRightPart() {
        reverseLexemes();
        List<String> rightPart = createLastRightPart();
        reverseLexemes();
        return rightPart;
    }

    private List<String> createLastRightPart() {

        List<String> rightPart = new ArrayList<>();
        for (LexemeRelation lexeme : this.lexemes) {
            rightPart.add(lexeme.getName());
            lexemes.remove(0);
            if (lexeme.getLastRelation().equals(RELATION_LESS)) {
                return rightPart;
            }
        }
        return rightPart;
    }

    private void reverseLexemes() {
        Collections.reverse(this.lexemes);
    }

    @Override
    public String toString() {
        String lex ="";
        for (LexemeRelation lexeme : lexemes) {
            lex+=lexeme.getName()+" ";
        }
        return lex;
    }

    public String peek() {
        return lexemes.get(lastIndex()).getName();
    }
}
