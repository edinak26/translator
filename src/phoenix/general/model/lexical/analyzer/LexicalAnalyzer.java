package phoenix.general.model.lexical.analyzer;

import phoenix.general.model.lexical.analyzer.reader.TextReader;

public class LexicalAnalyzer {
    CodeText code;


    public LexicalAnalyzer() throws Exception{
        code = new CodeText(TextReader.grammar().get());
        //CodeReader.getText()
        Lexemes lexemes = new Lexemes(TextReader.grammar().get());
        lexemes.show();
    }
    public void analyse(){
        while(code.hasNext()){
            //System.out.println("L: "+ code.next() + " Line: "+ code.getLineNum() + " Lex num: "+code.getLexemeNum());
        }
    }
}
