package org.sophia.nodecode.logicSystems.types;

import org.sophia.nodecode.logicSystems.core.DataType;

public class TypeString extends DataType<String> {
    public TypeString(String initVal) {
        super(String.class, initVal);
    }

    public TypeString(){
        this("");
    }

    @Override
    public int getColor() {
        return 0x8236de;
    }
}
