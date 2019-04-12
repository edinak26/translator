package phoenix.general.model.entities;

import phoenix.general.interfaces.MetaLanguage;

import java.util.*;

public class NonTerminal extends Terminal implements MetaLanguage {
    private List<VisibilityBlock> blocks;
    private Map<VisibilityBlock, Condition> sets;

    public NonTerminal(String name, List<VisibilityBlock> blocks) {
        super(name);
        this.blocks = new ArrayList<>(blocks);
    }

    public NonTerminal(String name, Stack<VisibilityBlock> blocks) {
        super(name);
        this.blocks = new ArrayList<>(blocks);
        Collections.reverse(this.blocks);
    }

    public VisibilityBlock getBlock() {
        return blocks.get(0);
    }

    public boolean isInBlock(VisibilityBlock block){
        return blocks.contains(block);
    }

    public boolean isAxiom() {
        return sets != null;
    }

    public boolean isAxiomOf(VisibilityBlock block) {
        return isAxiom() && getBlock().equals(block);
    }





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
        List<String> resultAfter = new ArrayList<>();
        for (Map.Entry<String, Set<String>[]> entry : blocksSets.entrySet()) {
            for (String afterLex : entry.getValue()[0]) {
                if (afterLex.equals(nextLex)) {
                    System.out.println('-' + entry.getKey());
                    resultAfter.add(entry.getKey());
                }
            }
        }
        List<String> resultBefore = new ArrayList<>();
        for (Map.Entry<String, Set<String>[]> entry : blocksSets.entrySet()) {
            for (String beforeLex : entry.getValue()[1]) {
                if (beforeLex.equals(lastLex)) {
                    System.out.println("+" + entry.getKey());
                    resultBefore.add(entry.getKey());
                }
            }
        }
        resultAfter.retainAll(resultBefore);
        if(resultAfter.size()!=1){
            System.out.println("ERRROR< UNCORRECT GRAMMAR");
        }
        // if(resultBefore.equals(currBlock))
        //return resultBefore;
        return resultAfter.get(0);
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
