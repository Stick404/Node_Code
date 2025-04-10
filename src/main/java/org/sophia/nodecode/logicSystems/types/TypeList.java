package org.sophia.nodecode.logicSystems.types;

import org.sophia.nodecode.logicSystems.core.DataType;

import java.util.*;

public class TypeList extends DataType<ArrayList<DataType<?>>> {
    //mmmm, casting
    public TypeList(ArrayList<DataType<?>> initVal) {
        super((Class<ArrayList<DataType<?>>>) new ArrayList<DataType<?>>().getClass(), initVal);
    }

    public TypeList(){
        this(new ArrayList<>());
    }

    @Override
    public int getColor() {
        return 0xc51fd4;
    }

    @Override
    public ArrayList<DataType<?>> getData() {
        return (ArrayList<DataType<?>>) super.getData().clone();
    }
}
