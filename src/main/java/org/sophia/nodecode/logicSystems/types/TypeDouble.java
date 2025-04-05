package org.sophia.nodecode.logicSystems.types;

import org.sophia.nodecode.logicSystems.core.DataType;

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
