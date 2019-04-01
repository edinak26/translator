package phoenix.general.model.syntax.analyzer;

import phoenix.accessory.constant.Characters;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RelationsTable implements Characters {
    String[][] relations;
    List<List<String>> grammar;
    List<String> terms;
    private static Logger log = Logger.getLogger(RelationsTable.class.getName());




    public RelationsTable(List<List<String>> grammar) {
        this.grammar = grammar;
        Searcher.setGrammar(grammar);
        this.terms = getUniqueTerms();
        relations = new String[terms.size()][terms.size()];
        setRelations();

    }
    private ArrayList<String> getUniqueTerms() {
        LinkedHashSet<String> terms = new LinkedHashSet<>();
        for (List<String> rule : grammar) {
            terms.add(rule.get(0));
        }
        for (List<String> rule : grammar) {
            for (int i = 1; i < rule.size(); i++) {
                if (!rule.get(i).equals("|"))
                    terms.add(rule.get(i));
            }
        }
        return new ArrayList<>(terms);
    }

    private void setRelations() {
        for (List<String> rule : grammar) {
            for (int i = 1; i < rule.size() - 1; i++) {
                if (!rule.get(i).equals("|") && !rule.get(i + 1).equals("|")) {
                    setEqualRel(rule.get(i), rule.get(i + 1));
                    setMoreRel(Searcher.get().last(rule.get(i)), rule.get(i+1));
                    setLessRel(rule.get(i), Searcher.get().first(rule.get(i+1)));
                    setMoreRel(Searcher.get().last(rule.get(i)), Searcher.get().first(rule.get(i+1)));
                }
            }
        }
    }

    private void setEqualRel(String ter1, String ter2) {
        if(relations[terms.indexOf(ter1)][terms.indexOf(ter2)]!=null&&!relations[terms.indexOf(ter1)][terms.indexOf(ter2)].equals(RELATION_EQUALITY)){
            log.log(Level.INFO,
                    "Rewrite( "+ter1+" | "+ter2+" ) from " +relations[terms.indexOf(ter1)][terms.indexOf(ter2)] + " to =");
        }
        relations[terms.indexOf(ter1)][terms.indexOf(ter2)] = RELATION_EQUALITY;
    }

    private void setMoreRel(String ter1, String ter2) {
        if(relations[terms.indexOf(ter1)][terms.indexOf(ter2)]!=null&&!relations[terms.indexOf(ter1)][terms.indexOf(ter2)].equals(RELATION_MORE)){
            log.log(Level.INFO,
                    "Rewrite( "+ter1+" | "+ter2+" ) from " +relations[terms.indexOf(ter1)][terms.indexOf(ter2)] + " to >");

        }
        relations[terms.indexOf(ter1)][terms.indexOf(ter2)] = RELATION_MORE;
    }

    private void setMoreRel(HashSet<String> lastPlus, String ter) {
        for (String term : lastPlus) {
            setMoreRel(term, ter);
        }
    }
    private void setMoreRel(HashSet<String> lastPlus, HashSet<String> firstPlus) {
        for (String term : firstPlus) {
            setMoreRel(lastPlus, term);
        }
    }

    private void setLessRel(String ter1, String ter2) {
        if(relations[terms.indexOf(ter1)][terms.indexOf(ter2)]!=null&&!relations[terms.indexOf(ter1)][terms.indexOf(ter2)].equals(RELATION_LESS)){
            log.log(Level.INFO,
                    "Rewrite( "+ter1+" | "+ter2+" ) from " +relations[terms.indexOf(ter1)][terms.indexOf(ter2)] + " to <");
        }
        relations[terms.indexOf(ter1)][terms.indexOf(ter2)] = RELATION_LESS;
    }

    private void setLessRel( String ter, HashSet<String> firstPlus) {
        for (String term : firstPlus) {
            setLessRel(ter, term);
        }
    }

    public String[][] getRelations() {
        return relations;
    }

    public List<List<String>> getGrammar() {
        return grammar;
    }

    public List<String> getTerms() {
        return terms;
    }
}
