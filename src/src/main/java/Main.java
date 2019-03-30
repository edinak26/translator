import src.main.java.phoenix.general.controller.Controller;
import src.main.java.phoenix.general.model.Model;
import src.main.java.phoenix.general.model.lexical.analyzer.LexicalAnalyzer;
import src.main.java.phoenix.general.model.reader.TextReader;
import src.main.java.phoenix.general.model.syntax.analyzer.RelationsTable;
import src.main.java.phoenix.general.view.View;

public class Main {
    public static void main(String[] args) throws Exception{
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(model,view);
        controller.run();
        (new LexicalAnalyzer()).analyse();
        new RelationsTable(TextReader.grammar().get());

    }
}
