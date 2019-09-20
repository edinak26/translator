package phoenix.model.syntax.analyzer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import phoenix.interfaces.MetaLanguage;
import phoenix.model.converter.PolishNotationConverter;
import phoenix.model.grammar.Grammar;
import phoenix.model.grammar.constructor.GrammarConstructor;
import phoenix.model.grammar.entities.*;
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
    private PolishNotationConverter converter;

    public SyntaxAnalyzer(TablesManager tables) throws Exception {
        inputTerminals = new TerminalsTable(tables);
        grammar = GrammarConstructor.construct(TextReader.grammar().setPath(STRAT_GRAM_PATH).get());
        relationsTable = new RelationsTable(grammar.getAllRules(), grammar.getUniqueTerminals());
        lexemesStack = new RelationTerminalsStack(relationsTable);
        lexemesStack.setIdentifiers(tables.getIdentifiers());
        currVisBlocks = new VisibilityBlocksStack(grammar.getGlobal());
        converter = new PolishNotationConverter(STRAT_GRAM_PATH);
        converter.setIdentifiers(tables.getIdentifiers());
        converter.setConstants(tables.getConstants());
        converter.setPointers(tables.getPointers());
    }

    public List<String> analyze() {
        while (!inputTerminals.isEmpty()) {
            saveCurrentRelation();
            logStatus();
            saveTableLexeme();

        }
        while(lexemesStack.size()!=2){
            saveCurrentRelation();
            logStatus();
            saveTableLexeme();
        }
        return lexemesStack.peekRelationTerminal().getShow();
    }

    private void saveCurrentRelation() {
        //System.out.println(lexemesStack.peek() + " " + inputTerminals.peek());
        currRelation = relationsTable.getRelation(lexemesStack.peek(), inputTerminals.peek());
        //System.out.println(currRelation);
        //System.out.println(inputTerminals.toString());
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
        /*System.out.println(inputTerminals.getSpecTypeValue());
        System.out.println(inputTerminals.getSpecTypeName());
        System.out.println(lexemesStack.peek());
        if(inputTerminals.getSpecTypeValue()!=null)
        if(inputTerminals.getSpecTypeName().equals(lexemesStack.peek().getName())) {
            lexemesStack.setLastValue(Collections.singletonList(new RelationTerminal(new Terminal(inputTerminals.getSpecTypeValue()))));
        }*/
        if (currRelation == Relation.LESS || currRelation == Relation.EQUAL) {
            lexemesStack.push(inputTerminals.peek(), currRelation);
            inputTerminals.pop();
        } else if (currRelation == Relation.MORE) {
            Replace replace = new Replace(lexemesStack.popLastRightPart());
            NonTerminal nonTerminal = getNextNonTerminal(replace);
            setVisibilityBlocks(nonTerminal);

            List<RelationTerminal> polishNotationLine = converter.get(nonTerminal,lexemesStack.getLastRelationRightPart());
            //System.out.println(polishNotationLine);
            lexemesStack.push(
                    nonTerminal
                    , relationsTable.getRelation(lexemesStack.peek(), nonTerminal)
            );

            //if(inputTerminals.getSpecTypeValue()!=null){
            //    polishNotationLine.get(polishNotationLine.size()-1).setValue(Collections.singletonList(new RelationTerminal(inputTerminals.getSpecTypeValue())));
            //}
            //System.out.println();
            lexemesStack.setLastValue(polishNotationLine);
        }
    }

    private NonTerminal getNextNonTerminal(Replace replace) {
        System.out.println("replace: "+replace);
        VisibilityBlock block = currVisBlocks.peek();
        List<NonTerminal> nonTerminals = NonTerminalsSearcher.getNonTerminal(block, replace);
        if (nonTerminals.size() == 1)
            return nonTerminals.get(0);
        System.out.println(nonTerminals);
        //System.out.println(lexemesStack.peek()+"||"+inputTerminals.peek());
        NonTerminalChooser chooser = new NonTerminalChooser(new Boundary(lexemesStack.peek(), inputTerminals.peek()));
        //System.out.println(nonTerminals);
        nonTerminals = chooser.choose(nonTerminals);
        if (nonTerminals.size() == 1)
            return nonTerminals.get(0);
        if(nonTerminals.size()== 0)
            throw new RuntimeException("Uncorrect lexeme "+ replace);

            throw new RuntimeException("Uncorrect grammar"+nonTerminals);
    }

    private void setVisibilityBlocks(NonTerminal nonTerminal) {

        if (nonTerminal.isAxiom()) {
            goOutBlock(nonTerminal);
        }
        else{
            goNextBlock(nonTerminal);
        }

    }

    private void goOutBlock(NonTerminal nonTerminal) {
        /*VisibilityBlock oldBlock = currVisBlocks.pop();
        if (!oldBlock.getAxiom().equals(nonTerminal))
            throw new RuntimeException("Wrong grammar: Non terminal: " + nonTerminal + " is not axiom of " + oldBlock);
        */
        if(currVisBlocks.peek().equals(nonTerminal.getBlock())){
            currVisBlocks.pop();
        }
    }

    private void goNextBlock(NonTerminal nonTerminal) {
        if(!currVisBlocks.peek().equals(nonTerminal.getBlock())){
            currVisBlocks.push(nonTerminal.getBlock());
        }
    }


}
