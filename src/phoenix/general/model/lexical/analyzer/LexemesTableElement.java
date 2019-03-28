package phoenix.general.model.lexical.analyzer;

public class LexemesTableElement {
    String name;
    int lineNum;
    int lineLexNum;
    String specType;

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
}

