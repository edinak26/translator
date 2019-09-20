package phoenix.model.grammar.entities;

public class Terminal {
    private String name;

    public Terminal(){
        name = "#";
    }

    public Terminal(String name) {
        this.name = name;
    }

    public Terminal(Terminal terminal) {
        this.name=terminal.name;
    }

    public String getName() {
        return name;
    }

    public boolean isAxiom(){
        if(this instanceof NonTerminal){
            return ((NonTerminal) this).getBlock().isAxiom((NonTerminal)this);
        }
        return false;
    }

    @Override
    public String toString() {
        return name;//+"\\"+(this instanceof NonTerminal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Terminal){
            return name.equals(((Terminal) o).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

