package phoenix;

import phoenix.general.model.Model;
import phoenix.general.model.lexical.analyzer.LexicalAnalyzer;
import phoenix.general.model.reader.TextReader;
import phoenix.general.model.syntax.analyzer.RelationsTable;
import phoenix.general.view.View;

public class Main {
    public static void main(String[] args) throws Exception{
        Model model = new Model();
        View view = new View();
        //Controller controller = new Controller(model,view);
        //controller.run();
        (new LexicalAnalyzer()).analyse();
        System.out.println((new RelationsTable(TextReader.grammar().get())).getLastPlus("<ініціалізація змінних>").toString());

    }
}
