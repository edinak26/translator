package phoenix.model.executor;

import phoenix.model.lexical.analyzer.TablesManager;

import java.util.*;

public class Executor {
    private List<String> polishNotationLine;
    private Memory memory;
    private int index = -1;
    private Map<String, Integer> pointers;
    private List<String> values;

    public Executor(List<String> polishNotationLine) {
        this.polishNotationLine = polishNotationLine;
        memory = new Memory();
        pointers = new HashMap<>();
        values = new ArrayList<>();
        for (int i = 0; i < polishNotationLine.size() - 1; i++) {
            if (polishNotationLine.get(i).charAt(0) == '#') {
                pointers.put(polishNotationLine.get(i), i);
            }
        }
        if (polishNotationLine.get(polishNotationLine.size() - 1).charAt(0) == '#') {
            pointers.put(polishNotationLine.get(polishNotationLine.size() - 1), polishNotationLine.size() - 1);
        }
        System.out.println(pointers.toString());
        System.out.println(polishNotationLine.size());
    }

    public void setTables(TablesManager tables) {
        tables.goTo(-1);
        while (tables.hasNext()) {
            tables.goNext();
            if ("IDN".equals(tables.get().getSpecType())) {
                memory.addIdentifier(tables.get().name());
            } else if ("CON".equals(tables.get().getSpecType())) {
                memory.addConstant(tables.get().name());
            }
        }
    }

    public void execute() {
        System.out.println(polishNotationLine);
        index = 0;
        while (index < polishNotationLine.size()) {
            //System.out.println(polishNotationLine);
            //System.out.println(values);
            //System.out.println(index);
            switch (get(current())) {
                case "==":
                    push((popValue() == popValue()) ? "1" : "0");
                    break;
                case "!=":
                    push((popValue() != popValue()) ? "1" : "0");
                    break;
                case "<=":
                    push((beforePopValue() <= popValue()) ? "1" : "0");
                    break;
                case ">=":
                    push((beforePopValue() >= popValue()) ? "1" : "0");
                    break;
                case "<":
                    push((beforePopValue() < popValue()) ? "1" : "0");
                    break;
                case ">":
                    push((beforePopValue() > popValue()) ? "1" : "0");
                    break;
                case "=":
                    memory.setIdentifierValue(beforePop(), "" + popValue());
                    break;
                case "+":
                    push((popValue() + popValue()) + "");
                    break;
                case "-":
                    push((popValue() - popValue()) + "");
                    break;
                case "*":
                    push((popValue() * popValue()) + "");
                    break;
                case "/":
                    push((beforePopValue() / popValue()) + "");
                    break;
                case ":":
                    goTo(get(last()));
                    break;
                case ";":
                    if (isFalse(popValue()))
                        goTo(get(last()));
                    break;
                case ">>":
                    System.out.println(popValue());
                    break;
                case "<<":
                    Scanner in = new Scanner(System.in);
                    memory.setIdentifierValue(pop(),in.nextInt()+"");
                    break;

                default:
                    if (memory.containsIdentifier(get(current()))) {
                        push(get(current()));
                    } else if (get(current()).charAt(0) != '#') {
                        push("" + Integer.valueOf(get(current())));
                    }
                    break;

            }
            index++;
        }
    }

    private boolean isFalse(int value) {
        return value==0;
    }

    private void goTo(String s) {
        index = pointers.get(s);
    }

    private void removeThree() {
        remove(current());
        remove(last());
        remove(beforeLast());
        index -= 3;
    }

    private void remove(int index) {
        polishNotationLine.remove(index);
    }

    private void removeLastTwo() {
        remove(last());
        remove(beforeLast());
        index -= 2;
    }

    private void set(String value) {
        polishNotationLine.set(index, value);
    }

    private String get(int index) {
        return polishNotationLine.get(index);
    }

    private int getValue(int index) {
        if (memory.containsIdentifier(get(index))) {
            return Integer.valueOf(memory.getIdentifierValue(get(index)));
        }
        return Integer.valueOf(get(index));
    }

    private int current() {
        return index;
    }

    private int last() {
        return index - 1;
    }

    private int beforeLast() {
        return index - 2;
    }

    private String pop() {
        return values.remove(values.size() - 1);
    }

    private String beforePop() {
        return values.remove(values.size() - 2);
    }

    private String peek() {
        return values.get(values.size() - 1);
    }

    private String beforePeek() {
        return values.get(values.size() - 2);
    }

    private void push(String value) {
        values.add(value);
    }

    private int popValue() {
        if (memory.containsIdentifier(peek())) {
            return Integer.valueOf(memory.getIdentifierValue(pop()));
        }
        return Integer.valueOf(pop());
    }

    private int beforePopValue() {
        if (memory.containsIdentifier(beforePeek())) {
            return Integer.valueOf(memory.getIdentifierValue(beforePop()));
        }
        return Integer.valueOf(beforePop());
    }
}
