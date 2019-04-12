package phoenix.general.model.entities;

import phoenix.general.interfaces.Patterns;

import java.util.*;
import java.util.regex.Matcher;

public class GrammarConstructor implements Patterns {
    private Grammar resGrammar;
    private Map<NonTerminal, List<List<Terminal>>> grammar;
    private List<List<String>> splitText;
    private Stack<VisibilityBlock> visibilityBlocks;
    private boolean isBlockAxiom = false;

    private GrammarConstructor(List<List<String>> splitText) {
        grammar = new LinkedHashMap<>();
        this.splitText = splitText;
        visibilityBlocks = new Stack<>();
    }

    public static Grammar getGrammar(List<List<String>> splitText) {
        GrammarConstructor constructor = new GrammarConstructor(splitText);
        constructor.convert();
        return constructor.resGrammar;
    }

    private void convert() {
        convertSplitText();
        resGrammar = new Grammar(grammar);
        saveAxioms();
    }

    private void convertSplitText() {
        for (List<String> line : splitText) {
            String first = line.get(0);
            if (isNonTerminal(first)) {
                addNonTerminal(line);
            } else if (isVisibilityOpenBlock(first)) {
                addVisibilityBlock(first);
            } else if (isVisibilityCloseBlock(first)) {
                removeVisibilityBlock();
            } else
                throw new RuntimeException("Undefine grammar element: " + first);
        }
    }


    private void saveAxioms() {
        resGrammar.setAxiom(resGrammar.getNonTerminal(splitText.get(0).get(0)));

        Set<NonTerminal> nonTerminals= new HashSet<>(grammar.keySet());
        for (NonTerminal nonTerminal : nonTerminals) {
            if (nonTerminal.isAxiom()) {
                convertToBlockAxiom(nonTerminal);
            }
        }
    }

    private void convertToBlockAxiom(NonTerminal nonTerminal) {
        for (String block : resGrammar.getUniqueBlocks()) {
            Set<String> after = GrammarSetsSearcher.getAfterMinus(nonTerminal, block);
            Set<String> before = GrammarSetsSearcher.getBeforePlus(nonTerminal, block);
            nonTerminal.addSets(block, after, before);
        }
    }


    private void addNonTerminal(List<String> line) {
        ArrayList<String> rightPart = new ArrayList<>(line);
        NonTerminal nonTerminal = new NonTerminal(rightPart.remove(0));
        nonTerminal.setBlocks(visibilityBlocks);
        setAxiom(nonTerminal);
        grammar.put(nonTerminal, splitLine(rightPart));
    }

    private void setAxiom(NonTerminal nonTerminal) {
        nonTerminal.setAxiom(isAxiom);
        if (isAxiom)
            isAxiom = false;
    }

    private List<List<String>> splitLine(List<String> line) {
        List<List<String>> splitLine = new ArrayList<>();
        splitLine.add(new ArrayList<>());
        for (String elem : line) {
            if (elem.equals(GRAMMAR_OR)) {
                splitLine.add(new ArrayList<>());
            } else {
                splitLine.get(splitLine.size() - 1).add(elem);
            }
        }
        return splitLine;
    }

    private void addVisibilityBlock(String block) {
        visibilityBlocks.push(block);
        isAxiom = true;
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
