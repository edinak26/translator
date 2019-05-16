package phoenix.model.grammar.entities;

import java.util.ArrayList;
import java.util.List;

public class VisibilityBlock {
    private static final String GLOBAL_BLOCK_NAME = "<::>";
    private String name;
    private VisibilityBlock parent;
    private List<VisibilityBlock> children;
    private NonTerminal axiom;
    private Rules rules = new Rules();
    private Usages usages = new Usages();

    public VisibilityBlock(){
        name = GLOBAL_BLOCK_NAME;
        children = new ArrayList<>();
    }

    public VisibilityBlock(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public VisibilityBlock(String name, List<VisibilityBlock> children) {
        this.name = name;
        this.children = children;
    }


    public void addChild(VisibilityBlock block) {
        children.add(block);
    }

    public String getName() {
        return name;
    }

    public void setAxiom(NonTerminal nonTerminal) {//TODO DELETE AXIOM METHODS
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof VisibilityBlock){
            return name.equals(((VisibilityBlock) o).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(VisibilityBlock block) {
        return name.equals(block.name);
    }

    public void show(){
        System.out.println(name);
        System.out.println("A->"+axiom);
        System.out.println("P->"+parent);
        System.out.println("C->"+children);
        rules.show();
        usages.show();
        children.forEach(VisibilityBlock::show);
    }

    public void setParent(VisibilityBlock parent) {
        this.parent=parent;
    }

    public boolean isAxiom(NonTerminal nonTerminal) {
        return axiom.equals(nonTerminal);
    }


    public void addRules(Rules rules) {
        this.rules.add(rules);
    }

    public List<VisibilityBlock> getChildren() {
        return children;
    }

    public boolean contains(Terminal terminal) {
        return rules.contains(terminal);
    }

    public void addUsage(VisibilityBlock searchBlock) {
        usages.addUsage(searchBlock,new BoundaryCondition(axiom,searchBlock.rules));
    }

    public VisibilityBlock getParent() {
        return parent;
    }

    public boolean hasParent() {
        return parent!=null;
    }

    public List<NonTerminal> getNonTerminals(Replace replace){
        return rules.getNonTerminals(replace);
    }

    public Rules getRules() {
        return rules;
    }

    public Usages getUsages() {
        return usages;
    }
}
