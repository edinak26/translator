package phoenix.general.model.syntax.analyzer;

import phoenix.accessory.constant.Characters;
import phoenix.general.model.lexical.analyzer.LexemesTableElement;
import phoenix.general.model.lexical.analyzer.TablesManager;
import phoenix.general.model.reader.TextReader;

import java.util.*;

public class SyntaxAnalyzer implements Characters {
    private Stack<LexemesTableElement> stack;
    private TablesManager tables;
    RelationsTable relationsTable ;

    public SyntaxAnalyzer(TablesManager tables) throws Exception{
        stack = new Stack<>();
        stack.push(new LexemesTableElement());
        this.tables = tables;
        relationsTable = new RelationsTable();
    }

    public void analyze(){

        while(tables.hasNext()){
            for(LexemesTableElement el:stack){
                System.out.print(el.lexeme()+" ");
            }
            tables.goNext();
            System.out.print("("+tables.get().lexeme()+")");
            String relation = relationsTable.getRelation(stack.peek().lexeme(),tables.get().lexeme());
            System.out.println(":"+relation);
            if(relation.equals(RELATION_LESS)||relation.equals(RELATION_EQUALITY)){
                stack.push(tables.get());
            }
            else if(relation.equals(RELATION_MORE)){
                for(int i = stack.size()-1;i>0;i--){
                    String rel = relationsTable.getRelation(stack.get(i-1).lexeme(),stack.get(i).lexeme());
                    if(rel.equals(RELATION_LESS)){
                        ArrayList<String> lexemes = new ArrayList<>();
                        String str ="";
                        while (!stack.peek().equals(stack.get(i-1))) {
                            lexemes.add(stack.pop().lexeme());
                        }
                            Collections.reverse(lexemes);

                            for(String lex : lexemes){
                                str+=lex;
                            }

                        stack.push(new LexemesTableElement().setName(relationsTable.getRuleTerm(str)));
                        tables.goBack();
                        i=0;
                    }
                }
            }
        }
    }


}
