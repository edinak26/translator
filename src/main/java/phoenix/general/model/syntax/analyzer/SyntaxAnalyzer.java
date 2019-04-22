package phoenix.general.model.syntax.analyzer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import phoenix.accessory.constant.Characters;
import phoenix.general.interfaces.MetaLanguage;
import phoenix.general.model.entities.*;
import phoenix.general.model.grammar.Grammar;
import phoenix.general.model.grammar.GrammarConstructor;
import phoenix.general.model.grammar.GrammarSetsSearcher;
import phoenix.general.model.lexical.analyzer.TablesManager;
import phoenix.general.model.reader.TextReader;

import java.util.*;

public class SyntaxAnalyzer implements Characters, MetaLanguage {
    private static final Logger logger = LogManager.getLogger(Messages.class.getName());
    private RelationTerminalsStack lexemesStack;
    private TerminalsTable inputTerminals;
    private Grammar grammar;
    private RelationsTable relationsTable;
    private static final String STRAT_GRAM_PATH = "D:\\University\\Java\\translator\\src\\main\\java\\phoenix\\accessory\\info\\stratGram";
    private VisibilityBlocksStack currVisBlocks;
    private String currRelation;

    public SyntaxAnalyzer(TablesManager tables) throws Exception {
        inputTerminals = new TerminalsTable(tables);
        lexemesStack = new RelationTerminalsStack();
        grammar = GrammarConstructor.construct(TextReader.grammar().setPath(STRAT_GRAM_PATH).get());
        relationsTable = new RelationsTable(grammar);
        currVisBlocks = new VisibilityBlocksStack(grammar);
        GrammarSetsSearcher.setGrammar(grammar);
    }

    public void analyze() {
        while (!inputTerminals.isEmpty()) {
            setRelation();
            logStatus();
            saveTableLexeme();

        }
    }

    private void setRelation() {
        currRelation = relationsTable.getRelation(lexemesStack.peek(), inputTerminals.peek());
        checkRelation(currRelation);
    }


    private void checkRelation(String relation) {
        if (relation == null) {
            throw new RuntimeException("Лексема не может стоять здесь: " + inputTerminals.peek());
        }
    }

    private void logStatus() {
        logger.info(
                lexemesStack.toString()
                        + "<--(" + currRelation + ")--"
                        + inputTerminals.peek() + "|"
                        + currVisBlocks.toString());
    }

    private void saveTableLexeme() {

        if (currRelation.equals(RELATION_LESS) || currRelation.equals(RELATION_EQUALITY)) {
            lexemesStack.push(inputTerminals.peek(), currRelation);
            inputTerminals.pop();
        } else if (currRelation.equals(RELATION_MORE)) {

            NonTerminal nonTerminal = getNextNonTerminal();

            setVisibilityBlocks(nonTerminal);
            lexemesStack.push(
                    nonTerminal
                    , relationsTable.getRelation(lexemesStack.peek(),nonTerminal)
            );
        }
    }

    private void setVisibilityBlocks(NonTerminal nonTerminal){

        boolean isTermInNewBlock =!nonTerminal.getBlock().equals(currVisBlocks.peek());
        if (nonTerminal.isAxiom()) {
            goOutBlock(nonTerminal);
            goNextBlock(nonTerminal);

        }
        else if(isTermInNewBlock){
            currVisBlocks.push(nonTerminal.getBlock());
        }

    }

    private void goOutBlock(NonTerminal nonTerminal){
        VisibilityBlock oldBlock = currVisBlocks.pop();
        if(!oldBlock.getAxiom().equals(nonTerminal))
            throw  new RuntimeException("Wrong grammar: Non terminal: "+nonTerminal+" is not axiom of "+oldBlock);
    }

    private void goNextBlock(NonTerminal nonTerminal){
        VisibilityBlock nextBlock = nonTerminal.nextBlock(lexemesStack.peek(),inputTerminals.peek());
        if(!nextBlock.equals(currVisBlocks.peek()))
            currVisBlocks.push(nextBlock);
    }



    private NonTerminal getNextNonTerminal() {
        List<NonTerminal> nonTerminals = new ArrayList<>();
        List<Terminal> rightPart = lexemesStack.popLastRightPart();
        Stack<VisibilityBlock> currBlocks = currVisBlocks.getBlocks();
        while (nonTerminals.isEmpty()) {
            if (!currBlocks.empty()) {
                nonTerminals = grammar.getBlockNonTerminals(rightPart, currBlocks.pop());
            } else {
                throw new RuntimeException("Uncorrect grammar NonTerminal notfound for right part: " + rightPart.toString());
            }
        }
        if (nonTerminals.size() > 1) {
            throw new RuntimeException("Uncorrect grammar right part:" + rightPart.toString() + " has more than 1 NT: " + nonTerminals.toString());
        }
        return nonTerminals.get(0);
    }
}
