package phoenix.general.model.lexical.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexemeTypes {
    private List<LexemeType> types;
    LexemeTypes(List<List<String>> splitText){
        types = new ArrayList<>();
        for(List<String> rule : splitText){
            for(String lexeme : rule) {
                if(isType(lexeme)&&!hasType(lexeme)) {
                    types.add(new LexemeType(lexeme));
                }
            }
        }
    }
    private boolean isType(String lex){
        if(lex.length()<3&&!lex.equals("|"))
            return true;
        Pattern p = Pattern.compile("^[^<].+[^>]$");//TODO optimise compile
        Matcher m = p.matcher(lex);
        return m.matches();
    }

    public boolean hasType(String lexeme){
        for(LexemeType type : types){
            if(lexeme.equals(type.getName()))
                return true;
        }
        return false;
    }

    public void show(){
        for(LexemeType lex:types){
            System.out.println(lex.getIndex()+" "+lex.getName());
        }
    }
}
