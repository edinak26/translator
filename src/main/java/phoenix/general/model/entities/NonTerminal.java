package phoenix.general.model.entities;

import phoenix.general.interfaces.MetaLanguage;

import java.util.*;

public class NonTerminal implements MetaLanguage {
    private String name;
    private List<String> blocks;

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

    public String getCurrBlock(){
        if(blocks.size()==0){
            return null;
        }
        return blocks.get(0);
    }
}
