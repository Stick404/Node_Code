package org.sophia.nodecode.logicSystems.types;

import org.sophia.nodecode.logicSystems.core.DataType;

public class TypeObject extends DataType<Object> {
    public TypeObject(Object initVal) {
        super(Object.class, initVal);
    }

    public TypeObject(){
        this(null);
    }

    @Override
    public int getColor() {
        return 0xffffff;
    }
}
