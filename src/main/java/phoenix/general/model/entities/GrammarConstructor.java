package phoenix.general.model.entities;

import phoenix.general.interfaces.MetaLanguage;
import phoenix.general.interfaces.Patterns;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrammarConstructor implements Patterns {
    private Map<NonTerminal, List<List<String>>> grammar;
    private List<List<String>> splitText;
    private Stack<String> visibilityBlocks;

    private GrammarConstructor(List<List<String>> splitText) {
        grammar = new HashMap<>();
        this.splitText = splitText;
        visibilityBlocks = new Stack<>();

    }

    public static Map<NonTerminal, List<List<String>>> getGrammar(List<List<String>> splitText) {
        GrammarConstructor constructor = new GrammarConstructor(splitText);
        constructor.convert();
        return constructor.grammar;
    }

    private void convert() {
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

    private void addNonTerminal(List<String> line) {
        NonTerminal nonTerminal = new NonTerminal(line.remove(0));
        nonTerminal.setBlocks(visibilityBlocks);
        grammar.put(nonTerminal, splitLine(line));
    }

    private List<List<String>> splitLine(List<String> line) {
        List<List<String>> splitLine = new ArrayList<>();
        splitLine.add(new ArrayList<>());
        for (String elem : line) {
            if(elem.equals(GRAMMAR_OR)){
                splitLine.add(new ArrayList<>());
            }
            else {
                splitLine.get(splitLine.size()-1).add(elem);
            }
        }
        return splitLine;
    }

    private void addVisibilityBlock(String block){
        visibilityBlocks.push(block);
    }

    private void removeVisibilityBlock(){
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
