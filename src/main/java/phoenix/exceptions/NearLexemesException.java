package phoenix.exceptions;

import phoenix.model.lexical.analyzer.LexemesTableElement;

public class NearLexemesException extends RuntimeException {
    private static String getMessage(LexemesTableElement elem, String lex) {
        return "Лексема: " + lex + " " + "не может стоять здесь" + "\n line:" + elem.getLineNum() + "\n lex num: " + elem.getLineLexNum();
    }

    public NearLexemesException(LexemesTableElement elem, String lex) {
        super(getMessage(elem, lex));
    }
}


