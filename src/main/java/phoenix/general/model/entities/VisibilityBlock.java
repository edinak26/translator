package phoenix.general.model.entities;

import java.util.List;
import java.util.Map;

public class VisibilityBlock {
    String name;
    List<VisibilityBlock> children;

    public VisibilityBlock(String name, List<VisibilityBlock> children) {
        this.name = name;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public boolean hasChildren(){
        return children!=null;
    }

    public boolean isChild(VisibilityBlock block){
        for(VisibilityBlock child: children){
            if(block.equals(child)){
                return true;
            }
        }
        return false;
    }
}
