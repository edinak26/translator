package phoenix.model.grammar.entities;

import phoenix.model.grammar.searcher.SetsSearcher;

import java.util.*;

import static phoenix.interfaces.Characters.END_TERMINAL;

public class LocalBoundaryCondition {
    private Set<Terminal> before;
    private Set<Terminal> after;

    public LocalBoundaryCondition(WideTerminal wideTerminal) {
        before = new HashSet<>(Collections.singleton(wideTerminal.getBefore()));
        after = new HashSet<>(Collections.singleton(wideTerminal.getAfter()));
    }

    public LocalBoundaryCondition(){
        before = new HashSet<>(Collections.singleton(null));
        after = new HashSet<>(Collections.singleton(null));
    }

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

    public boolean match(Boundary boundary) {
        boolean matchBefore = matchSet(boundary.getBefore(), before);
        boolean matchAfter = matchSet(boundary.getAfter(), after);
        return matchBefore && matchAfter;
    }

    private boolean matchSet(Terminal terminal, Set<Terminal> set) {
        boolean hasTerminal = set.contains(terminal);
        boolean canHaveTerminal = set.contains(null);
        return hasTerminal || canHaveTerminal;
    }

    @Override
    public String toString() {
        return " b:" + before + " a: " + after + "\n";
    }

    public LocalBoundaryCondition join(LocalBoundaryCondition usageCondition) {
        LocalBoundaryCondition result = new LocalBoundaryCondition(before, after);
        //System.out.println("lbc: "+this+"\n join: "+usageCondition);
        if (!isBeforeFinished()) {
            result.before.remove(null);
            result.before.addAll(usageCondition.before);
        }
        if (!isAfterFinished()) {
            result.after.remove(null);
            result.after.addAll(usageCondition.after);
        }
        //System.out.println("result: "+result);
        return result;
    }

    public void end() {
        replaceNull(before);
        replaceNull(after);
    }

    public void replaceNull(Set<Terminal> set) {
        set.remove(null);
        set.add(new Terminal(END_TERMINAL));
    }

    public boolean isSameAfter(LocalBoundaryCondition newCondition) {
        return after.equals(newCondition.after);
    }

    public LocalBoundaryCondition beforeJoin(LocalBoundaryCondition joinCondition) {
        LocalBoundaryCondition result = new LocalBoundaryCondition(before, after);
        result.before.addAll(joinCondition.before);
        return result;
    }

    public LocalBoundaryCondition afterJoin(LocalBoundaryCondition joinCondition) {
        LocalBoundaryCondition result = new LocalBoundaryCondition(before, after);
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
        if (!isAfterFinished() && !isBeforeFinished()) {
            throw new RuntimeException("2 null in condition exception: " + this);
        }
        if (!isAfterFinished()) {
            return afterJoin(localUsageCondition);
        }
        if (!isBeforeFinished()) {
            return beforeJoin(localUsageCondition);
        }
        return this;
    }

    public boolean isFinished() {
        return isAfterFinished() && isBeforeFinished();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalBoundaryCondition condition = (LocalBoundaryCondition) o;
        if(this.before.size()==1&&condition.before.size()==1&&this.before.contains(null)&&condition.before.contains(null))
        if(this.after.size()==1&&condition.after.size()==1&&this.after.contains(null)&&condition.after.contains(null))
            return true;
        return Objects.equals(before, condition.before) &&
                Objects.equals(after, condition.after);
    }

    @Override
    public int hashCode() {

        return Objects.hash(before, after);
    }


    public void expand() {
        expandBefore();
        expandAfter();
    }

    private void expandBefore() {
        before.forEach(t->{
            if(t instanceof NonTerminal)
            before.addAll(SetsSearcher.getLastPlus(((NonTerminal)t).getBlock().getRules(),t));
        });
    }

    private void expandAfter() {
        after.forEach(t->{
            if(t instanceof NonTerminal)
                after.addAll(SetsSearcher.getFirstPlus(((NonTerminal)t).getBlock().getRules(),t));
        });
    }

}
