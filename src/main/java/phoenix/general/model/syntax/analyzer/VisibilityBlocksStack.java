package phoenix.general.model.syntax.analyzer;

import phoenix.general.model.entities.Grammar;
import phoenix.general.model.entities.NonTerminal;
import phoenix.general.model.entities.VisibilityBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public void push(NonTerminal nonTerminal) {
        boolean isTermAxiom = nonTerminal.isAxiomOf(visibilityBlocks.peek());
        boolean isTermInNewBlock =!nonTerminal.getCurrBlock().equals(visibilityBlocks.peek());
        if (isTermAxiom) {
            visibilityBlocks.pop();
        }
        else if(isTermInNewBlock){
            visibilityBlocks.push(nonTerminal.getCurrBlock());
        }
    }

    public void push(String block){
        visibilityBlocks.push(block);
    }

    public String pop() {
        return visibilityBlocks.pop();
    }

    @Override
    public String toString() {
        return visibilityBlocks.toString();
    }

}
