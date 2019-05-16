package phoenix.model.grammar.constructor.commands;

import phoenix.model.grammar.constructor.GrammarConstructor;

import java.util.List;

public abstract class GrammarConstructorCommand {
    GrammarConstructor constructor;

    public GrammarConstructorCommand(GrammarConstructor constructor) {
        this.constructor = constructor;
    }

    public abstract void execute(List<String> line);



}
