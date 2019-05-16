package phoenix.model.grammar.entities;

import phoenix.model.grammar.searcher.SetsSearcher;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static phoenix.interfaces.Characters.END_TERMINAL;

public class LocalBoundaryCondition {
    private Set<Terminal> before;
    private Set<Terminal> after;

    public LocalBoundaryCondition(NonTerminal nonTerminal, Rules rules) {
        before = SetsSearcher.getBeforePlus(rules, nonTerminal);
        after = SetsSearcher.getAfterPlus(rules, nonTerminal);
    }

    public LocalBoundaryCondition(Set<Terminal> before, Set<Terminal> after) {
        this.before = new HashSet<>(before);
        this.after = new HashSet<>(after);
    }

    public LocalBoundaryCondition(Terminal before, Set<Terminal> after) {
        this.before = new HashSet<>(Collections.singletonList(before));
        this.after = new HashSet<>(after);
    }

    public boolean match(Boundary boundary){
        boolean matchBefore = matchSet(boundary.getBefore(),before);
        boolean matchAfter = matchSet(boundary.getAfter(),after);
        return matchBefore&&matchAfter;
    }

    private boolean matchSet(Terminal terminal, Set<Terminal> set){
        boolean hasTerminal = set.contains(terminal);
        boolean canHaveTerminal = set.contains(null);
        return hasTerminal || canHaveTerminal;
    }

    @Override
    public String toString() {
        return "before=" + before +
                "\nafter=" + after + "\n";
    }

    public LocalBoundaryCondition join(LocalBoundaryCondition usageCondition) {
        LocalBoundaryCondition result = new LocalBoundaryCondition(before,after);
        result.before.addAll(usageCondition.before);
        result.after.addAll(usageCondition.after);
        return result;
    }

    public void end() {
        replaceNull(before);
        replaceNull(after);
    }

    public void replaceNull(Set<Terminal> set){
        set.remove(null);
        set.add(new Terminal (END_TERMINAL));
    }

    public boolean isSameAfter(LocalBoundaryCondition newCondition) {
        return after.equals(newCondition.after);
    }

    public LocalBoundaryCondition beforeJoin(LocalBoundaryCondition joinCondition) {
        LocalBoundaryCondition result = new LocalBoundaryCondition(before,after);
        result.before.addAll(joinCondition.before);
        return result;
    }

    public LocalBoundaryCondition afterJoin(LocalBoundaryCondition joinCondition) {
        LocalBoundaryCondition result = new LocalBoundaryCondition(before,after);
        result.after.addAll(joinCondition.after);
        return result;
    }

    public boolean isAfterFinished() {
        return !after.contains(null);
    }

    public boolean isBeforeFinished() {

        return !before.contains(null);
    }

    public LocalBoundaryCondition innerJoin(LocalBoundaryCondition localUsageCondition) {
        if(!isAfterFinished()&&!isBeforeFinished()){//TODO REMOVE check
            throw new RuntimeException("2 null in condition exception: "+ this);
        }
        if(!isAfterFinished()){
            return afterJoin(localUsageCondition);
        }
        if(!isBeforeFinished()){
            return beforeJoin(localUsageCondition);
        }
        return this;
    }

    public boolean isFinished() {
        return isAfterFinished()&&isBeforeFinished();
    }
}
