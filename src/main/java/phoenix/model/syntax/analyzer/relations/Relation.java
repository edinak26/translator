package phoenix.model.syntax.analyzer.relations;

import phoenix.interfaces.Characters;

public enum Relation implements Characters {
    EQUAL(RELATION_EQUALITY),
    LESS(RELATION_LESS),
    MORE(RELATION_MORE),
    UNDEFINED(RELATION_UNDEFINED);

    private String character;

    Relation(String character){
        this.character=character;
    }

    @Override
    public String toString() {
        return character;
    }
}
