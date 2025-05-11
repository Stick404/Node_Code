package com.mindlesstoys.stick404.nodecode.logicSystems.Funcs;

import com.mindlesstoys.stick404.nodecode.logicSystems.core.DefaultColors;
import org.jetbrains.annotations.Nullable;
import com.mindlesstoys.stick404.nodecode.logicSystems.core.DataType;
import com.mindlesstoys.stick404.nodecode.logicSystems.core.Func;
import com.mindlesstoys.stick404.nodecode.logicSystems.types.TypeList;

import java.util.ArrayList;

import static com.mindlesstoys.stick404.nodecode.registries.DataTypeRegistry.*;

public class FuncAppendList implements Func {
    public final static DataType<?>[] inputTypes = new DataType[]{TYPE_LIST.get(), TYPE_ANY.get(), TYPE_DOUBLE.get()};
    public final static DataType<?>[] outputTypes = new DataType[]{TYPE_LIST.get()};

    public DataType<?>[] getInputTypes() {
        return inputTypes;
    }
    public DataType<?>[] getOutputTypes() {
        return outputTypes;
    }

    public int getColor() {
        return DefaultColors.LIST.color;
    }

    public DataType<?>[] run(DataType<?>[] inputs, @Nullable DataType<?> extra) {
        ArrayList<DataType<?>> list = getSlot(inputs,0);
        DataType<?> input = getSlot(inputs,1);
        //The best way to do an Any, since DataType<?> is the Object of the Type System
        Integer slot = getSlot(inputs,2);

        if (slot == null) {
            list.add(input);
        } else {
            list.set(slot,input);
        }


        return new DataType<?>[]{new TypeList(list)};
    }
}
