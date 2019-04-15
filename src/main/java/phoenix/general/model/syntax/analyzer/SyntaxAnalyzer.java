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
    private RelationTerminalsStack lexemesStack;
    private TablesManager tables;
    private TerminalsTable inputTerminals;
    Grammar grammar;
    RelationsTable relationsTable;
    private static final String STRAT_GRAM_PATH = "D:\\University\\Java\\translator\\src\\main\\java\\phoenix\\accessory\\info\\stratGram";
    private VisibilityBlocksStack currVisBlocks;
    private String currRelation;

    public SyntaxAnalyzer(TablesManager tables) throws Exception {
        inputTerminals = new TerminalsTable(tables);
        lexemesStack = new RelationTerminalsStack();
        this.tables = tables;
        relationsTable = new RelationsTable();
        grammar = GrammarConstructor.construct(TextReader.grammar().setPath(STRAT_GRAM_PATH).get());
        currVisBlocks = new VisibilityBlocksStack(grammar);
        GrammarSetsSearcher.setGrammar(grammar);
    }

    public void analyze() {
        while (tables.hasNext()) {
            tables.goNext();
            setRelation();
            logStatus();
            saveTableLexeme();
        }
    }

    private void setRelation() {
        currRelation = relationsTable.getRelation(lexemesStack.peek(),inputTerminals.peek());
        checkRelation(currRelation);
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
            lexemesStack.push(inputTerminals.peek(), currRelation);

        } else if (currRelation.equals(RELATION_MORE)) {
            NonTerminal nonTerminal = getNextNonTerminal();
            checkVisibilityBlocks(nonTerminal);
            lexemesStack.push(
                    nonTerminal.getName()
                    , getRelation(lexemesStack.getLastLexemeName(), nonTerminal.getName())
            );
            tables.goBack();
        }
    }

    private void checkVisibilityBlocks(){

    }

    private void checkAxiom(NonTerminal nonTerminal) {
        nonTerminal.nextBlock(lexemesStack.peek(),tables.get().lexeme(),currVisBlocks.pop());
        boolean isAxiom = nonTerminal.isAxiom();
        boolean isCurrAxiom = nonTerminal.isAxiomOf(currVisBlocks.getCurrentBlock());
        if (isAxiom) {
            if (isCurrAxiom) {
                currVisBlocks.pop();
            } else {
                Axiom axiom = (Axiom) nonTerminal;
                boolean isNewBlock = !axiom.getNextBlock(lexemesStack.peek(), tables.get().lexeme(), currVisBlocks.getCurrentBlock()).equals(currVisBlocks.getCurrentBlock());
                if (isNewBlock)
                    currVisBlocks.push(axiom.getNextBlock(lexemesStack.peek(), tables.get().lexeme(), currVisBlocks.getCurrentBlock()));
            }
        }
    }

    private NonTerminal getNextNonTerminal() {
        List<NonTerminal> nonTerminals = new ArrayList<>();
        List<Terminal> rightPart = lexemesStack.popLastRightPart();
        Stack<VisibilityBlock> currBlocks = currVisBlocks.getBlocks();
        while (nonTerminals.isEmpty()) {
            if (!currBlocks.empty()) {
                nonTerminals = grammar.getBlockNonTerminals(rightPart,currBlocks.pop());
            } else {
                throw new RuntimeException("Uncorrect grammar NonTerminal notfound for right part: "+rightPart.toString());
            }
        }
        if(nonTerminals.size()>1){
            throw  new RuntimeException("Uncorrect grammar right part:"+rightPart.toString()+" has more than 1 NT: "+nonTerminals.toString())
        }
        return nonTerminals.get(0);
    }
}
