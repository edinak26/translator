package phoenix.general.controller;

import phoenix.general.model.Model;
import phoenix.general.model.lexical.analyzer.LexicalAnalyzer;
import phoenix.general.model.syntax.analyzer.SyntaxAnalyzer;
import phoenix.general.view.View;

import java.io.File;

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
