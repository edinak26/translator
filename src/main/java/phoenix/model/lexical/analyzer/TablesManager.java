package phoenix.model.lexical.analyzer;

import java.util.ArrayList;
import java.util.List;

public class TablesManager {
    private List<LexemesTableElement> lexemesTable;
    private CodeText code;
    private int index=-1;

    public TablesManager(CodeText code) {
        lexemesTable = new ArrayList<>();
        this.code = code;
    }

    public void addCurLexeme() {
        LexemesTableElement elem = new LexemesTableElement();
        elem.setName(code.getCurLexeme());
        elem.setLineNum(code.getLineNum());
        elem.setLineLexNum(code.getLineLexemesSize());
        elem.setSpecType(null);
        lexemesTable.add(elem);
    }

    public void addCurLexeme(String specType) {
        LexemesTableElement elem = new LexemesTableElement();
        elem.setName(code.getCurLexeme());
        elem.setLineNum(code.getLineNum());
        elem.setLineLexNum(code.getLexemeNum());
        elem.setSpecType(specType);
        lexemesTable.add(elem);
    }

    public boolean hasNext(){
        return index<lexemesTable.size()-1;
    }
    public LexemesTableElement get(){
        return lexemesTable.get(index);
    }

    public void goNext(){
        index++;
    }

    public void goTo(int index) { this.index=index;}

    public void show() {
        String text;
        for (LexemesTableElement elem : lexemesTable) {
            text = String.format("%-15s %-5d %-5d %-10s\n",
                    elem.name, elem.lineNum + 1, elem.lineLexNum + 1, elem.specType);
            System.out.print(text);
        }
    }

    public List<String> getIdentifiers(){
        List<String> result = new ArrayList<>();
        for(LexemesTableElement elem: lexemesTable){
            if("IDN".equals(elem.specType)){
                result.add(elem.name);
            }
        }
        return result;
    }

    public void goBack() {
        index--;
    }

    public int getIndex() {
        return index;

    }

    public List<String> getConstants() {
        List<String> result = new ArrayList<>();
        for(LexemesTableElement elem: lexemesTable){
            if("CON".equals(elem.specType)){
                result.add(elem.name);
            }
        }
        return result;
    }

    public List<String> getPointers() {
        List<String> result = new ArrayList<>();
        for(LexemesTableElement elem: lexemesTable){
            if("POINTER".equals(elem.specType)){
                result.add(elem.name);
            }
        }
        return result;
    }
}
