package phoenix.model.grammar.entities;

import phoenix.model.grammar.searcher.SetsSearcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static phoenix.interfaces.Characters.END_TERMINAL;

public class BoundaryCondition {
    private List<LocalBoundaryCondition> localConditions = new ArrayList<>();

    public BoundaryCondition(NonTerminal nonTerminal, Rules rules) {
        createConditions(nonTerminal, rules);
        if(localConditions.size()==0){
            Set<Terminal>before = new HashSet<>();
            Set<Terminal>after = new HashSet<>();
            before.add(null);
            after.add(null);
            localConditions.add(new LocalBoundaryCondition(before,after));
        }
    }

    public BoundaryCondition(List<LocalBoundaryCondition> localConditions) {
        this.localConditions = new ArrayList<>(localConditions);
    }

    public void createConditions(NonTerminal nonTerminal, Rules rules) {
        Set<Terminal> before = SetsSearcher.getBeforePlus(rules, nonTerminal);
        for (Terminal beforeTerminal : before) {
            System.out.println(beforeTerminal);
            Set<Terminal> afterTerminals = SetsSearcher.getAfterMinus(beforeTerminal, rules, nonTerminal);
            System.out.println(afterTerminals);
            addCondition(new LocalBoundaryCondition(beforeTerminal, afterTerminals));
        }
    }

    public void addCondition(LocalBoundaryCondition newCondition) {
        if (!addSame(newCondition)) {
            localConditions.add(newCondition);
        }
    }

    public boolean addSame(LocalBoundaryCondition newCondition) {
        for (LocalBoundaryCondition condition : localConditions) {
            if (condition.isSameAfter(newCondition)) {
                localConditions.remove(condition);
                condition = condition.beforeJoin(newCondition);
                localConditions.add(condition);
                return true;
            }
        }
        return false;
    }

    public boolean match(Boundary boundary) {
        for (LocalBoundaryCondition condition : localConditions) {
            if (condition.match(boundary))
                return true;
        }
        return false;
    }

    private boolean matchSet(Terminal terminal, Set<Terminal> set) {
        boolean hasTerminal = set.contains(terminal);
        boolean canHaveTerminal = set.contains(null);
        return hasTerminal || canHaveTerminal;
    }

    public BoundaryCondition join(BoundaryCondition usageCondition) {
        BoundaryCondition result = new BoundaryCondition(localConditions);
        for (LocalBoundaryCondition condition : localConditions) {
            if (!condition.isFinished()) {
                result.removeCondition(condition);
                for (LocalBoundaryCondition localUsageCondition : usageCondition.localConditions) {
                    result.addCondition(condition.innerJoin(localUsageCondition));
                }
            }

        }
        return result;
    }

    private void removeCondition(LocalBoundaryCondition condition) {
        localConditions.remove(condition);
    }

    public void end() {
        localConditions.forEach(LocalBoundaryCondition::end);
    }

    @Override
    public String toString() {
        return localConditions.toString();
    }
}
