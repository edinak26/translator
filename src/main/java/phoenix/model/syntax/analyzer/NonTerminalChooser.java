package phoenix.model.syntax.analyzer;

import phoenix.model.grammar.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NonTerminalChooser {
    private Boundary boundary;

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
        System.out.println("+++++"+nonTerminal);
        BoundaryCondition condition = new BoundaryCondition(nonTerminal, nonTerminal.getBlock().getRules());
        System.out.println(condition);
        return condition.match(boundary);
    }


    private boolean isChosen(List<NonTerminal> result) {
        return result.size() == 1;
    }


    private List<NonTerminal> blockUsagesFilter(List<NonTerminal> nonTerminals) {
        List<NonTerminal> result = new ArrayList<>(nonTerminals);
        for (NonTerminal nonTerminal : nonTerminals) {
            BoundaryCondition condition = new BoundaryCondition(nonTerminal, nonTerminal.getBlock().getRules());
            if (!matchUsages(nonTerminal.getBlock(), condition)) {
                result.remove(nonTerminal);
            }
        }
        return result;
    }

    private boolean matchUsages(VisibilityBlock block, BoundaryCondition condition) {
        Usages usages = block.getUsages();
        if (usages.size() != 0) {
            for (Map.Entry<VisibilityBlock, BoundaryCondition> entry : usages.entrySet()) {
                VisibilityBlock nextBlock = entry.getKey();
                BoundaryCondition usageCondition = entry.getValue();
                BoundaryCondition joinCondition = condition.join(usageCondition);
                if (matchUsages(nextBlock, joinCondition)) {
                    return true;
                }
            }
        } else {
            condition.end();
            return condition.match(boundary);
        }
        return false;
    }
}
