package phoenix.exceptions;

import phoenix.interfaces.Characters;

public interface GrammarExceptions extends Characters {
    String WRONG_GRAMMAR_ELEMENT = "Grammar element is wrong";

    String WRONG_GRAMMAR_ELEMENT_MESSAGE=WRONG_GRAMMAR_ELEMENT+COLON+SPACE;
}
