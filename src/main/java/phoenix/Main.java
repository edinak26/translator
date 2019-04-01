package phoenix;

import phoenix.general.controller.Controller;
import phoenix.general.model.Model;
import phoenix.general.model.lexical.analyzer.LexicalAnalyzer;
import phoenix.general.model.reader.TextReader;
import phoenix.general.model.syntax.analyzer.RelationsTable;
import phoenix.general.view.View;

public class Main {
    public static void main(String[] args) throws Exception{
        Model model = new Model();
        RelationsTable relTable = new RelationsTable(TextReader.grammar()
                .setPath("D:\\University\\Java\\translator\\src\\main\\java\\phoenix\\accessory\\info\\stratGram").get());
        View.setRel(relTable.getRelations());
        View.setTypes(relTable.getTerms());
        View view = new View();
        Controller controller = new Controller(model,view);
        controller.run();
        (new LexicalAnalyzer()).analyse();


    }
}
