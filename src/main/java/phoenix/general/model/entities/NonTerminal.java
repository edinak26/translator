package phoenix.general.model.entities;

import phoenix.general.interfaces.MetaLanguage;

import java.util.*;

public class NonTerminal implements MetaLanguage {
    private String name;
    private List<String> blocks;
    private boolean isAxiom;

    NonTerminal(String name) {
        this.name = name;
    }

    public void setBlocks(Stack<String> blocks) {
        this.blocks = new ArrayList<>(blocks);
        Collections.reverse(this.blocks);
    }

    public String getName() {
        return name;
    }

    public List<String> getBlocks() {
        return blocks;
    }


    public String getCurrBlock() {
        if (blocks.size() == 0) {
            return null;
        }
        return blocks.get(0);
    }

    public void setAxiom(boolean isAxiom) {
        this.isAxiom = isAxiom;
    }

    public boolean isAxiomOf(String block) {
        return isAxiom && blocks.get(0).equals(block);
    }
}
