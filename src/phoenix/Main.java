package phoenix;

import phoenix.general.model.Model;
import phoenix.general.model.lexical.analyzer.LexicalAnalyzer;
import phoenix.general.view.View;

public class Main {
    public static void main(String[] args) throws Exception{
        Model model = new Model();
        View view = new View();
        //Controller controller = new Controller(model,view);
        //controller.run();
        (new LexicalAnalyzer()).analyse();

    }
}
