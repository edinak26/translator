package phoenix.controller;

import phoenix.model.Model;
import phoenix.model.lexical.analyzer.LexicalAnalyzer;
import phoenix.model.syntax.analyzer.SyntaxAnalyzer;
import phoenix.view.View;

public class Controller {
    private Model model;
    private View view;
    LexicalAnalyzer lexicalAnalyzer;
    SyntaxAnalyzer syntaxAnalyzer;
    public Controller(Model model, View view){
        this.model = model;
        this.view = view;
    }
    public void run() throws Exception{
        //view.run();
        lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.analyse();
        syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer.getTables());

            syntaxAnalyzer.analyze();
        //view.run();
    }
}
