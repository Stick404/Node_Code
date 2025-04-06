package org.sophia.nodecode.logicSystems.types;

import org.sophia.nodecode.logicSystems.core.DataType;

import java.util.ArrayList;

public class TypeList extends DataType<ArrayList> {
    public TypeList(ArrayList<?> initVal) {
        super(ArrayList.class, initVal);
    }

    public TypeList(){
        this(new ArrayList<>());
    }

    @Override
    public int getColor() {
        return 0xc51fd4;
    }
}
