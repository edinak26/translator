package phoenix.model.grammar.constructor;

import phoenix.model.grammar.entities.NonTerminal;
import phoenix.model.grammar.entities.VisibilityBlock;

public class UsagesConstructor {

    static void construct(VisibilityBlock block) {
        (new UsagesConstructor()).constructUsages(block);
    }

    private void constructUsages(VisibilityBlock block) {
        constructSameVisibilityUsages(block, block.getAxiom());
        for (VisibilityBlock child : block.getChildren()) {
            UsagesConstructor.construct(child);
        }
    }

    private void constructSameVisibilityUsages(VisibilityBlock block, NonTerminal axiom) {
        if (block.hasParent()) {
            constructUsage(block.getParent(), axiom);
            for (VisibilityBlock usageOption : block.getParent().getChildren()) {
                if(!usageOption.equals(block)) {
                    constructUsage(usageOption, axiom);
                    constructChildrenUsages(usageOption, axiom);
                }
            }
        }
    }

    private void constructChildrenUsages(VisibilityBlock block, NonTerminal axiom) {
        for (VisibilityBlock usageOption : block.getChildren()) {
            constructUsage(usageOption, axiom);
            constructChildrenUsages(usageOption, axiom);
        }
    }

    private void constructUsage(VisibilityBlock block, NonTerminal axiom) {
        if (block.contains(axiom)) {
            axiom.getBlock().addUsage(block);
        }
    }
}
