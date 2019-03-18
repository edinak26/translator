package phoenix.general.controller;

import phoenix.general.model.Model;
import phoenix.general.view.View;

public class Controller {
    private Model model;
    private View view;
    public Controller(Model model, View view){
        this.model = model;
        this.view = view;
    }
}
