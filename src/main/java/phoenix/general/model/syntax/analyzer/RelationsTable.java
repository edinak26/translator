package phoenix.general.model.syntax.analyzer;

import phoenix.accessory.constant.Characters;
import phoenix.general.model.entities.Grammar;
import phoenix.general.model.entities.GrammarConstructor;
import phoenix.general.model.entities.Terminal;
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
        this.grammar = GrammarConstructor.construct(TextReader
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

    public void setEqualRel(Terminal ter1, Terminal ter2) {
        if (relations[uniqueTerminals.indexOf(ter1.getName())][uniqueTerminals.indexOf(ter2.getName())] != null && !relations[uniqueTerminals.indexOf(ter1.getName())][uniqueTerminals.indexOf(ter2.getName())].equals(RELATION_EQUALITY)) {
            log.log(Level.INFO,
                    "Rewrite( " + ter1 + " | " + ter2 + " ) from " + relations[uniqueTerminals.indexOf(ter1.getName())][uniqueTerminals.indexOf(ter2.getName())] + " to =");
        }
        relations[uniqueTerminals.indexOf(ter1.getName())][uniqueTerminals.indexOf(ter1.getName())] = RELATION_EQUALITY;
    }

    private void setMoreRel(Terminal ter1, Terminal ter2) {
        if (relations[uniqueTerminals.indexOf(ter1.getName())][uniqueTerminals.indexOf(ter2.getName())] != null && !relations[uniqueTerminals.indexOf(ter1.getName())][uniqueTerminals.indexOf(ter2.getName())].equals(RELATION_MORE)) {
            log.log(Level.INFO,
                    "Rewrite( " + ter1.getName() + " | " + ter2.getName() + " ) from " + relations[uniqueTerminals.indexOf(ter1.getName())][uniqueTerminals.indexOf(ter2.getName())] + " to >");

        }
        relations[uniqueTerminals.indexOf(ter1.getName())][uniqueTerminals.indexOf(ter2.getName())] = RELATION_MORE;
    }

    public void setMoreRel(Set<Terminal> lastPlus, Terminal ter) {
        for (Terminal term : lastPlus) {
            setMoreRel(term, ter);
        }
    }

    public void setMoreRel(Set<Terminal> lastPlus, Set<Terminal> firstPlus) {
        for (Terminal term : firstPlus) {
            setMoreRel(lastPlus, term);
        }
    }

    private void setLessRel(Terminal ter1, Terminal ter2) {
        if (relations[uniqueTerminals.indexOf(ter1.getName())][uniqueTerminals.indexOf(ter2.getName())] != null && !relations[uniqueTerminals.indexOf(ter1.getName())][uniqueTerminals.indexOf(ter2.getName())].equals(RELATION_LESS)) {
            log.log(Level.INFO,
                    "Rewrite( " + ter1.getName() + " | " + ter2.getName() + " ) from " + relations[uniqueTerminals.indexOf(ter1.getName())][uniqueTerminals.indexOf(ter2.getName())] + " to <");
        }
        relations[uniqueTerminals.indexOf(ter1.getName())][uniqueTerminals.indexOf(ter2.getName())] = RELATION_LESS;
    }

    public void setLessRel(Terminal ter, Set<Terminal> firstPlus) {
        for (Terminal term : firstPlus) {
            setLessRel(ter, term);
        }
    }

    public String getRelation(Terminal term1, Terminal term2) {
        return relations[uniqueTerminals.indexOf(term1.getName())][uniqueTerminals.indexOf(term2.getName())];
    }

    public String[][] getRelations() {
        return relations;
    }

    public List<String> getTerms() {
        return uniqueTerminals;
    }

}
