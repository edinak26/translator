package phoenix.general.model.entities;

import java.util.*;

public class NonTerminal {
    private String name;
    private List<String> blocks;

    NonTerminal(String name) {
        this.name = name;
    }

    public void setBlocks(Stack<String> blocks) {
        this.blocks = new ArrayList<>(blocks);
        Collections.reverse(this.blocks);
    }
}
