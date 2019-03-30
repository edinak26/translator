package src.main.java.phoenix.general.controller;

import src.main.java.phoenix.general.model.Model;
import src.main.java.phoenix.general.view.View;

public class Controller {
    private Model model;
    private View view;
    public Controller(Model model, View view){
        this.model = model;
        this.view = view;
    }
    public void run(){
        view.run();
    }
}
