package phoenix;

import phoenix.controller.Controller;
import phoenix.model.Model;
import phoenix.view.View;

public class Main {
    public static void main(String[] args) throws Exception{
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(model,view);
        controller.run();
    }
}
