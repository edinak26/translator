package phoenix.model.grammar;

import phoenix.interfaces.MetaLanguage;
import phoenix.model.grammar.entities.*;
import phoenix.model.grammar.searcher.SetsSearcher;
import phoenix.model.syntax.analyzer.relations.RelationsTable;

import java.util.*;

public class Grammar implements MetaLanguage {
    private VisibilityBlock global;
    private Set<Terminal> uniqueTerminals;
    private Rules allRules;

    public Grammar(VisibilityBlock global){
        this.global=global;
    }

    public void setUniqueTerminals(Set<Terminal> uniqueTerminals) {
        this.uniqueTerminals = uniqueTerminals;
    }

    public void setRules(Rules rules) {
        allRules = rules;
    }

    public Set<Terminal> getUniqueTerminals() {
        return uniqueTerminals;
    }

    public Rules getAllRules() {
        return allRules;
    }

    public VisibilityBlock getGlobal() {
        return global;
    }


    public void show() {
        global.show();
    }
}
