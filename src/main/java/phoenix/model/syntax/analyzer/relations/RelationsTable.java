package phoenix.model.syntax.analyzer.relations;

import phoenix.model.grammar.entities.Replace;
import phoenix.model.grammar.entities.Rule;
import phoenix.model.grammar.entities.Rules;
import phoenix.model.grammar.entities.Terminal;
import phoenix.model.grammar.searcher.SetsSearcher;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static phoenix.interfaces.Characters.*;
import static phoenix.model.syntax.analyzer.relations.Relation.*;

public class RelationsTable {
    private Relation[][] relations;
    private List<Terminal> uniqueTerminals;
    private static Logger logger = Logger.getLogger(RelationsTable.class.getName());
    ;

    public RelationsTable(Rules rules, Set<Terminal> uniqueTerminals) {
        this.uniqueTerminals = new ArrayList<>(uniqueTerminals);
        setStartRelations();
        configRelations(rules);
    }

    private void setStartRelations() {
        relations = new Relation[uniqueTerminals.size() + 1][uniqueTerminals.size() + 1];
        for (int i = 0; i < relations.length; i++)
            for (int j = 0; j < relations.length; j++)
                relations[i][j] = UNDEFINED;
        setEnd();
    }


    private void setEnd() {
        uniqueTerminals.add(new Terminal(END_TERMINAL));
        for (int i = 0; i < relations.length; i++) {
            relations[relations.length-1][i] = LESS;
            relations[i][relations.length-1] = MORE;
        }
    }

    private void configRelations(Rules rules) {
        for (Rule rule: rules.getRules()) {
            for(Replace ruleReplace : rule.getReplaces()){
                List <Terminal> replace =  ruleReplace.toList();
                for(int i = 0; i < replace.size() - 1; i++){
                    Terminal currTerminal = replace.get(i);
                    Terminal nextTerminal = replace.get(i + 1);
                    Set<Terminal> curLast = SetsSearcher.getLastPlus(rules,currTerminal);
                    Set<Terminal> nextFirst = SetsSearcher.getFirstPlus(rules,nextTerminal);

                    setEqualRel(currTerminal, nextTerminal);
                    setMoreRel(curLast, nextTerminal);
                    setLessRel(currTerminal, nextFirst);
                    setMoreRel(curLast, nextFirst);

                }
            }
        }
    }


    private void setRelation(Terminal first, Terminal second, Relation relation) {
        checkRewrite(first, second, relation);

        int f = uniqueTerminals.indexOf(first);
        int s = uniqueTerminals.indexOf(second);
        relations[f][s] = relation;
    }


    private void setEqualRel(Terminal first, Terminal second) {
        setRelation(first, second, EQUAL);
    }

    private void setMoreRel(Terminal first, Terminal second) {
        setRelation(first, second, MORE);
    }

    private void setLessRel(Terminal first, Terminal second) {
        setRelation(first, second, LESS);
    }

    private void setMoreRel(Set<Terminal> lastPlus, Terminal ter) {
        for (Terminal term : lastPlus) {
            setMoreRel(term, ter);
        }
    }

    private void setMoreRel(Set<Terminal> lastPlus, Set<Terminal> firstPlus) {
        for (Terminal term : firstPlus) {
            setMoreRel(lastPlus, term);
        }
    }

    public void setLessRel(Terminal ter, Set<Terminal> firstPlus) {
        for (Terminal term : firstPlus) {
            setLessRel(ter, term);
        }
    }

    public Relation getRelation(Terminal first, Terminal second) {
        int f = uniqueTerminals.indexOf(first);
        int s = uniqueTerminals.indexOf(second);
        return relations[f][s];

    }

    private void checkRewrite(Terminal first, Terminal second, Relation relation) {
        int f = uniqueTerminals.indexOf(first);
        int s = uniqueTerminals.indexOf(second);
        Relation oldRelation = relations[f][s];

        boolean hasRelation = oldRelation != UNDEFINED;
        boolean hasAnotherRelation = oldRelation != relation;

        if (hasRelation && hasAnotherRelation)
            logger.log(Level.INFO,
                    "Rewrite( " + first + " | " + second + " ) from " + oldRelation + " to " + relation);
    }
}
