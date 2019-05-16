package phoenix.exceptions;


public class NotLexemeException extends RuntimeException{
    private static String getMessage(String lex, int line, int lexNum) {
        return "Лексема: " + lex + " " + "некорректна" + "\n line:" + (line+1) + "\n lex num: " + (lexNum+1);
    }

    public NotLexemeException(String lex, int line, int lexNum) {
        super(getMessage(lex, line,lexNum));
    }
}
