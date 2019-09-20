package phoenix.model.grammar.entities;

import java.util.Objects;

public class WideTerminal {
    private Terminal terminal;
    private Terminal before;
    private Terminal after;
    private NonTerminal nonTerminal;


    public WideTerminal(Terminal terminal, Terminal before, Terminal after) {
        this.terminal = terminal;
        this.before = before;
        this.after = after;
    }

    public Terminal getBefore() {
        return before;
    }

    public Terminal getAfter() {
        return after;
    }

    public boolean isFinal() {
        return !isBeforeNotFinal() && !isAfterNotFinal();
    }

    public boolean isBeforeNotFinal() {
        return before == null;
    }

    public boolean isAfterNotFinal() {
        return after == null;
    }

    public void setNonTerminal(NonTerminal nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public NonTerminal getNonTerminal() {
        return nonTerminal;
    }

    @Override
    public String toString() {
        return before + " | " + terminal + " | " + after + " in " + nonTerminal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WideTerminal that = (WideTerminal) o;
        return terminal.equals(that.terminal) &&
                Objects.equals(before, that.before) &&
                Objects.equals(after, that.after) &&
                nonTerminal.equals(that.nonTerminal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terminal, before, after, nonTerminal);
    }
}
