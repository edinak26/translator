package phoenix.accessory.info;

import phoenix.general.model.lexical.analyzer.SeparatorConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GrammarConfig implements SeparatorConstants {
    //TYPE(REGEX_TYPE),
    SEPARATOR(REGEX_SEPARATOR);

    Pattern regexPattern;
    GrammarConfig(String regex) {
        this.regexPattern = Pattern.compile(regex);
    }
    public boolean check(String string){
        Matcher m = regexPattern.matcher(string);
        return m.matches();
    }

}
