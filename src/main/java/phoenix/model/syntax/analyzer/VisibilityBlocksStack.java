package phoenix.model.syntax.analyzer;

import phoenix.model.grammar.Grammar;
import phoenix.model.grammar.entities.VisibilityBlock;

import java.util.Stack;

public class VisibilityBlocksStack {
    private Stack<VisibilityBlock> visibilityBlocks;
    private VisibilityBlock global;

    VisibilityBlocksStack(VisibilityBlock global) {
        visibilityBlocks = new Stack<>();
        visibilityBlocks.push(global);
        this.global = global;
    }

    public void push(VisibilityBlock block) {
        visibilityBlocks.push(block);
    }

    public VisibilityBlock peek() {
        if (visibilityBlocks.isEmpty())
            return global;
        return visibilityBlocks.peek();
    }

    public VisibilityBlock pop() {
        if (visibilityBlocks.isEmpty())
            return global;
        return visibilityBlocks.pop();
    }

    @Override
    public String toString() {
        return visibilityBlocks.toString();
    }

}
