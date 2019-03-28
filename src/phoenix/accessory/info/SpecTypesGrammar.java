package phoenix.accessory.info;

import phoenix.general.model.lexical.analyzer.SeparatorConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SpecTypesGrammar implements SeparatorConstants {
    IDN(REGEX_IDENTIFIER),
    CON(REGEX_CONSTANT),
    POINTER(REGEX_POINTER);

    Pattern regexPattern;

    SpecTypesGrammar(String regex) {
        this.regexPattern = Pattern.compile(regex);
    }

    public static String check(String string){
        for(SpecTypesGrammar type: SpecTypesGrammar.values()) {
            Matcher m = type.regexPattern.matcher(string);
            if(m.matches())
                return type.name();
        }
        return null;
    }
}
