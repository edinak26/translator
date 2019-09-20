package phoenix.model.grammar.boundary;

import phoenix.model.grammar.entities.*;

import java.util.*;

public class BoundaryConditionConstructor {
    class Searched{
        private WideTerminal searchedWideTerminals;
        private Set<LocalBoundaryCondition> searchedConditions;
        Searched(WideTerminal searchedWideTerminals,Set<LocalBoundaryCondition> searchedConditions){
            this.searchedConditions=new HashSet<>(searchedConditions);
            this.searchedWideTerminals=searchedWideTerminals;
        }
        void set(Searched searched){
            searchedWideTerminals = searched.searchedWideTerminals;
            searchedConditions = new HashSet<>(searched.searchedConditions);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Searched searched = (Searched) o;
            return Objects.equals(searchedWideTerminals, searched.searchedWideTerminals) &&
                    Objects.equals(searchedConditions, searched.searchedConditions);
        }

        @Override
        public int hashCode() {
            return Objects.hash(searchedWideTerminals, searchedConditions);
        }

        @Override
        public String toString() {
            return this.hashCode()+" "+ searchedWideTerminals +"____\n"+ searchedConditions +"\n ------------------";
        }
    }
    private static int counter = 0;
    private Terminal basis;
    private Set<LocalBoundaryCondition> conditions;
    private Rules rules;

    private Set<WideTerminal> searchedWideTerminals;
    private Set<Set<LocalBoundaryCondition>> searchedConditions;
    private Set<Searched> searched;
    public boolean isFirst=true;

    public BoundaryConditionConstructor(Terminal basis, Rules rules) {
        this.basis = basis;
        this.rules = rules;
        conditions = new HashSet<>();
    }

    public Set<LocalBoundaryCondition> construct() {
        counter=0;
        searched= new HashSet<>();
        //System.out.println("B: "+basis);
        //System.out.println("R: "+rules);
        for (WideTerminal wideTerminal : rules.getWideTerminals(basis)) {
            //System.out.println("+++"+wideTerminal+"||"+wideTerminal.getNonTerminal()+" | "+wideTerminal.isFinal());
            if (wideTerminal.isFinal()) {
                searched.add(new Searched(wideTerminal,conditions));
                conditions.add(new LocalBoundaryCondition(wideTerminal));
            } else {
                 addAll(wideTerminal,constructBefore(wideTerminal, searched));
            }
        }
        //System.out.println("Result: "+conditions);
        //System.out.println("++++++++++++++++++++++++++++");
        //System.out.println(basis);
        //System.out.println(conditions);
       // System.out.println(rules);
       // System.out.println("----------------------------");
        return conditions;
    }

    private Set<LocalBoundaryCondition>  constructBefore(WideTerminal wideTerminal,Set<Searched> search) {
        return (new BoundaryConditionConstructor(wideTerminal.getNonTerminal(), rules)).getBefore(search);
    }

    private Set<LocalBoundaryCondition> getBefore(Set<Searched> search) {
        counter++;
        //System.out.println(search);
        this.searched=new HashSet<>(search);//
        //System.out.println("S:"+searched.searchedWideTerminals+ "|||" + searched.searchedConditions);
        //System.out.println("before wides: " +
        //System.out.println("Basis:" + basis);
        if(rules.getWideTerminals(basis).isEmpty()&&isFirst){
            conditions.add(new LocalBoundaryCondition());
            counter--;
            return conditions;
        }
        isFirst=false;
        for (WideTerminal wideTerminal : new ArrayList<>(rules.getWideTerminals(basis))) {
            //System.out.println("S: "+ new ArrayList<>(rules.getWideTerminals(basis)).size());
            //System.out.println( "ind: "+new ArrayList<>(rules.getWideTerminals(basis)).indexOf(wideTerminal));
            //System.out.println("C "+counter);
            //System.out.println(new ArrayList<>(rules.getWideTerminals(basis))+""+counter);
            if (!wideTerminal.isFinal()&&!wideTerminal.getNonTerminal().isAxiom()&&counter<7){//&&!this.searched.contains(new Searched(wideTerminal,conditions))) {
                //System.out.println("+");
                //System.out.println("CCCCCCCCCCC"+conditions);
                //System.out.println(")))))"+new Searched(wideTerminal,conditions)+"$*&#@(*&%(*#");
                Searched sear = new Searched(wideTerminal,conditions);
                this.searched.add(sear);
                addAll(wideTerminal,constructBefore(wideTerminal,this.searched));
                //System.out.println(this.searched.contains(sear));
                conditions.add(new LocalBoundaryCondition(wideTerminal));
                //System.out.println("AFTERRRR" + searched +"AAAAA");
                //save(constructBefore(wideTerminal,this.searchedWideTerminals,this.searchedConditions));
                save(constructBefore(wideTerminal,this.searched));
                //System.out.println("out");
            }
            //this.searched.add(new Searched(wideTerminal,conditions));
            conditions.add(new LocalBoundaryCondition(wideTerminal));
        }
        //System.out.println("before: "+conditions + " | "+basis);
        counter--;
        return conditions;
    }



    private void save(LocalBoundaryCondition condition, Set<LocalBoundaryCondition> saveConditions){
        //System.out.println("Old::"+conditions);
        conditions.remove(condition);
        saveConditions.forEach(c->{
            conditions.add(condition.join(c));
        });
        //System.out.println("New::"+conditions);

    }

    private void save(Set<LocalBoundaryCondition> saveConditions){
        for(LocalBoundaryCondition condition: new ArrayList<>(conditions)){
            conditions.remove(condition);
            saveConditions.forEach(c->{
                conditions.add(condition.join(c));
            });
        }
        //System.out.println("RRRRRRR "+conditions+"rrrrrrrr");

    }



    private void addAll(WideTerminal wideTerminal, Set<LocalBoundaryCondition> addConditions) {
        addConditions.forEach(c-> conditions.add((new LocalBoundaryCondition(wideTerminal)).join(c)));
    }
}
