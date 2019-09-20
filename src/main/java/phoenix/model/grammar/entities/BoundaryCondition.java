package phoenix.model.grammar.entities;

import phoenix.model.grammar.boundary.BoundaryConditionConstructor;
import phoenix.model.grammar.searcher.SetsSearcher;

import java.util.*;

import static phoenix.interfaces.Characters.END_TERMINAL;

public class BoundaryCondition {
    private Set<LocalBoundaryCondition> localConditions = new HashSet<>();

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

    public BoundaryCondition(Set<LocalBoundaryCondition> localConditions) {
        this.localConditions = new HashSet<>(localConditions);
    }

    public void createConditions(NonTerminal nonTerminal, Rules rules) {
        //System.out.println("help meeee" + nonTerminal+"|"+rules);
        BoundaryConditionConstructor conditionConstructor = new BoundaryConditionConstructor(nonTerminal,rules);
        conditionConstructor.isFirst=true;
        localConditions = conditionConstructor.construct();
        //System.out.println("yep? again"+localConditions);
        localConditions.forEach(LocalBoundaryCondition::expand);
        System.out.println("for"+nonTerminal+" Result:"+localConditions);
        /*Set<Terminal> before = SetsSearcher.getBeforePlus(rules, nonTerminal);
        for (Terminal beforeTerminal : before) {
            System.out.println(beforeTerminal);
            Set<Terminal> afterTerminals = SetsSearcher.getAfterMinus(beforeTerminal, rules, nonTerminal);
            System.out.println(afterTerminals);
            addCondition(new LocalBoundaryCondition(beforeTerminal, afterTerminals));
        }*/
    }

    public void addCondition(LocalBoundaryCondition newCondition) {
        //System.out.println("ERROR PLACE" + localConditions+"NEW" + newCondition);
        //if (!addSame(newCondition)) {
            localConditions.add(newCondition);
        //}
        //System.out.println("END"+ localConditions);
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
        //System.out.println("START"+localConditions);
        BoundaryCondition result = new BoundaryCondition(localConditions);
        for (LocalBoundaryCondition condition : localConditions) {
            if (!condition.isFinished()&&usageCondition.localConditions.size()>0) {
                result.removeCondition(condition);
                for (LocalBoundaryCondition localUsageCondition : usageCondition.localConditions) {
                    result.addCondition(condition.join(localUsageCondition));
                }
            }

        }
        //System.out.println("END"+result);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundaryCondition condition = (BoundaryCondition) o;
        return Objects.equals(localConditions, condition.localConditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localConditions);
    }
}
