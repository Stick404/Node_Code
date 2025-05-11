package com.mindlesstoys.stick404.nodecode.logicSystems.Funcs;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DefaultColors;
import org.jetbrains.annotations.Nullable;
import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import com.mindlesstoys.stick404.nodecode.logicSystems.core.Func;

import static com.mindlesstoys.stick404.nodecode.registries.DataTypeRegistry.TYPE_ANY;

public class FuncPrint implements Func {
    public final static DataType<?>[] inputTypes = new DataType[]{TYPE_ANY.get()};
    public final static DataType<?>[] outputTypes = new DataType[]{};

    public DataType<?>[] getInputTypes() {
        return inputTypes;
    }

    public DataType<?>[] getOutputTypes() {
        return outputTypes;
    }

    public int getColor() {
        return DefaultColors.OUTPUT.color;
    }

    public DataType<?>[] run(DataType<?>[] inputs, @Nullable DataType<?> extra) {
        System.out.println(inputs[0].getData()); // not the best way, but works
        return new DataType<?>[0];
    }
}
