package phoenix.general.model.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrammarConstructor {
    private Map<NonTerminal, List<List<String>>> grammar;
    List<List<String>> splitText;

    private GrammarConstructor(List<List<String>> splitText) {
        grammar = new HashMap<>();
        this.splitText = splitText;
    }

    public static Map<NonTerminal, List<List<String>>> getGrammar(List<List<String>> splitText) {
        GrammarConstructor constructor = new GrammarConstructor(splitText);
        constructor.convert();
        return constructor.grammar;
    }

    private void convert() {
        for (List<String> line : splitText){

        }
    }
}
