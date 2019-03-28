package phoenix.general.model.lexical.analyzer;

import phoenix.accessory.info.SpecTypesGrammar;
import phoenix.general.model.reader.TextReader;

public class LexicalAnalyzer {
    CodeText code;
    LexemeTypes lexemeTypes;
    TablesManager tables;


    public LexicalAnalyzer() throws Exception{
        code = new CodeText(TextReader.code().get());
        lexemeTypes = new LexemeTypes(TextReader.grammar().get());
        System.out.println(TextReader.grammar().get());
        System.out.println(TextReader.regexGrammar().get());
        System.out.println(TextReader.typesGrammar().get());
        tables = new TablesManager(code);
    }
    public void analyse(){
        while(code.hasNext()){
            code.goToNextLexeme();
            checkMinus();
            checkLexeme();
        }
        tables.show();
    }
    private void checkLexeme(){
        boolean hasLexType = lexemeTypes.hasType(code.getCurLexeme());
        if(hasLexType){
            tables.addCurLexeme();
            return;
        }
        String specType = SpecTypesGrammar.check(code.getCurLexeme());
        boolean isSpecType = specType!=null;
        if(isSpecType){
            tables.addCurLexeme(specType);
            return;
        }
        throw new ArrayIndexOutOfBoundsException(code.getCurLexeme());
    }
    private void checkMinus(){
        boolean isLexMinus = code.getCurLexeme().equals("-");
        if(isLexMinus){
            if(code.hasNext()){
                boolean isNextCON = SpecTypesGrammar.check(code.next()).equals("CON");
                if(isNextCON){

                }
            }
        }
    }
}
