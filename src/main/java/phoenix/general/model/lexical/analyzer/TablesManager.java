package phoenix.general.model.lexical.analyzer;

import java.util.ArrayList;
import java.util.List;

public class TablesManager {
    private List<LexemesTableElement> lexemesTable;
    private CodeText code;
    public TablesManager(CodeText code){
        lexemesTable = new ArrayList<>();
        this.code = code;
    }
    public void addCurLexeme(){
        LexemesTableElement elem = new LexemesTableElement();
        elem.setName(code.getCurLexeme());
        elem.setLineNum(code.getLineNum());
        elem.setLineLexNum(code.getLineLexemesSize());
        elem.setSpecType(null);
        lexemesTable.add(elem);
    }
    public void addCurLexeme(String specType){
        LexemesTableElement elem = new LexemesTableElement();
        elem.setName(code.getCurLexeme());
        elem.setLineNum(code.getLineNum());
        elem.setLineLexNum(code.getLexemeNum());
        elem.setSpecType(specType);
        lexemesTable.add(elem);
    }
    public void show(){
        String text;
        for (LexemesTableElement elem : lexemesTable) {
            text = String.format("%-15s %-5d %-5d %-10s\n",
                    elem.name, elem.lineNum+1, elem.lineLexNum+1, elem.specType);
            System.out.print(text);
        }
    }
}
