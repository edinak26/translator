package phoenix.general.model.entities;

public class Terminal {
    private String name;
    public Terminal(String name){
        this.name = name;
    }

    public static Terminal create(String name){
        return new Terminal(name);
    }

    public String getName() {
        return name;
    }


}
