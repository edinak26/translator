package phoenix.general.model.lexical.analyzer;

import phoenix.general.model.reader.GrammarDivider;
import phoenix.general.model.reader.TextReader;

public class LexicalAnalyzer {
    CodeText code;


    public LexicalAnalyzer() throws Exception{
        code = new CodeText((new TextReader()).getText());
        //CodeReader.getText()
    }
    public void analyse(){
        while(code.hasNext()){
            System.out.println("L: "+ code.next() + " Line: "+ code.getLineNum() + " Lex num: "+code.getLexemeNum());
        }
    }
}
