package phoenix.model.grammar.entities;

public class Boundary {
    private Terminal before;
    private Terminal after;

    public Boundary(Terminal before, Terminal after) {
        this.before = before;
        this.after = after;
    }

    public Terminal getBefore() {
        return before;
    }

    public Terminal getAfter() {
        return after;
    }

    @Override
    public String toString() {
        return  before + " : " + after;
    }
}
