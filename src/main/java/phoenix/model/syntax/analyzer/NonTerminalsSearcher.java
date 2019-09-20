package phoenix.model.syntax.analyzer;

import phoenix.exceptions.GrammarException;
import phoenix.model.grammar.entities.NonTerminal;
import phoenix.model.grammar.entities.Replace;
import phoenix.model.grammar.entities.Terminal;
import phoenix.model.grammar.entities.VisibilityBlock;

import java.util.ArrayList;
import java.util.List;

public class NonTerminalsSearcher {
    private Replace replace;
    private VisibilityBlock block;
    private List<NonTerminal> result = new ArrayList<>();

    public NonTerminalsSearcher(VisibilityBlock block, Replace replace) {
        this.replace = replace;
        this.block = block;
    }

    public static List<NonTerminal> getNonTerminal(VisibilityBlock block, Replace replace){
        return (new NonTerminalsSearcher(block,replace)).search();
    }

    private List<NonTerminal> search(){
        findBlockNonTerminal(block);
        //if(isFound())
            //return result;
        findChildrenNonTerminals(block);
        //if(isFound()){
            //return result;
        //}
        findParentNonTerminals(block);
        if(isFound()){
            return result;
        }
        throw new GrammarException("NonTerminal not found for "+ replace + " in " +block);
    }


    private void findBlockNonTerminal(VisibilityBlock block) {
        List<NonTerminal> blockNonTerminals = block.getNonTerminals(replace);
        if (blockNonTerminals.size()>0) {
            result.addAll(blockNonTerminals);
        }
    }

    private void findChildrenNonTerminals(VisibilityBlock block) {
        for (VisibilityBlock child : block.getChildren()) {
            List<NonTerminal> childNonTerminals = child.getNonTerminals(replace);
            if (childNonTerminals.size()>0) {
                result.addAll(childNonTerminals);
            } else {
                findChildrenNonTerminals(child);
            }
        }
    }

    private void findParentNonTerminals(VisibilityBlock block) {
        VisibilityBlock parent = block.getParent();
        if (parent != null) {
            findBlockNonTerminal(parent);
            for (VisibilityBlock neighbourBlock : parent.getChildren()) {
                if (!neighbourBlock.equals(block)) {
                    findBlockNonTerminal(neighbourBlock);
                    findChildrenNonTerminals(neighbourBlock);
                }
            }
            findParentNonTerminals(parent);
        }
    }


    private boolean isFound() {
        return result.size()>0;
    }
}
