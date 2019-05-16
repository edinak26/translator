package phoenix.model.grammar.constructor.commands;

import phoenix.model.grammar.constructor.GrammarConstructor;
import phoenix.model.grammar.entities.VisibilityBlock;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AddVisibilityBlockCommand extends GrammarConstructorCommand {
    private Set<VisibilityBlock> uniqueBlocks;

    public AddVisibilityBlockCommand(GrammarConstructor constructor) {
        super(constructor);
        uniqueBlocks = new LinkedHashSet<>();
    }

    @Override
    public void execute(List<String> line) {
        addVisibilityBlock(line.get(0));
    }

    private void addVisibilityBlock(String name) {
        VisibilityBlock newBlock = createBlock(name);
        VisibilityBlock currentBlock = constructor.currentBlock();
        currentBlock.addChild(newBlock);
        newBlock.setParent(currentBlock);
        constructor.saveRules();
        constructor.addBlock(newBlock);
    }

    private VisibilityBlock createBlock(String name) {
        VisibilityBlock block = getUniqueBlock(name);
        if (block != null)
            return block;
        return createUniqueBlock(name);

    }

    private VisibilityBlock getUniqueBlock(String name) {
        return uniqueBlocks.stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }

    private VisibilityBlock createUniqueBlock(String name) {
        VisibilityBlock block = new VisibilityBlock(name);
        uniqueBlocks.add(block);
        return block;
    }
}
