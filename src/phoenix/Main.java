package phoenix;

import phoenix.general.controller.Controller;
import phoenix.general.model.CodeReader;
import phoenix.general.model.Model;
import phoenix.general.view.View;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(model,view);
        controller.run();
        System.out.println((new CodeReader()).getText());
    }
}
