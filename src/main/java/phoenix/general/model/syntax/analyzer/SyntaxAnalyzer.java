package phoenix.general.model.syntax.analyzer;

import phoenix.general.model.lexical.analyzer.TablesManager;

import java.util.Stack;

public class SyntaxAnalyzer {
    Stack<String> stack;
    TablesManager tables;

    SyntaxAnalyzer(TablesManager tables){
        stack = new Stack<>();
        this.tables = tables;
    }

    
}
