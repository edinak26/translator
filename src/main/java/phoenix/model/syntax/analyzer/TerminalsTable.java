package phoenix.model.syntax.analyzer;

import phoenix.model.grammar.entities.Terminal;
import phoenix.model.lexical.analyzer.TablesManager;

import java.util.ArrayList;
import java.util.List;

public class TerminalsTable {
    private List<Terminal> terminalsTable;
    private TablesManager tables;

    public TerminalsTable(TablesManager tables) {
        this.tables = tables;
        terminalsTable = new ArrayList<>();
        while (tables.hasNext()) {
            tables.goNext();
            terminalsTable.add(new Terminal(tables.get().lexeme()));
        }
        tables.goTo(-1);
        for (int i = 0; i < terminalsTable.size() - 1; i++) {
            if (terminalsTable.get(i).getName().equals("{") && terminalsTable.get(i + 1).getName().equals("\\n")) {
                terminalsTable.set(i, new Terminal("{\\n"));
                terminalsTable.remove(i + 1);
            }
        }
    }

    public Terminal peek() {
        if (isEmpty())
            return new Terminal();
        return terminalsTable.get(0);
    }

    public Terminal pop() {
        if (isEmpty())
            return new Terminal();
        tables.goNext();
        return terminalsTable.remove(0);
    }

    public String getSpecTypeName() {
        if (tables.getIndex() > -1)
            if (tables.get().getSpecType() != null) {
                return tables.get().getSpecType();
            }
        return null;
    }

    public String getSpecTypeValue() {
        if (tables.getIndex() > -1)
            if (tables.get().getSpecType() != null) {
                return tables.get().name();
            }
        return null;
    }

    public boolean isEmpty() {
        return terminalsTable.isEmpty();
    }

    @Override
    public String toString() {
        return "TerminalsTable{" +
                "terminalsTable=" + terminalsTable +
                '}';
    }
}
