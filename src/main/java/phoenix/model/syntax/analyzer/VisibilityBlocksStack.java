package phoenix.model.syntax.analyzer;

import phoenix.model.grammar.Grammar;
import phoenix.model.grammar.entities.VisibilityBlock;

import java.util.Stack;

public class VisibilityBlocksStack {
    private Stack<VisibilityBlock> visibilityBlocks;

    VisibilityBlocksStack(VisibilityBlock global) {
        visibilityBlocks = new Stack<>();
        visibilityBlocks.push(global);
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
