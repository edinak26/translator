package phoenix.general.model.lexical.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexemes {
    List<Lexeme> lexemes;
    Lexemes(List<List<String>> splitText){
        lexemes = new ArrayList<>();
        for(List<String> rule : splitText){
            for(String lex : rule) {
                if(isCorrect(lex)&&!hasLexeme(lex)) {
                    lexemes.add(new Lexeme(lex));
                }
            }
        }
    }
    private boolean isCorrect(String lex){
        if(lex.length()<3)
            return true;
        Pattern p = Pattern.compile("^[^<].+[^>]$");
        Matcher m = p.matcher(lex);
        return m.matches();
    }

    private boolean hasLexeme(String lex){
        for(Lexeme lexeme: lexemes){
            if(lex.equals(lexeme.getName()))
                return true;
        }
        return false;
    }
    public void show(){
        for(Lexeme lex:lexemes){
            System.out.println(lex.getName());
        }
    }
}
