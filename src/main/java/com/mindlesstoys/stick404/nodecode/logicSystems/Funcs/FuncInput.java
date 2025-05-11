package com.mindlesstoys.stick404.nodecode.logicSystems.Funcs;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DefaultColors;
import org.jetbrains.annotations.Nullable;
import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import com.mindlesstoys.stick404.nodecode.logicSystems.core.Func;

public class FuncInput<T> implements Func {
    private final DataType<T> type;
    public final DataType<?>[] outputTypes;

    public FuncInput(DataType<T> type) { //The init value to use
        this.type = type;
        this.outputTypes = new DataType<?>[]{type};
    }

    public int getColor() {
        return DefaultColors.INPUT.color;
    }

    public DataType<?>[] getInputTypes() {
        return new DataType[0];
    }

    public DataType<?>[] getOutputTypes() {
        return outputTypes;
    }

    public DataType<?>[] run(DataType<?>[] inputs, @Nullable DataType<?> extra) {
        DataType<?> output;
        try {
            T data;
            if (extra == null){
                data = type.getData();
            } else {
                data = (T) extra.getData();
            }

            output = type.getClass().getConstructor(this.type.getType()).newInstance(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new DataType[]{output};
    }
}
