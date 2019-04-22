package phoenix.general.model.syntax.analyzer;

import phoenix.general.model.grammar.Grammar;
import phoenix.general.model.entities.VisibilityBlock;

import java.util.Stack;

public class VisibilityBlocksStack {
    private Stack<VisibilityBlock> visibilityBlocks;
    private Grammar grammar;

    VisibilityBlocksStack(Grammar grammar) {
        this.grammar = grammar;
        visibilityBlocks = new Stack<>();
        visibilityBlocks.push(grammar.getStartVisibilityBlocks());
    }

    public VisibilityBlock getCurrentBlock() {
        return visibilityBlocks.peek();
    }

    public Stack<VisibilityBlock> getBlocks() {
        Stack<VisibilityBlock> blocks = new Stack<>();
        blocks.addAll(visibilityBlocks);
        return blocks;
    }

    public void push(VisibilityBlock block){
        visibilityBlocks.push(block);
    }

    public VisibilityBlock peek(){ return visibilityBlocks.peek();}

    public VisibilityBlock pop() {
        return visibilityBlocks.pop();
    }

    @Override
    public String toString() {
        return visibilityBlocks.toString();
    }

}
