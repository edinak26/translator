package phoenix.general.model.syntax.analyzer;

import phoenix.accessory.constant.Characters;
import phoenix.general.model.entities.Grammar;
import phoenix.general.model.reader.TextReader;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RelationsTable implements Characters {
    private String[][] relations;
    private Grammar grammar;
    private List<String> uniqueTerminals;
    private static Logger log = Logger.getLogger(RelationsTable.class.getName());

    private static final String STRAT_GRAM_PATH = "D:\\University\\Java\\translator\\src\\main\\java\\phoenix\\accessory\\info\\stratGram";

    public RelationsTable() throws Exception {
        this.grammar = new Grammar(TextReader
                .grammar()
                .setPath(STRAT_GRAM_PATH)
                .get());

        this.uniqueTerminals = grammar.getUniqueTerminals();
        relations = new String[uniqueTerminals.size() + 1][uniqueTerminals.size() + 1];
        grammar.configRelations(this);
        setEnd();
    }

    private void setEnd() {

        for (int i = 0; i < uniqueTerminals.size(); i++) {
            relations[uniqueTerminals.size()][i] = RELATION_LESS;
            relations[i][uniqueTerminals.size()] = RELATION_MORE;
        }
        uniqueTerminals.add("#");
    }

    public void setEqualRel(String ter1, String ter2) {
        if (relations[uniqueTerminals.indexOf(ter1)][uniqueTerminals.indexOf(ter2)] != null && !relations[uniqueTerminals.indexOf(ter1)][uniqueTerminals.indexOf(ter2)].equals(RELATION_EQUALITY)) {
            log.log(Level.INFO,
                    "Rewrite( " + ter1 + " | " + ter2 + " ) from " + relations[uniqueTerminals.indexOf(ter1)][uniqueTerminals.indexOf(ter2)] + " to =");
        }
        relations[uniqueTerminals.indexOf(ter1)][uniqueTerminals.indexOf(ter2)] = RELATION_EQUALITY;
    }

    private void setMoreRel(String ter1, String ter2) {
        if (relations[uniqueTerminals.indexOf(ter1)][uniqueTerminals.indexOf(ter2)] != null && !relations[uniqueTerminals.indexOf(ter1)][uniqueTerminals.indexOf(ter2)].equals(RELATION_MORE)) {
            log.log(Level.INFO,
                    "Rewrite( " + ter1 + " | " + ter2 + " ) from " + relations[uniqueTerminals.indexOf(ter1)][uniqueTerminals.indexOf(ter2)] + " to >");

        }
        relations[uniqueTerminals.indexOf(ter1)][uniqueTerminals.indexOf(ter2)] = RELATION_MORE;
    }

    public void setMoreRel(Set<String> lastPlus, String ter) {
        for (String term : lastPlus) {
            setMoreRel(term, ter);
        }
    }

    public void setMoreRel(Set<String> lastPlus, Set<String> firstPlus) {
        for (String term : firstPlus) {
            setMoreRel(lastPlus, term);
        }
    }

    private void setLessRel(String ter1, String ter2) {
        if (relations[uniqueTerminals.indexOf(ter1)][uniqueTerminals.indexOf(ter2)] != null && !relations[uniqueTerminals.indexOf(ter1)][uniqueTerminals.indexOf(ter2)].equals(RELATION_LESS)) {
            log.log(Level.INFO,
                    "Rewrite( " + ter1 + " | " + ter2 + " ) from " + relations[uniqueTerminals.indexOf(ter1)][uniqueTerminals.indexOf(ter2)] + " to <");
        }
        relations[uniqueTerminals.indexOf(ter1)][uniqueTerminals.indexOf(ter2)] = RELATION_LESS;
    }

    public void setLessRel(String ter, Set<String> firstPlus) {
        for (String term : firstPlus) {
            setLessRel(ter, term);
        }
    }

    public String getRelation(String term1, String term2) {
        return relations[uniqueTerminals.indexOf(term1)][uniqueTerminals.indexOf(term2)];
    }

    public String[][] getRelations() {
        return relations;
    }

    public List<String> getTerms() {
        return uniqueTerminals;
    }

}
