package phoenix.model.grammar.entities;

import phoenix.interfaces.MetaLanguage;

import java.util.*;
import java.util.stream.Collectors;

public class NonTerminal extends Terminal implements MetaLanguage {
    private VisibilityBlock block;
    private Map<VisibilityBlock, Set<Terminal>[]> sets;

    public NonTerminal(String name, Stack<VisibilityBlock> blocks) {//TODO delete this constructor
        super(name);
        this.block = blocks.peek();
    }

    public NonTerminal (String name){
        super(name);
    }

    public NonTerminal(Terminal terminal, VisibilityBlock block){
        super(terminal);
        this.block = block;
    }

    public VisibilityBlock getBlock() {
        return block;
    }

    public boolean isAxiom() {
        return getBlock().isAxiom(this);
    }

    public void setAxiom() {
        sets = new HashMap<>();
    }


    public void addSets(VisibilityBlock block, Set<Terminal> after, Set<Terminal> before) {
        if (!isAxiom())
            setAxiom();
        Set[] sets = new Set[2];
        if (before != null)
        sets[0] = new HashSet<>(after);
        if (before != null)
            sets[1] = new HashSet<>(before);
        this.sets.put(block, sets);
    }

    private Set<Terminal> createAfter(VisibilityBlock block, Set<Terminal> after) {
        if (after == null)
            return null;
        if (after.isEmpty())
            return block.getAxiom().getAfterTerminals();
        return after;
    }

    private Set<Terminal> getAfterTerminals() {
        return sets.entrySet().stream()
                .filter(e -> e.getValue()[0] != null)
                .flatMap(e -> e.getValue()[0].stream())
                .collect(Collectors.toSet());
    }

    public VisibilityBlock nextBlock(Terminal lastLex, Terminal nextLex) {
        if (sets == null) {
            return getBlock();
        }
        return getNextBlock(lastLex, nextLex);

    }

    private VisibilityBlock getNextBlock(Terminal lastLex, Terminal nextLex) {
        //System.out.println(lastLex+" "+nextLex);
        //TODO addChild block check in set search &&block.has(e.getKey())
        List<VisibilityBlock> resultAfter = sets.entrySet().stream()
                .filter(e -> e.getValue()[0] != null)
                .filter(e -> e.getValue()[0].contains(nextLex))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<VisibilityBlock> resultBefore = sets.entrySet().stream()
                .filter(e -> e.getValue()[1] != null)
                .filter(e ->e.getValue()[1].contains(lastLex))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());




        showAfter();
        showBefore();
        //System.out.println(resultAfter);
        //System.out.println(resultBefore);
        resultAfter.retainAll(resultBefore);
        if (resultAfter.size() == 1) {
            return resultAfter.get(0);
        } else if(resultAfter.size() == 0)
            return selectNextFromAllBlocks(lastLex,nextLex);
        else
            throw new RuntimeException("UNCORRECT GRAMMAR");


    }


    private VisibilityBlock selectNextFromAllBlocks(Terminal lastLex, Terminal nextLex) {
        Map<VisibilityBlock, Set<Terminal>[]> wideSets = new HashMap<VisibilityBlock, Set<Terminal>[]>();
        for(Map.Entry<VisibilityBlock, Set<Terminal>[]> entry :sets.entrySet()){

        }
        Set<Terminal> wideAfter = sets.entrySet().stream()
                .filter(e -> e.getValue()[0] != null)
                .flatMap(
                        e->e.getValue()[0].stream()
                                .anyMatch(Objects::isNull)?getWideAfter(e).stream():e.getValue()[0].stream()
                        )
                .collect(Collectors.toSet());
        //System.out.println("RRRRRR"+wideAfter);
        return null;
    }

    private Set<Terminal> getWideAfter(Map.Entry<VisibilityBlock,Set<Terminal>[]> afterEntry){
        Set<Terminal> result = afterEntry.getKey().getAxiom().getAfter();
        result.addAll(afterEntry.getValue()[0]);
        return result;
    }

    private Set<Terminal> getAfter(){
        Set<Terminal> result = new HashSet<>();
        sets.forEach((v,s)->{if(s[0]!=null)result.addAll(s[0]);});
        return result;
    }


    public void showAfter() {
        for (Map.Entry<VisibilityBlock, Set<Terminal>[]> entry : sets.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue()[0]);
        }
    }

    public void showBefore() {
        for (Map.Entry<VisibilityBlock, Set<Terminal>[]> entry : sets.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue()[1]);
        }
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
