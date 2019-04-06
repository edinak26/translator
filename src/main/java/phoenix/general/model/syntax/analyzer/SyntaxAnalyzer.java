package phoenix.general.model.syntax.analyzer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import phoenix.accessory.constant.Characters;
import phoenix.accessory.exceptions.NearLexemesException;
import phoenix.general.interfaces.MetaLanguage;
import phoenix.general.model.entities.Grammar;
import phoenix.general.model.entities.NonTerminal;
import phoenix.general.model.lexical.analyzer.LexemesTableElement;
import phoenix.general.model.lexical.analyzer.TablesManager;
import phoenix.general.model.reader.TextReader;

import java.util.*;

public class SyntaxAnalyzer implements Characters, MetaLanguage {
    private static final Logger logger = LogManager.getLogger(Messages.class.getName());


    private Stack<LexemesTableElement> stack;
    private TablesManager tables;
    Grammar grammar;
    RelationsTable relationsTable;
    private static final String STRAT_GRAM_PATH = "D:\\University\\Java\\translator\\src\\main\\java\\phoenix\\accessory\\info\\stratGram";
    private Stack<String> currVisibilityBlock;

    public SyntaxAnalyzer(TablesManager tables) throws Exception {
        stack = new Stack<>();
        stack.push(new LexemesTableElement());
        this.tables = tables;
        relationsTable = new RelationsTable();

        grammar = new Grammar(TextReader.grammar().setPath(STRAT_GRAM_PATH).get());
        currVisibilityBlock = new Stack<>();
        currVisibilityBlock.push(grammar.getStartVisibilityBlocks());
        //System.out.println(SetsSearcher.get().first("<арифметичний вираз ініціалізації>"));;
    }

    public void analyze() {

        while (tables.hasNext()) {
            tables.goNext();
            String relation = relationsTable.getRelation(stack.peek().lexeme(), tables.get().lexeme());
            logger.info(Messages.stack(stack).get() + "<--(" + relation + ")--" + tables.get().lexeme()+"|"+currVisibilityBlock.toString());
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
                            LexemesTableElement el = stack.pop();
                            lexemes.add(el.lexeme());
                            line = el.getLineNum();
                            lexNum = el.getLineLexNum();
                        }
                        Collections.reverse(lexemes);

                        System.out.println("hi" + lexemes.toString() + currVisibilityBlock.peek());

                        NonTerminal nonTerminal = null;

                        while (nonTerminal == null && !currVisibilityBlock.isEmpty()) {
                            String visBlock = currVisibilityBlock.pop();
                            nonTerminal = grammar.getBlockNonTerminal(lexemes, visBlock);
                        }

                        if(nonTerminal!=null){
                            currVisibilityBlock.push(nonTerminal.getCurrBlock());
                        }
                        else {
                            nonTerminal = grammar.getGlobalNonTerminal(lexemes);
                        }

                        /*if (!currVisibilityBlock.peek().equals(nonTerminal.getCurrBlock())) {
                            currVisibilityBlock.push(nonTerminal.getCurrBlock());
                        }*/
                        System.out.println(currVisibilityBlock);
                        stack.push(new LexemesTableElement()
                                .setName(nonTerminal.getName())
                                .setLineNum(line)
                                .setLineLexNum(lexNum));
                        tables.goBack();
                        i = 0;
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
                    stack.push(new LexemesTableElement().setName(grammar.getNonTerminal(lexemes, currVisibilityBlock.peek()).getName()));
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
