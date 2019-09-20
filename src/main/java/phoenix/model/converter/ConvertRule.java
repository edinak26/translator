package phoenix.model.converter;

import phoenix.interfaces.Patterns;
import phoenix.model.grammar.entities.NonTerminal;
import phoenix.model.grammar.entities.Terminal;
import phoenix.model.syntax.analyzer.RelationTerminal;
import phoenix.model.syntax.analyzer.relations.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConvertRule implements Patterns {
    private List<Terminal> start;
    private List<Terminal> converted;

    public static int pointersCounter;

    public ConvertRule(List<String> rule) {
        List<Terminal> result = new ArrayList<>();
        for(String terminal: rule){
            if(GRAMMAR_NON_TERMINAL_GROUP_PATTERN.matcher(terminal).matches()){
                result.add(new NonTerminal(terminal));
            } else if(terminal.equals("->>")){
                start=result;
                result=new ArrayList<>();
            }
            else{
                result.add(new Terminal(terminal));
            }
        }
        converted = result;
    }

    public List<RelationTerminal> get(List<RelationTerminal> replace, List<String> identifiers) {

        //System.out.println("input replace: "+replace);
        //System.out.println("start: "+start);
        //System.out.println(replace);
        if (replace.size() != start.size()) {
            return null;
        }

        List<RelationTerminal> result = new ArrayList<>();
        for(Terminal t: converted){
            result.add(new RelationTerminal(t));
        }
        result = convertPointers(result);
        //System.out.println("result");

        for (int i = 0; i < replace.size(); i++) {
            List<RelationTerminal> replaceValue = replace.get(i).getValue();
            Terminal replaceTerminal = replace.get(i).getTerminal();
            Terminal startTerminal = start.get(i);
            if (((replaceTerminal instanceof NonTerminal) && (startTerminal instanceof NonTerminal))) {
                result.replaceAll(t->{
                    if(t.getTerminal().getName().equals(startTerminal.getName()))
                        return  new RelationTerminal(replaceTerminal, replaceValue);
                    else
                        return t;
                });
            } else if((replaceTerminal instanceof NonTerminal)!=(startTerminal instanceof NonTerminal)){
                return null;
            }

        }
        return result;
    }

    private List<RelationTerminal> convertPointers(List<RelationTerminal> replace) {
        Pattern pattern = Pattern.compile("#[0-9]+");

        for(RelationTerminal terminal:new ArrayList<>(replace)){
            //System.out.println(terminal.getTerminal().getName());
            if(pattern.matcher(terminal.getTerminal().getName()).matches()){
                replace.replaceAll(t->{
                    if(t.getTerminal().getName().equals(terminal.getTerminal().getName())){
                        return new RelationTerminal(new Terminal("##"+pointersCounter));
                    }
                    else{
                        return t;
                    }
                });
                pointersCounter++;
            }
        }

        return replace;
    }
}
