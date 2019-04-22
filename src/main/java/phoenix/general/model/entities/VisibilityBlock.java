package phoenix.general.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VisibilityBlock {
    private String name;
    private List<VisibilityBlock> children;
    private NonTerminal axiom;

    public VisibilityBlock(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public VisibilityBlock(String name, List<VisibilityBlock> children) {
        this.name = name;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void addChild(VisibilityBlock block) {
        children.add(block);
    }

    public boolean hasChildren() {
        return children != null;
    }

    public void showChildren(){
        System.out.println(children.toString());
    }

    public boolean has(VisibilityBlock block){
        return this.equals(block)||children.stream().anyMatch(child->child.has(block));
    }

    public void axiom(NonTerminal nonTerminal) {
        if (hasAxiom()) {
            throw new RuntimeException("Exception: visibility block already has axiom");
        }
        axiom = nonTerminal;
    }

    public boolean hasAxiom() {
        return axiom != null;
    }

    public NonTerminal getAxiom(){
        return axiom;
    }


    public boolean isChild(VisibilityBlock block) {
        for (VisibilityBlock child : children) {
            if (block.equals(child)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean equals(VisibilityBlock block) {
        return name.equals(block.name);
    }
}
