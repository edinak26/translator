package phoenix.model.grammar.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Usages {
    private Map<VisibilityBlock, BoundaryCondition> usages;

    public Usages() {
        usages = new HashMap<>();
    }

    public void addUsage(VisibilityBlock block, BoundaryCondition condition) {
        usages.put(block, condition);
    }
    public Set<Map.Entry<VisibilityBlock, BoundaryCondition>> entrySet(){
        return usages.entrySet();
    }

    public void show() {
        System.out.println("Usages->" + usages.keySet().toString());
        for (Map.Entry<VisibilityBlock, BoundaryCondition> usage : usages.entrySet()) {
            System.out.println(usage.getKey());
            System.out.println(usage.getValue());

        }
    }

    public int size() {
        return usages.size();
    }

    @Override
    public String toString() {
        return "Usages{" +
                "usages=" + usages +
                '}';
    }
}
