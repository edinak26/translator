package phoenix.model.grammar.constructor.commands;

import phoenix.model.grammar.constructor.GrammarConstructor;

import java.util.List;

public class RemoveVisibilityBlockCommand extends GrammarConstructorCommand {
    public RemoveVisibilityBlockCommand(GrammarConstructor constructor) {
        super(constructor);
    }

    @Override
    public void execute(List<String> line) {
        constructor.saveRules();
        removeVisibilityBlock(line.get(0));

    }


    private void removeVisibilityBlock(String name) {
        if (!isCurrentBlockClose(name))
            throw new RuntimeException("Block " + constructor.currentBlock() + " must be closed");
        constructor.popBlock();
    }

    private boolean isCurrentBlockClose(String name) {//TODO optimize
        String currentBlockName = constructor.currentBlock().getName();
        currentBlockName = currentBlockName.replaceAll("<:", "");
        currentBlockName = currentBlockName.replaceAll(":>", "");
        name = name.replaceAll("<;", "");
        name = name.replaceAll(";>", "");
        return name.equals(currentBlockName);
    }


}
