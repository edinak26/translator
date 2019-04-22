package phoenix.general.model.entities;

import java.util.Objects;

public class Terminal {
    private String name;

    public Terminal(String name) {
        this.name = name;
    }

    public static Terminal create(String name) {
        return new Terminal(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return hashCode()==o.hashCode();
    }

    public boolean equals(Terminal terminal) {
        return terminal.getName().equals(name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

