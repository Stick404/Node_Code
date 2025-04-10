package com.mindlesstoys.stick404.nodecode.logicSystems.types;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;

public class TypeNull extends DataType<Void> {
    public TypeNull() {
        super(Void.class, null);
    }

    @Override
    public int getColor() {
        return 0x6a6a6a;
    }
}
