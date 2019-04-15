package phoenix.general.model.syntax.analyzer;

import phoenix.general.model.entities.Terminal;
import phoenix.general.model.lexical.analyzer.TablesManager;

import java.util.ArrayList;
import java.util.List;

public class TerminalsTable {
    private List<Terminal> terminalsTable;

    public TerminalsTable(TablesManager tables){
        terminalsTable = new ArrayList<>();
        while(tables.hasNext()){
            terminalsTable.add(new Terminal(tables.get().name()));
            tables.goNext();
        }
    }

    public Terminal peek(){
        return terminalsTable.get(0);
    }

    public Terminal pop(){
        return terminalsTable.remove(0);
    }
}
