package phoenix.model.grammar.constructor.commands;

import phoenix.model.grammar.constructor.GrammarConstructor;
import phoenix.model.grammar.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static phoenix.interfaces.MetaLanguage.GRAMMAR_OR;

public class AddRuleCommand extends GrammarConstructorCommand {
    public AddRuleCommand(GrammarConstructor constructor) {
        super(constructor);
    }

    public Set<Terminal> getUniqueTerminals(){
        return constructor.getUniqueTerminals();
    }

    @Override
    public void execute(List<String> line) {
        addRule(line);
    }

    private void addRule(List<String> line) {
        Rule rule = createRule(new ArrayList<>(line));
        constructor.getRules().add(rule);
        addAxiom(rule);
    }

    private Rule createRule(List<String> line) {
        NonTerminal nonTerminal = createNonTerminal(line.remove(0));
        List<Replace> replaces = createReplacements(line);
        return new Rule(nonTerminal, replaces);
    }

    private NonTerminal createNonTerminal(String name) {
        Terminal terminal = createTerminal(name);
        if (terminal instanceof NonTerminal) {
            return (NonTerminal) terminal;
        }
        return convertTerminal(terminal);
    }

    private Terminal createTerminal(String name) {
        Terminal terminal = getUniqueTerminal(name);
        if (terminal != null)
            return terminal;
        return createUniqueTerminal(name);
    }

    private Terminal getUniqueTerminal(String name) {
        return getUniqueTerminals().stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }

    private Terminal createUniqueTerminal(String name) {
        Terminal terminal = new Terminal(name);
        getUniqueTerminals().add(terminal);
        return terminal;
    }

    private void addAxiom(Rule rule) {
        VisibilityBlock currentBlock = constructor.currentBlock();
        if(!currentBlock.hasAxiom()){
            currentBlock.setAxiom(rule.getNonTerminal());
        }
    }

    private NonTerminal convertTerminal(Terminal terminal) {
        NonTerminal nonTerminal = new NonTerminal(terminal, constructor.currentBlock());
        getUniqueTerminals().remove(terminal);
        getUniqueTerminals().add(nonTerminal);
        return nonTerminal;
    }

    private List<Replace> createReplacements(List<String> line) {
        List<Replace> replaces = new ArrayList<>();
        while (!line.isEmpty()) {
            replaces.add(createReplacement(line));
        }
        return replaces;
    }

    private Replace createReplacement(List<String> line) {
        List<Terminal> replacement = new ArrayList<>();
        while (!line.isEmpty() && !end(line)) {
            replacement.add(createTerminal(line.remove(0)));
        }
        return new Replace(replacement);
    }

    private boolean end(List<String> line) {
        if (!line.get(0).equals(GRAMMAR_OR))
            return false;
        line.remove(0);
        return true;
    }
}
