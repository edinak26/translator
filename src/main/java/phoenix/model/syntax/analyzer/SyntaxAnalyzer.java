package phoenix.model.syntax.analyzer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import phoenix.interfaces.MetaLanguage;
import phoenix.model.grammar.Grammar;
import phoenix.model.grammar.constructor.GrammarConstructor;
import phoenix.model.grammar.entities.Boundary;
import phoenix.model.grammar.entities.NonTerminal;
import phoenix.model.grammar.entities.Replace;
import phoenix.model.grammar.entities.VisibilityBlock;
import phoenix.model.lexical.analyzer.TablesManager;
import phoenix.model.reader.TextReader;
import phoenix.model.syntax.analyzer.relations.Relation;
import phoenix.model.syntax.analyzer.relations.RelationsTable;

import java.util.*;

public class SyntaxAnalyzer implements MetaLanguage {
    private static final Logger logger = LogManager.getLogger(Messages.class.getName());
    private RelationTerminalsStack lexemesStack;
    private TerminalsTable inputTerminals;
    private Grammar grammar;
    private RelationsTable relationsTable;
    private static final String STRAT_GRAM_PATH = "D:\\University\\Java\\translator\\src\\main\\resources\\stratGram";
    private VisibilityBlocksStack currVisBlocks;
    private Relation currRelation;

    public SyntaxAnalyzer(TablesManager tables) throws Exception {
        inputTerminals = new TerminalsTable(tables);
        lexemesStack = new RelationTerminalsStack();
        grammar = GrammarConstructor.construct(TextReader.grammar().setPath(STRAT_GRAM_PATH).get());
        relationsTable = new RelationsTable(grammar.getAllRules(), grammar.getUniqueTerminals());
        currVisBlocks = new VisibilityBlocksStack(grammar.getGlobal());
    }

    public void analyze() {
        while (!inputTerminals.isEmpty()) {
            saveCurrentRelation();
            logStatus();
            saveTableLexeme();

        }
    }

    private void saveCurrentRelation() {
        System.out.println(lexemesStack.peek() + " " + inputTerminals.peek());
        currRelation = relationsTable.getRelation(lexemesStack.peek(), inputTerminals.peek());
        System.out.println(currRelation);
        checkRelation(currRelation);
    }


    private void checkRelation(Relation relation) {
        if (relation == Relation.UNDEFINED) {
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

        if (currRelation == Relation.LESS || currRelation == Relation.EQUAL) {
            lexemesStack.push(inputTerminals.peek(), currRelation);
            inputTerminals.pop();
        } else if (currRelation == Relation.MORE) {
            NonTerminal nonTerminal = getNextNonTerminal();
            setVisibilityBlocks(nonTerminal);
            lexemesStack.push(
                    nonTerminal
                    , relationsTable.getRelation(lexemesStack.peek(), nonTerminal)
            );
        }
    }

    private NonTerminal getNextNonTerminal() {
        Replace replace = new Replace(lexemesStack.popLastRightPart());
        System.out.println("rp: "+replace);
        VisibilityBlock block = currVisBlocks.peek();
        List<NonTerminal> nonTerminals = NonTerminalsSearcher.getNonTerminal(block, replace);
        if (nonTerminals.size() == 1)
            return nonTerminals.get(0);
        System.out.println("Bound:"+lexemesStack.peek() + " " + inputTerminals.peek());
        NonTerminalChooser chooser = new NonTerminalChooser(new Boundary(lexemesStack.peek(), inputTerminals.peek()));
        nonTerminals = chooser.choose(nonTerminals);
        System.out.println(nonTerminals);
        if (nonTerminals.size() == 1)
            return nonTerminals.get(0);
        throw new RuntimeException("Hello, my dear");
    }

    private void setVisibilityBlocks(NonTerminal nonTerminal) {

        boolean isTermInNewBlock = !nonTerminal.getBlock().equals(currVisBlocks.peek());
        if (nonTerminal.isAxiom()) {
            goOutBlock(nonTerminal);
            goNextBlock(nonTerminal);

        } else if (isTermInNewBlock) {
            currVisBlocks.push(nonTerminal.getBlock());
        }

    }

    private void goOutBlock(NonTerminal nonTerminal) {
        VisibilityBlock oldBlock = currVisBlocks.pop();
        if (!oldBlock.getAxiom().equals(nonTerminal))
            throw new RuntimeException("Wrong grammar: Non terminal: " + nonTerminal + " is not axiom of " + oldBlock);
    }

    private void goNextBlock(NonTerminal nonTerminal) {
        VisibilityBlock nextBlock = nonTerminal.nextBlock(lexemesStack.peek(), inputTerminals.peek());
        if (!nextBlock.equals(currVisBlocks.peek()))
            currVisBlocks.push(nextBlock);
    }


}
