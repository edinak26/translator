package phoenix.model.converter;

import phoenix.model.grammar.entities.NonTerminal;
import phoenix.model.grammar.entities.Terminal;
import phoenix.model.reader.TextReader;
import phoenix.model.syntax.analyzer.RelationTerminal;
import phoenix.model.syntax.analyzer.relations.Relation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PolishNotationConverter {
    private Map<String, List<ConvertRule>> allRules;
    private List<String> identifiers;
    private List<String> constants;
    private List<String> pointers;

    public PolishNotationConverter(String path) throws Exception {
        ConvertRule.pointersCounter = 1;
        allRules = new ConverterConstructor(TextReader.polishNotation().setPath(path).get()).construct();
    }

    public List<RelationTerminal> get(NonTerminal nonTerminal, List<RelationTerminal> replace) {
        //System.out.println(allRules.keySet().toString());
        //System.out.println(replace);
        replace = replace.stream().map(t->{
            if(t.getTerminal().getName().equals("IDN")){
                return new RelationTerminal( new Terminal(identifiers.remove(0)));
            } else if(t.getTerminal().getName().equals("CON")){
                return new RelationTerminal( new Terminal(constants.remove(0)));
            } else if(t.getTerminal().getName().equals("POINTER")){
                return new RelationTerminal( new Terminal(pointers.remove(0)));
            } else
                return t;
            }).collect(Collectors.toList());

        List<ConvertRule> rules = allRules.get(nonTerminal.getName());
        if (rules != null) {
            for (ConvertRule rule : rules) {
                List<RelationTerminal> result = rule.get(replace,identifiers);
                if (result != null) {
                    return result;
                }
            }
        }
        return Collections.singletonList(new RelationTerminal(nonTerminal, replace));
    }

    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    public void setConstants(List<String> constants) {
        this.constants = constants;
    }

    public void setPointers(List<String> pointers) {
        this.pointers = pointers;
    }
}
