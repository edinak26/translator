package phoenix.general.model.syntax.analyzer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import phoenix.accessory.constant.Characters;
import phoenix.general.interfaces.MetaLanguage;
import phoenix.general.model.entities.*;
import phoenix.general.model.lexical.analyzer.TablesManager;
import phoenix.general.model.reader.TextReader;

import java.util.*;

public class SyntaxAnalyzer implements Characters, MetaLanguage {
    private static final Logger logger = LogManager.getLogger(Messages.class.getName());


    private LexemesStack lexemesStack;
    private TablesManager tables;
    Grammar grammar;
    RelationsTable relationsTable;
    private static final String STRAT_GRAM_PATH = "D:\\University\\Java\\translator\\src\\main\\java\\phoenix\\accessory\\info\\stratGram";
    private VisibilityBlocksStack currVisBlocks;
    private String currRelation;

    public SyntaxAnalyzer(TablesManager tables) throws Exception {
        lexemesStack = new LexemesStack();
        this.tables = tables;
        relationsTable = new RelationsTable();
        grammar = GrammarConstructor.getGrammar(TextReader.grammar().setPath(STRAT_GRAM_PATH).get());
        currVisBlocks = new VisibilityBlocksStack(grammar);
        GrammarSetsSearcher.setGrammar(grammar);
    }

    public void analyze() {
        while (tables.hasNext()) {
            tables.goNext();
            System.out.println("@" + lexemesStack.getLastLexemeName());
            setRelation();
            logStatus();
            saveTableLexeme();
        }
    }

    private void setRelation() {
        currRelation = getRelation(lexemesStack.getLastLexemeName(), tables.get().lexeme());
        checkRelation(currRelation);
    }

    private String getRelation(String lex1, String lex2) {
        return relationsTable.getRelation(lex1, lex2);
    }

    private void checkRelation(String relation) {
        if (relation == null) {
            throw new RuntimeException("Лексема не может стоять здесь: " + tables.get().lexeme());
        }
    }

    private void logStatus() {
        logger.info(
                lexemesStack.toString()
                        + "<--(" + currRelation + ")--"
                        + tables.get().lexeme() + "|"
                        + currVisBlocks.toString());
    }

    private void saveTableLexeme() {
        if (currRelation.equals(RELATION_LESS) || currRelation.equals(RELATION_EQUALITY)) {
            lexemesStack.push(tables.get(), currRelation);

        } else if (currRelation.equals(RELATION_MORE)) {
            List<String> rightPart = lexemesStack.popLastRightPart();

            NonTerminal nonTerminal = getNonTerminal(rightPart);
            checkAxiom(nonTerminal);
            lexemesStack.push(
                    nonTerminal.getName()
                    , getRelation(lexemesStack.getLastLexemeName(), nonTerminal.getName())
            );
            tables.goBack();
        }
    }

    private void checkAxiom(NonTerminal nonTerminal) {
        boolean isAxiom = nonTerminal.isAxiom();
        boolean isCurrAxiom = nonTerminal.isAxiomOf(currVisBlocks.getCurrentBlock());
        boolean isNewBlock = !nonTerminal.getCurrBlock().equals(currVisBlocks.getCurrentBlock());
        if (isAxiom) {
            if (isCurrAxiom) {
                currVisBlocks.pop();
            } else {
                Axiom axiom = (Axiom) nonTerminal;
                axiom.getNextBlock(lexemesStack.peek(), tables.get().lexeme(), currVisBlocks.getCurrentBlock());
                if (isNewBlock)
                    currVisBlocks.push(axiom.getNextBlock(lexemesStack.peek(), tables.get().lexeme(), currVisBlocks.getCurrentBlock()));
            }
        }
    }

    private NonTerminal getNonTerminal(List<String> rightPart) {
        NonTerminal nonTerminal = null;
        Stack<String> currBlocks = currVisBlocks.getBlocks();
        while (nonTerminal == null) {
            if (!currBlocks.empty()) {
                //TODO think about refactor grammar class
                // and add both to one method getNonTerminal
                nonTerminal = grammar.getBlockNonTerminal(rightPart, currBlocks.pop());
            } else {
                nonTerminal = grammar.getGlobalNonTerminal(rightPart);
            }
        }
        checkNonTerminal(nonTerminal, rightPart);
        return nonTerminal;
    }

    private void checkNonTerminal(NonTerminal nonTerminal, List<String> rightPart) {
        if (nonTerminal == null)
            throw new RuntimeException(
                    "NonTerminal not found in grammar for right part: " + rightPart.toString());
    }
}
