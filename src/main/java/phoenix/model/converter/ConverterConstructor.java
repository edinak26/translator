package phoenix.model.converter;

import phoenix.model.grammar.entities.NonTerminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConverterConstructor {
    private List<List<String>> text;
    private Map<String,List<ConvertRule>> rules = new HashMap<>();

    public ConverterConstructor(List<List<String>> text) {
        this.text = text;
    }

    public Map<String,List<ConvertRule>> construct(){
        for(List<String> line: text){
            line = new ArrayList<>(line);
            NonTerminal nonTerminal = new NonTerminal(line.remove(0));
            List<ConvertRule> rules = new ArrayList<>();
            List<String> rule = new ArrayList<>();
            for(String terminal: line){
                if(!terminal.equals("|")){
                    rule.add(terminal);
                } else{
                    rules.add(new ConvertRule(rule));
                    rule = new ArrayList<>();
                }
            }
            rules.add(new ConvertRule(rule));
            this.rules.put(nonTerminal.getName(),rules);
        }
        return this.rules;
    }
}
