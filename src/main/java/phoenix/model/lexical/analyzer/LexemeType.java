package phoenix.model.lexical.analyzer;

public class LexemeType {
    private String name;
    private int index;
    private static int newLexemeIndex=1;
    LexemeType(String name){
        this.name = name;
        this.index = newLexemeIndex++;
    }
    public String getName(){
        return name;
    }

    public int getIndex() {
        return index;
    }
}
