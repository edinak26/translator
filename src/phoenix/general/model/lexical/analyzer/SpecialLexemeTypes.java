package phoenix.general.model.lexical.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecialLexemeTypes {
    private List<List<String>> specTypes;

    SpecialLexemeTypes(List<List<String>> splitText){
        specTypes = splitText;
    }

    public String getType(String lex){
        for(List<String> type : specTypes){
            Pattern p = Pattern.compile(type.get(1));//TODO optimise compile
            Matcher m = p.matcher(lex);
            if(m.matches())
                return type.get(0);
        }
        return null;
    }
}
