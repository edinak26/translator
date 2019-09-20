package phoenix.model.syntax.analyzer;

import phoenix.model.grammar.entities.*;

import java.util.*;
import java.util.stream.Collectors;

public class NonTerminalChooser {
    private Boundary boundary;

    private List<VisibilityBlock> searchedBlocks;
    private List<BoundaryCondition> searchedCondition;

    public NonTerminalChooser(Boundary boundary) {
        this.boundary = boundary;
    }

    public List<NonTerminal> choose(List<NonTerminal> nonTerminals) {
        List<NonTerminal> result = nonTerminals;
        result = blockFilter(result);
        if (isChosen(result)) {
            return result;
        }
        return blockUsagesFilter(result);
    }

    private List<NonTerminal> blockFilter(List<NonTerminal> result) {
        return result.stream()
                .filter(this::isFit).collect(Collectors.toList());
    }

    private boolean isFit(NonTerminal nonTerminal) {
        BoundaryCondition condition = new BoundaryCondition(nonTerminal, nonTerminal.getBlock().getRules());
        //System.out.println(nonTerminal+"----"+condition);
        return condition.match(boundary);
    }


    private boolean isChosen(List<NonTerminal> result) {
        return result.size() == 1;
    }


    private List<NonTerminal> blockUsagesFilter(List<NonTerminal> nonTerminals) {
        List<NonTerminal> result = new ArrayList<>(nonTerminals);
        for (NonTerminal nonTerminal : nonTerminals) {
            BoundaryCondition condition = new BoundaryCondition(nonTerminal, nonTerminal.getBlock().getRules());
//            System.out.println( "___"+nonTerminal);
//            System.out.println(condition);
//            System.out.println( "___"+nonTerminal);
            searchedCondition= new ArrayList<>();
            searchedBlocks= new ArrayList<>();
            if (!matchUsages(nonTerminal.getBlock(), condition)) {
                result.remove(nonTerminal);
            }
        }
        return result;
    }

    private boolean matchUsages(VisibilityBlock block, BoundaryCondition condition) {
        //System.out.println( block + "|||"+condition);
        Usages usages = block.getUsages();
        //System.out.println("Start condition"+ condition);
        //System.out.println("Usages"+ usages);
        //System.out.println(searchedBlocks.contains(block));
        //System.out.println(searchedCondition.contains(condition));
        if (usages.size() != 0&&!isSearched(block, condition)) {
            searchedCondition.add(condition);
            searchedBlocks.add(block);
            for (Map.Entry<VisibilityBlock, BoundaryCondition> entry : usages.entrySet()) {
                VisibilityBlock nextBlock = entry.getKey();
                BoundaryCondition usageCondition = entry.getValue();
                //System.out.println("before join:"+condition);
                //System.out.println("join wirh: "+usageCondition);

                BoundaryCondition joinCondition = condition.join(usageCondition);
                //System.out.println("join result: "+joinCondition);
                //System.out.println(condition.equals(joinCondition));
//
//                System.out.println("NB: "+nextBlock);
//                System.out.println(joinCondition);
//                System.out.println("NB: "+nextBlock);
//                System.out.println("Usage:"+usageCondition);
                //System.out.println("join: "+ usageCondition + "join");
                //System.out.println("joined::::"+joinCondition);
                if (matchUsages(nextBlock, joinCondition)) {
                    return true;
                }
            }
        } else {
//            //condition.end();
//            System.out.println("(((("+condition);
//            System.out.println(boundary);
//            System.out.println(condition.match(boundary)+"))))");
            return condition.match(boundary);
        }
        return false;
    }

    private boolean isSearched(VisibilityBlock block, BoundaryCondition condition) {
        for(int i=0;i<searchedCondition.size();i++){
            if(searchedCondition.get(i).equals(condition)&&searchedBlocks.get(i).equals(block))
                return true;
        }
        return false;
//        return searchedBlocks.contains(block)
//                &&searchedCondition.contains(condition);
        }
}
