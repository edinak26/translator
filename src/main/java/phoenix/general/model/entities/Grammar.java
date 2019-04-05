package phoenix.general.model.entities;

import phoenix.general.interfaces.MetaLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;

public class Grammar implements MetaLanguage {
    private Map<NonTerminal, List<List<String>>> grammar;

    Grammar(List<List<String>> splitText) {
        grammar = GrammarConstructor.getGrammar(splitText);
    }


}
