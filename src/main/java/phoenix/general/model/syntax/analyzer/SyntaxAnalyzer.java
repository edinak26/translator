package phoenix.general.model.syntax.analyzer;

import phoenix.accessory.constant.Characters;
import phoenix.accessory.exceptions.NearLexemesException;
import phoenix.general.model.lexical.analyzer.LexemesTableElement;
import phoenix.general.model.lexical.analyzer.TablesManager;
import phoenix.general.model.reader.TextReader;

import java.util.*;

public class SyntaxAnalyzer implements Characters {
    private Stack<LexemesTableElement> stack;
    private TablesManager tables;
    private static StackLogger logger = new StackLogger();
    RelationsTable relationsTable;

    public SyntaxAnalyzer(TablesManager tables) throws Exception {
        stack = new Stack<>();
        stack.push(new LexemesTableElement());
        this.tables = tables;
        relationsTable = new RelationsTable();
    }

    public void analyze() {

        while (tables.hasNext()) {
            tables.goNext();
            String relation = relationsTable.getRelation(stack.peek().lexeme(), tables.get().lexeme());
            logger.log(stack,tables.get().lexeme(),relation);
            if (relation == null) {
                throw new NearLexemesException(stack.peek(), tables.get().lexeme());
            }
            if (relation.equals(RELATION_LESS) || relation.equals(RELATION_EQUALITY)) {
                stack.push(tables.get());
            } else if (relation.equals(RELATION_MORE)) {

                for (int i = stack.size() - 1; i > 0; i--) {

                    String rel = relationsTable.getRelation(stack.get(i - 1).lexeme(), stack.get(i).lexeme());
                    if (rel.equals(RELATION_LESS)) {
                        ArrayList<String> lexemes = new ArrayList<>();
                        String str = "";
                        int line = 0;
                        int lexNum = 0;
                        while (!stack.peek().equals(stack.get(i - 1))) {
                            LexemesTableElement el =  stack.pop();
                            lexemes.add(el.lexeme());
                            line=el.getLineNum();
                            lexNum=el.getLineLexNum();
                        }
                        Collections.reverse(lexemes);

                        for (String lex : lexemes) {
                            str += lex;
                        }
                        if(relationsTable.getRuleTerm(str)==null){
                            throw new NearLexemesException(stack.peek(), tables.get().lexeme());
                        }
                        stack.push(new LexemesTableElement()
                                .setName(relationsTable.getRuleTerm(str))
                                .setLineNum(line)
                                .setLineLexNum(lexNum));
                        tables.goBack();
                        i = 0;
                    }else{
                        //throw new NearLexemesException(stack.peek(), tables.get().lexeme());
                    }
                }


            }

        }
        boolean isAxiom = false;
        while (!isAxiom) {
            for (int i = stack.size() - 1; i > 0; i--) {
                String rel = relationsTable.getRelation(stack.get(i - 1).lexeme(), stack.get(i).lexeme());
                if (rel.equals(RELATION_LESS)) {
                    ArrayList<String> lexemes = new ArrayList<>();
                    String str = "";
                    while (!stack.peek().equals(stack.get(i - 1))) {
                        lexemes.add(stack.pop().lexeme());
                    }
                    Collections.reverse(lexemes);

                    for (String lex : lexemes) {
                        str += lex;
                    }
                    if(relationsTable.getRuleTerm(str)==null){
                        throw new NearLexemesException(stack.peek(), tables.get().lexeme());
                    }
                    stack.push(new LexemesTableElement().setName(relationsTable.getRuleTerm(str)));
                    i = 0;
                }
            }
            if (stack.size() == 2 && stack.get(1).lexeme().equals("<програма>") && stack.get(0).lexeme().equals("#")) {
                isAxiom = true;
                System.out.println("End");
            }
        }
    }


}
