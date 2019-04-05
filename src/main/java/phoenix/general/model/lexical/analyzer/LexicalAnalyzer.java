package phoenix.general.model.lexical.analyzer;


import phoenix.accessory.exceptions.NotLexemeException;
import phoenix.general.model.reader.TextReader;

public class LexicalAnalyzer {
    CodeText code;
    LexemeTypes lexemeTypes;
    TablesManager tables;
    SpecialLexemeTypes specTypes;


    public LexicalAnalyzer() throws Exception{
        code = new CodeText(TextReader.code().get());
        lexemeTypes = new LexemeTypes(TextReader.grammar().get());
        specTypes = new SpecialLexemeTypes(TextReader.regexGrammar().get());
        tables = new TablesManager(code);
    }
    public void analyse(){
        while(code.hasNext()){
            code.goToNextLexeme();
            saveLexeme();
        }
    }
    private void saveLexeme(){
        boolean hasLexType = lexemeTypes.hasType(code.getCurLexeme());
        if(hasLexType){
            tables.addCurLexeme();
            return;
        }
        String specType = specTypes.getType(code.getCurLexeme());
        boolean isSpecType = specType!=null;
        if(isSpecType){
            tables.addCurLexeme(specType);
            return;
        }
        throw new NotLexemeException(code.getCurLexeme(),code.getLineNum(),code.getLexemeNum());
    }
    public TablesManager getTables(){
        return tables;
    }
}
