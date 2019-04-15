package phoenix.general.model.entities;

import phoenix.general.interfaces.MetaLanguage;

import java.util.*;
import java.util.stream.Collectors;

public class NonTerminal extends Terminal implements MetaLanguage {
    private List<VisibilityBlock> blocks;
    private Map<VisibilityBlock, Set<Terminal>[]> sets;

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

    public boolean isInBlock(VisibilityBlock block) {
        return blocks.contains(block);
    }

    public boolean isAxiom() {
        return sets != null;
    }

    public boolean isAxiomOf(VisibilityBlock block) {
        return isAxiom() && getBlock().equals(block);
    }


    public void addSets(VisibilityBlock block, Set<Terminal> after, Set<Terminal> before) {
        Set[] sets = new Set[2];
        sets[0] = new HashSet<>(after);
        sets[1] = new HashSet<>(before);
        this.sets.put(block, sets);
    }

    public VisibilityBlock nextBlock(Terminal lastLex, Terminal nextLex, String currBlock) {
        if (sets == null) {
            return getBlock();
        }
        return getNextBlock(lastLex, nextLex, currBlock);

    }

    private VisibilityBlock getNextBlock(Terminal lastLex, Terminal nextLex, String currBlock) {
        List<VisibilityBlock> resultAfter = sets.entrySet().stream()
                .filter(e -> e.getValue()[0].contains(nextLex))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<VisibilityBlock> resultBefore = sets.entrySet().stream()
                .filter(e -> e.getValue()[1].contains(lastLex))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        resultAfter.retainAll(resultBefore);

        if (resultAfter.size() != 1) {
            System.out.println("ERRROR: UNCORRECT GRAMMAR");
        }

        return resultAfter.get(0);
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
}
