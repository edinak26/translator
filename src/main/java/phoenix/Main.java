package phoenix;

import phoenix.controller.Controller;
import phoenix.model.Model;
import phoenix.view.View;

public class Main {
    public static void main(String[] args) throws Exception{
        Model model = new Model();
        //RelationsTable relTable = new RelationsTable();
        //View.setRel(relTable.getRelations());
       // View.setTypes(relTable.getTerms());
        View view = new View();
        Controller controller = new Controller(model,view);
        controller.run();

        /*class A {
            protected String name;
            A(String name){

                this.name=name;
            }
            public void show(){
                System.out.println(name);
            }

            public void setName(String name) {
                this.name = name;
            }
        }
        class B extends A{
            B(A a){
                super(a.name);
            }
        }
        A a1 = new A("nameA");
        A a2 = a1;
        a1 = new B(a1);
        a1.setName("nameB");
        a1.show();
        a2.show();*/
    }
}
