package phoenix.controller;

import phoenix.model.Model;
import phoenix.model.executor.Executor;
import phoenix.model.lexical.analyzer.LexicalAnalyzer;
import phoenix.model.syntax.analyzer.SyntaxAnalyzer;
import phoenix.view.View;

public class Controller {
    private Model model;
    private View view;
    LexicalAnalyzer lexicalAnalyzer;
    SyntaxAnalyzer syntaxAnalyzer;
    Executor executor;
    public Controller(Model model, View view){
        this.model = model;
        this.view = view;
    }
    public void run() throws Exception{
        lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.analyse();
        lexicalAnalyzer.getTables().show();
        syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer.getTables());
        executor = new Executor(syntaxAnalyzer.analyze());
        executor.setTables(lexicalAnalyzer.getTables());
        executor.execute();
    }
}
