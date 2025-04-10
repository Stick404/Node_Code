package com.mindlesstoys.stick404.nodecode.logicSystems.types;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;

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
