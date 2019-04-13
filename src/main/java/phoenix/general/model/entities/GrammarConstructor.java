package phoenix.general.model.entities;

import phoenix.general.interfaces.Patterns;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GrammarConstructor implements Patterns {
    private Grammar resultGrammar;
    private Map<NonTerminal, List<List<Terminal>>> grammar;
    private List<List<String>> splitText;
    private Stack<VisibilityBlock> visibilityBlocks;

    private GrammarConstructor(List<List<String>> splitText) {
        grammar = new LinkedHashMap<>();
        this.splitText = splitText;
        visibilityBlocks = new Stack<>();
    }

    public static Grammar construct(List<List<String>> splitText) {
        return GrammarConstructor.create(splitText).convert();
    }

    private static GrammarConstructor create(List<List<String>> splitText) {
        return new GrammarConstructor(splitText);
    }

    private Grammar convert() {
        convertSplitText();
        createGrammar();
        convertTerminals();
        convertAxioms();
        return resultGrammar;
    }

    private void convertSplitText() {
        for (List<String> line : splitText) {
            String first = line.get(0);
            if (isNonTerminal(first)) {
                addRule(line);
            } else if (isVisibilityOpenBlock(first)) {
                addVisibilityBlock(first);
            } else if (isVisibilityCloseBlock(first)) {
                removeVisibilityBlock();
            } else
                throw new RuntimeException("Undefine grammar element: " + first);
        }
    }

    private void createGrammar() {
        resultGrammar = new Grammar(grammar);
    }

    private void convertTerminals() {
        Set<NonTerminal> nonTerminals = new LinkedHashSet<>(grammar.keySet());
        for (NonTerminal nonTerminal : nonTerminals) {
                List<List<Terminal>> convertRightPart = grammar.get(nonTerminal).stream()
                        .map(rightPart -> rightPart.stream()
                                .map(terminal ->
                                        isNonTerminal(terminal) ? createNonTerminal(terminal) : terminal)
                                .collect(Collectors.toList()))
                        .collect(Collectors.toList());
                grammar.replace(nonTerminal, convertRightPart);
        }
    }

    private void convertAxioms(){
        grammar.keySet().forEach(nonTerminal -> {
            if(isAxiom(nonTerminal)){
                convertAxiom(nonTerminal);
            }
        });
    }

    private boolean isNonTerminal(Terminal terminal) {
        return resultGrammar.getAllNonTerminals().stream()
                .anyMatch(nonTerminal -> nonTerminal.equals(terminal));
    }

    private void addRule(List<String> line) {
        NonTerminal nonTerminal = createNonTerminal(line.remove(0));
        List<List<Terminal>> rightPart = convertRightPart(line);
        grammar.put(nonTerminal, rightPart);
        checkVisibilityBlockAxiom(nonTerminal);
    }

    private void checkVisibilityBlockAxiom (NonTerminal nonTerminal){
        if(!visibilityBlocks.peek().hasAxiom()){
            visibilityBlocks.peek().axiom(nonTerminal);
        }
    }

    private NonTerminal createNonTerminal(String name) {
        return new NonTerminal(name, visibilityBlocks);
    }

    private boolean isAxiom(NonTerminal nonTerminal){
        return visibilityBlocks.stream()
                .anyMatch(block->block.getAxiom().equals(nonTerminal));
    }

    private NonTerminal createNonTerminal(Terminal terminal){
        return resultGrammar.getAllNonTerminals().stream()
                .filter(nonTerminal -> nonTerminal.equals(terminal))
                .findAny().orElse(null);
    }

    private void convertAxiom(NonTerminal nonTerminal) {
        for (VisibilityBlock block : resultGrammar.getUniqueBlocks()) {
            Set<Terminal> after = GrammarSetsSearcher.getAfterMinus(nonTerminal, block);
            Set<Terminal> before = GrammarSetsSearcher.getBeforePlus(nonTerminal, block);
            nonTerminal.addSets(block, after, before);
        }
    }

    private List<List<Terminal>> convertRightPart(List<String> line) {
        List<List<Terminal>> rightPart = new ArrayList<>();
        rightPart.add(new ArrayList<>());
        for (String lexeme : line) {//TODO add in DIVIDER lines split
            if (lexeme.equals(GRAMMAR_OR)) {
                rightPart.add(new ArrayList<>());
            } else {
                rightPart.get(rightPart.size() - 1).add(Terminal.create(lexeme));
            }
        }
        return rightPart;
    }

    private void addVisibilityBlock(String line) {
        VisibilityBlock block = new VisibilityBlock(line);
        visibilityBlocks.peek().addChild(block);
        visibilityBlocks.push(block);
    }

    private void removeVisibilityBlock() {
        visibilityBlocks.pop();
    }

    private boolean isNonTerminal(String word) {
        Matcher matcher = GRAMMAR_NON_TERMINAL_GROUP_PATTERN.matcher(word);
        return matcher.matches();
    }

    private boolean isVisibilityOpenBlock(String word) {
        Matcher matcher = GRAMMAR_OPEN_BLOCK_PATTERN.matcher(word);
        return matcher.matches();
    }

    private boolean isVisibilityCloseBlock(String word) {
        Matcher matcher = GRAMMAR_CLOSE_BLOCK_PATTERN.matcher(word);
        return matcher.matches();
    }
}
