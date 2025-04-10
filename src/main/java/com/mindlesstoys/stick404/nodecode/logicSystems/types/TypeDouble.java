package com.mindlesstoys.stick404.nodecode.logicSystems.types;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;

public class TypeDouble extends DataType<Double> {

    public TypeDouble(Double initVal) {
        super(Double.class, initVal);
    }

    public TypeDouble() {
        this(0.0);
    }

    @Override
    public int getColor() {
        return 0x2fb4db;
    }
}
