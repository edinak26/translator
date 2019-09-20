package phoenix.model.grammar.entities;

import java.util.ArrayList;
import java.util.List;

public class Replace {
    private List<Terminal> replace;

    public Replace(List<Terminal> replacement) {
        this.replace = replacement;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        replace.forEach(r -> str.append(r.toString()));
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Replace) {
            return ((Replace) o).replace.equals(replace);
        }
        return false;
    }

    public List<Terminal> toList() {
        return replace;
    }

    public Terminal get(int index) {
        if(index<0||index>=getSize())
            return null;
        return replace.get(index);
    }

    public Terminal getFirst() {
        return get(0);
    }

    public Terminal getLast() {
        if (replace.size() >= 1)
            return get(replace.size() - 1);
        return null;
    }

    public int getSize(){
        return replace.size();
    }

    public Terminal getPenultimate() {
        if (replace.size() > 1)
            return get(replace.size() - 2);
        return null;
    }

    public Terminal getAfter(Terminal terminal) {
        int index = replace.indexOf(terminal);
        boolean hasAfter = index >= 0 && index < replace.size() - 1;
        if (hasAfter) {
            return replace.get(index + 1);
        }
        return null;
    }

    /*public Terminal getAfterMinus(Terminal terminal, Terminal before, Rules rules) {
        int index = replace.indexOf(terminal);
        boolean hasAfter = index >= 0 && index < replace.size() - 1;
        if (!hasAfter)
            return null;

        boolean hasBeforeTerminal = index != 0;
        if(!hasBeforeTerminal) {
            boolean isBeforeNull = before == null;
            if (isBeforeNull)
                return replace.get(index + 1);
            else{
                if(terminal instanceof NonTerminal) {
                    List<Replace> replaces = rules.getReplaces((NonTerminal) terminal);
                    for(Replace r : replaces){
                        if(r.getBefore(terminal).equals(before)){
                            return
                        }
                    }
                    System.out.println("HEEELLLOOO" + rules.getAfterMinus(terminal, before));
                    return replace.get(index + 1);
                }
            }
        }
        else {
            if (replace.get(index - 1).equals(before)) {
                return replace.get(index + 1);
            }
        }

        return null;
    }*/

    public List<WideTerminal> getWideTerminals(Terminal terminal){
        List<WideTerminal> result = new ArrayList<>();
        for(int i = 0; i<replace.size();i++){
            Terminal replaceTerminal = get(i);
            if(replaceTerminal.equals(terminal)){
                Terminal before = getBefore(i);
                Terminal after = getAfter(i);
                result.add(new WideTerminal(replaceTerminal,before,after));
            }
        }
        return result;
    }

    public Terminal getBefore(int index){
        if(0<index&&index<replace.size()){
            return get(index-1);
        }
        return null;
    }
    public Terminal getAfter(int index){
        if(0<=index&&index<replace.size()-1){
            return get(index+1);
        }
        return null;
    }

    public Terminal getBefore(Terminal terminal) {
        int index = replace.indexOf(terminal);
        boolean hasBefore = index > 0 && index <= replace.size() - 1;
        if (hasBefore) {
            return replace.get(index - 1);
        }
        return null;
    }

    public boolean isLast(Terminal terminal) {
        if (getLast() != null)
            return getLast().equals(terminal);
        return false;
    }

    public boolean isLastMinus(Terminal terminal, Terminal before) {
        if(before==null&&getSize()==1)
            return isLast(terminal);
        if (getPenultimate() != null)
            return getPenultimate().equals(before) && isLast(terminal);
        return false;
    }

    public boolean isFirst(Terminal terminal) {
        return getFirst().equals(terminal);
    }


    public boolean has(Terminal terminal) {
        return replace.stream().anyMatch(t -> t.equals(terminal));
    }

    public boolean contains(Terminal terminal) {
        return replace.contains(terminal);
    }

    public void replace(Terminal terminal) {
        replace.replaceAll(t -> {
            //System.out.println(t+"|||"+terminal+t.getName().equals(terminal.getName()));
            if (t.getName().equals(terminal.getName())) {
                return terminal;
            }
            //System.out.println(replace);
            return t;
        });
    }


    public List<Terminal> getTerminals() {
        return replace;
    }

    public int indexOf(Terminal replaceTerminal) {
        return replace.indexOf(replaceTerminal);
    }
}
