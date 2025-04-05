package org.sophia.nodecode.logicSystems.types;

import org.sophia.nodecode.logicSystems.core.DataType;

public class TypeBoolean extends DataType<Boolean> {
    public TypeBoolean(Boolean initVal) {
        super(Boolean.class, initVal);
    }
    public TypeBoolean(){
        this(false);
    }

    public int getColor(){
        return 0x24bb42;
    }
}
