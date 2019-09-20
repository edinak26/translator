package phoenix.model.grammar.constructor;

import phoenix.exceptions.GrammarException;
import phoenix.interfaces.Patterns;
import phoenix.model.grammar.Grammar;
import phoenix.model.grammar.constructor.commands.AddRuleCommand;
import phoenix.model.grammar.constructor.commands.AddVisibilityBlockCommand;
import phoenix.model.grammar.constructor.commands.GrammarConstructorCommand;
import phoenix.model.grammar.constructor.commands.RemoveVisibilityBlockCommand;
import phoenix.model.grammar.entities.*;

import java.util.*;
import java.util.regex.Pattern;

import static phoenix.exceptions.GrammarExceptions.WRONG_GRAMMAR_ELEMENT_MESSAGE;

public class GrammarConstructor implements Patterns {
    private Grammar resultGrammar;
    private Map<NonTerminal, List<List<Terminal>>> grammar;
    private Stack<VisibilityBlock> visibilityBlocks;
    private VisibilityBlock global;
    private Rules currentBlockRules;
    private Rules allRules;
    private Set<Terminal> uniqueTerminals;

    private Map<Pattern, GrammarConstructorCommand> commands;

    private GrammarConstructor() {
        grammar = new LinkedHashMap<>();
        global = new VisibilityBlock();
        visibilityBlocks = new Stack<>();
        visibilityBlocks.push(global);
        allRules = new Rules();
        currentBlockRules = new Rules();
        uniqueTerminals = new LinkedHashSet<>();
        createCommands();
    }

    private void createCommands() {
        commands = new LinkedHashMap<>();
        commands.put(GRAMMAR_NON_TERMINAL_GROUP_PATTERN, new AddRuleCommand(this));
        commands.put(GRAMMAR_OPEN_BLOCK_PATTERN, new AddVisibilityBlockCommand(this));
        commands.put(GRAMMAR_CLOSE_BLOCK_PATTERN, new RemoveVisibilityBlockCommand(this));
    }

    public static Grammar construct(List<List<String>> splitText) {
        return (new GrammarConstructor()).convert(splitText);
    }

    private Grammar convert(List<List<String>> splitText) {
        convertSplitText(splitText);
        UsagesConstructor.construct(global);
        return constructGrammar();
    }

    private Grammar constructGrammar() {
        Grammar grammar = new Grammar(global);
        grammar.setUniqueTerminals(uniqueTerminals);
        grammar.setRules(allRules);
        //grammar.show();
        return grammar;
    }

    private void convertSplitText(List<List<String>> splitText) {
        for (List<String> line : splitText) {
            String first = line.get(0);
            chooseCommand(first).execute(line);
        }
        convertTerminals();
    }

    private void convertTerminals() {
        uniqueTerminals.forEach(t -> {
            allRules.replace(t);
        });
    }

    private GrammarConstructorCommand chooseCommand(String name) {
        for (Map.Entry<Pattern, GrammarConstructorCommand> entry : commands.entrySet()) {
            if (entry.getKey().matcher(name).matches()) {
                return entry.getValue();
            }
        }
        throw new GrammarException(WRONG_GRAMMAR_ELEMENT_MESSAGE + name);
    }

    public VisibilityBlock currentBlock() {
        return visibilityBlocks.peek();
    }

    public void addBlock(VisibilityBlock block) {
        visibilityBlocks.push(block);
    }

    public void popBlock() {
        visibilityBlocks.pop();
    }

    public Rules getRules() {
        return currentBlockRules;
    }

    public void saveRules() {
        currentBlock().addRules(currentBlockRules);
        allRules.add(currentBlockRules);
        currentBlockRules = new Rules();
    }

    public Set<Terminal> getUniqueTerminals() {
        return uniqueTerminals;
    }
}
