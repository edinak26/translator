package phoenix.general.model.syntax.analyzer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import phoenix.general.model.lexical.analyzer.LexemesTableElement;

import java.util.Stack;

public class Messages {
    private static String message="";

    public static Messages stack(Stack<LexemesTableElement> stack){//}, String nextLexeme, String relation){
        for (LexemesTableElement el : stack) {
            message += el.lexeme() + " ";
        }
        return new Messages();
    }

    public String get(){
        String mes = message;
        message="";
        return mes;
    }

}
