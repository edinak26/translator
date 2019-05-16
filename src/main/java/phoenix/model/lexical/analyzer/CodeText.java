package phoenix.model.lexical.analyzer;

import java.util.List;

public class CodeText {
    private List<List<String>> splitText;
    private int curLineNum = DEFAULT_START_INDEX ;
    private int curLexemeNum = LEXEMES_START_INDEX;

    private static final int DEFAULT_START_INDEX = 0;
    private  static final int LEXEMES_START_INDEX = -1;

    public CodeText(List<List<String>> splitText) {
        this.splitText = splitText;
    }

    public String getCurLexeme() {
        return getLexeme(curLineNum, curLexemeNum);
    }

    public String next(){
        goToNextLexeme();
        return getCurLexeme();
    }

    public void goToNextLexeme(){

        if(hasNextLineLexeme()){
            goToNextLineLexeme();
        }
        else {
            goToNextLine();
        }
    }

    public void goToNextLineLexeme() {
        if (!hasNextLineLexeme()) {
            throw new ArrayIndexOutOfBoundsException(
                    "CodeText out of range lexemes range: " + curLexemeNum + " size: " + getLineLexemesSize());
        }
        curLexemeNum++;
    }

    public void goToNextLine() {
        if (!hasNextLine()) {
            throw new ArrayIndexOutOfBoundsException(
                    "CodeText out of lines range: "+ curLineNum + " size: " + getLinesSize());
        }
        curLexemeNum = DEFAULT_START_INDEX;
        curLineNum++;
    }

    public boolean hasNextLine() {
        return curLineNum < getLinesSize()-1;
    }

    public boolean hasNextLineLexeme() {
        return curLexemeNum < getLineLexemesSize()-1;
    }

    public String getLexeme(int lineNum, int lexemeNum) {
        return splitText.get(lineNum).get(lexemeNum);
    }

    public int getLineLexemesSize(){
        return splitText.get(curLineNum).size();
    }

    public int getLinesSize(){
        return splitText.size();
    }

    public boolean hasNext(){
        return hasNextLine()||hasNextLineLexeme();
    }

    public int getLineNum() {
        return curLineNum;
    }

    public int getLexemeNum() {
        return curLexemeNum;
    }
}
