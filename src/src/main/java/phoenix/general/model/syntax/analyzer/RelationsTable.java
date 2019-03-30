package src.main.java.phoenix.general.model.syntax.analyzer;

import src.main.java.phoenix.accessory.constant.Characters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class RelationsTable implements Characters {
    String[][] relations;
    List<List<String>> grammar;
    List<String> terms;

    public RelationsTable(List<List<String>> grammar) {
        this.grammar = grammar;
        LinkedHashSet<String> terms = new LinkedHashSet<>();
        setTerms(terms);
        this.terms = new ArrayList<>(terms);
        relations = new String[terms.size()][terms.size()];
        setRelations();
        for (String[] line : relations) {
            for (String rel : line) {
                System.out.print(rel + "|");
            }
            System.out.println();
        }
    }

    private void setTerms(LinkedHashSet<String> terms) {
        for (List<String> rule : grammar) {
            terms.add(rule.get(0));
        }
        for (List<String> rule : grammar) {
            for (int i = 1; i < rule.size(); i++) {
                if (!rule.get(i).equals("|"))
                    terms.add(rule.get(i));
            }
        }

    }

    public HashSet<String> getFirstPlus(String term) {
        HashSet<String> firstPlus = new HashSet<>(getFirst(term));
        for (String ter : getFirst(term)) {
            if (!ter.equals(term))
                firstPlus.addAll(getFirstPlus(ter));
        }
        return firstPlus;
    }

    public HashSet<String> getLastPlus(String term) {
        HashSet<String> lastPlus = new HashSet<>(getLast(term));
        for (String ter : getLast(term)) {
            if (!ter.equals(term))
                lastPlus.addAll(getLastPlus(ter));
        }
        return lastPlus;
    }

    public HashSet<String> getFirst(String term) {
        HashSet<String> first = new HashSet<>();
        for (List<String> rule : grammar) {
            if (rule.get(0).equals(term)) {
                for (int i = 1; i < rule.size(); i++) {
                    if (i == 1 || rule.get(i - 1).equals("|")) {
                        first.add(rule.get(i));
                    }
                }
            }
        }
        return first;
    }

    public HashSet<String> getLast(String term) {
        HashSet<String> last = new HashSet<>();
        for (List<String> rule : grammar) {
            if (rule.get(0).equals(term)) {
                for (int i = 1; i < rule.size(); i++) {
                    if (i == rule.size() - 1 || rule.get(i + 1).equals("|")) {
                        last.add(rule.get(i));
                    }
                }
            }
        }
        return last;
    }

    private void setRelations() {
        for (List<String> rule : grammar) {
            for (int i = 1; i < rule.size() - 1; i++) {
                if (!rule.get(i).equals("|") && !rule.get(i + 1).equals("|")) {
                    /*if(rule.get(i).equals("<список змінних>")||rule.get(i+1).equals("<список змінних>")){
                        System.out.println(rule.get(i)+"|"+rule.get(i+1));
                        System.out.println(getLastPlus(rule.get(i + 1))+"|\n|"
                                +getFirstPlus(rule.get(i + 1))
                        + "||\n||"+getLastPlus(rule.get(i)));
                    }*/
                    setEqualRel(rule.get(i), rule.get(i + 1));
                    setMoreRel(getLastPlus(rule.get(i)), rule.get(i+1));
                    setLessRel(rule.get(i),getFirstPlus(rule.get(i + 1)));
                    setMoreRel(getLastPlus(rule.get(i)),getFirstPlus(rule.get(i + 1)));
                }
            }
        }
    }

    private void setEqualRel(String ter1, String ter2) {
        relations[terms.indexOf(ter1)][terms.indexOf(ter2)] = RELATION_EQUALITY;
    }

    private void setMoreRel(String ter1, String ter2) {
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
        relations[terms.indexOf(ter1)][terms.indexOf(ter2)] = RELATION_LESS;
    }

    private void setLessRel( String ter, HashSet<String> firstPlus) {
        for (String term : firstPlus) {
            setLessRel(ter, term);
        }
    }
}
