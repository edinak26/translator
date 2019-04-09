package phoenix.general.model.entities;

import java.util.*;

public class Axiom extends NonTerminal {
    private Map<String, Set<String>[]> blocksSets;

    public Axiom(String name) {
        super(name);
        setAxiom(true);
        blocksSets = new HashMap<>();
    }

    public Axiom(NonTerminal nonTerminal) {
        super(nonTerminal.getName(), nonTerminal.getCurrBlock());
        setAxiom(true);
        blocksSets = new HashMap<>();
    }

    public void addSets(String block, Set<String> after, Set<String> before) {
        Set[] sets = new Set[2];
        sets[0] = new HashSet<>(after);
        sets[1] = new HashSet<>(before);
        blocksSets.put(block, sets);
    }

    public void check() {
        checkAfter();
        checkBefore();
    }

    private void checkAfter() {
        for (Map.Entry<String, Set<String>[]> entry1 : blocksSets.entrySet()) {
            for (Map.Entry<String, Set<String>[]> entry2 : blocksSets.entrySet()) {
                if (!entry1.equals(entry2)) {//TODO try to optimise
                    Set<String> after1 = entry1.getValue()[0];
                    Set<String> after2 = entry2.getValue()[0];
                    Set<String> af = new HashSet<>(after1);
                    af.retainAll(after2);
                    if (!af.isEmpty()) {
                        System.out.println("ERROR in " + getName() + " : "
                                + entry1.getKey() + "|" + after1.toString()
                                + entry2.getKey() + "|" + after2.toString());
                    }
                }
            }
        }
    }

    private void checkBefore() {
        for (Map.Entry<String, Set<String>[]> entry1 : blocksSets.entrySet()) {
            for (Map.Entry<String, Set<String>[]> entry2 : blocksSets.entrySet()) {
                if (!entry1.equals(entry2)) {//TODO try to optimise
                    Set<String> before1 = entry1.getValue()[1];
                    Set<String> before2 = entry2.getValue()[1];
                    Set<String> bf = new HashSet<>(before1);
                    bf.retainAll(before2);
                    if (!bf.isEmpty()) {
                        System.out.println("ERROR in " + getName() + " : "
                                + entry1.getKey() + "|" + before1.toString()
                                + entry2.getKey() + "|" + before2.toString());
                    }
                }
            }
        }
    }

    public String getNextBlock(String lastLex, String nextLex, String currBlock) {
        String resultAfter="";
        for (Map.Entry<String, Set<String>[]> entry : blocksSets.entrySet()) {
            for (String afterLex : entry.getValue()[0]) {
                if (afterLex.equals(nextLex)) {
                    System.out.println('-');
                    resultAfter = entry.getKey();
                }
            }
        }
        String resultBefore="";
        for (Map.Entry<String, Set<String>[]> entry : blocksSets.entrySet()) {
            for (String beforeLex : entry.getValue()[1]) {
                if (beforeLex.equals(lastLex)) {
                    System.out.println("+"+entry.getKey()+lastLex);
                    resultBefore = entry.getKey();
                }
            }
        }
        System.out.println("="+resultAfter+" "+resultBefore);
       // if(resultBefore.equals(currBlock))
            //return resultBefore;
        return resultAfter;
    }

    public void showAfter() {
        for (Map.Entry<String, Set<String>[]> entry : blocksSets.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue()[0]);
        }
    }

    public void showBefore() {
        for (Map.Entry<String, Set<String>[]> entry : blocksSets.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue()[1]);
        }
    }
}
