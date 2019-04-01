package phoenix.general.model.lexical.analyzer;

public class LexemesTableElement {
    String name;
    int lineNum;
    int lineLexNum;
    String specType;

    public LexemesTableElement() {
        name = "#";
    }

    public LexemesTableElement setName(String name) {
        this.name = name;
        return this;
    }

    public LexemesTableElement setLineNum(int lineNum) {
        this.lineNum = lineNum;
        return this;
    }

    public LexemesTableElement setLineLexNum(int lineLexNum) {
        this.lineLexNum = lineLexNum;
        return this;
    }

    public LexemesTableElement setSpecType(String specType) {
        this.specType = specType;
        return this;
    }

    public String name() {
        return name;
    }

    public int getLineNum() {
        return lineNum;
    }

    public int getLineLexNum() {
        return lineLexNum;
    }

    public String getSpecType() {
        return specType;
    }

    public String lexeme() {
        if(specType!=null)
            return specType;
        return name;
    }
}

