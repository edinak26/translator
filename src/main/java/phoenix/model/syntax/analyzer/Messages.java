package phoenix.model.syntax.analyzer;

import phoenix.model.lexical.analyzer.LexemesTableElement;

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
