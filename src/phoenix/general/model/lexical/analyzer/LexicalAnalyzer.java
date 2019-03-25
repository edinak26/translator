package phoenix.general.model.lexical.analyzer;

public class LexicalAnalyzer {
    CodeText code;


    public LexicalAnalyzer() throws Exception{
        code = new CodeText(CodeReader.getText());
    }
    public void analyse(){
        while(code.hasNext()){
            System.out.println("L: "+ code.next() + " Line: "+ code.getLineNum() + " Lex num: "+code.getLexemeNum());
        }
    }
}
