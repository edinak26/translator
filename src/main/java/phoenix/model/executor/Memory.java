package phoenix.model.executor;

import phoenix.interfaces.ExecutorConstants;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Memory implements ExecutorConstants {
    private static List<List<String>> identifiers;

    public Memory(){
        identifiers = new ArrayList<>();
    }

    public void addIdentifier(String name) {
        List<String> identifier = new ArrayList<>();
        identifier.add(name);
        identifier.add("" + identifiers.size());
        identifier.add(DEFFAULT_IDENTIFIER_VALUE);
        identifiers.add(identifier);
    }

    public void addIdentifier(String name, String value) {
        addIdentifier(name);
        setIdentifierValue(name,value);
    }

    public void setIdentifierValue(String name, String value){
        if(containsIdentifier(name)){
            List<String> identifier = identifiers.get(getIdentifierIndex(name));
            identifier.set(2,value);
        } else{
            throw new RuntimeException("Identifier "+name+" not found");
        }
    }

    public String getIdentifierValue(String name){
        if(containsIdentifier(name)){
            return identifiers.get(getIdentifierIndex(name)).get(2);
        } else{
            throw new RuntimeException("Identifier "+name+" not found");
        }
    }

    public boolean containsIdentifier(String name) {
        return getIdentifierIndex(name)> -1;
    }

    public int getIdentifierIndex(String name){
        for(int i =0; i<identifiers.size();i++){
            if(identifiers.get(i).get(0).equals(name))
                return i;
        }
        return -1;
    }

    public void addConstant(String name) {
    }
}
