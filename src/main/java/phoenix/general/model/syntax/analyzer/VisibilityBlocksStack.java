package phoenix.general.model.syntax.analyzer;

import phoenix.general.model.entities.Grammar;
import phoenix.general.model.entities.NonTerminal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class VisibilityBlocksStack {
    private Stack<String> visibilityBlocks;
    private Grammar grammar;

    VisibilityBlocksStack(Grammar grammar) {
        this.grammar = grammar;
        visibilityBlocks = new Stack<>();
        visibilityBlocks.push(grammar.getStartVisibilityBlocks());
    }

    public String getCurrentBlock() {
        return visibilityBlocks.peek();
    }

    public Stack<String> getBlocks() {
        Stack<String> blocks = new Stack<>();
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

    public String pop(String block) {
        return visibilityBlocks.pop();
    }

    @Override
    public String toString() {
        return visibilityBlocks.toString();
    }

}
