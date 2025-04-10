package org.sophia.nodecode.logicSystems.Funcs;

import org.jetbrains.annotations.Nullable;
import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.Func;
import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.types.TypeList;

import java.util.ArrayList;

import static org.sophia.nodecode.registries.DataTypeRegistry.*;
import static org.sophia.nodecode.registries.DataTypeRegistry.TYPE_LIST;

public class FuncGetList implements Func {
    public final static DataType<?>[] inputTypes = new DataType[]{TYPE_LIST.get(), TYPE_DOUBLE.get()};
    public final static DataType<?>[] outputTypes = new DataType[]{TYPE_LIST.get(), TYPE_ANY.get()};

    public DataType<?>[] getInputTypes() {
        return inputTypes;
    }

    public DataType<?>[] getOutputTypes() {
        return outputTypes;
    }

    public DataType<?>[] run(DataType<?>[] inputs, @Nullable DataType<?> extra) {
        ArrayList<DataType<?>> list = getSlot(inputs,0);
        Integer target = getSlot(inputs,1);
        if (target == null){
            var last = list.getLast();
            list.removeLast();
            return new DataType<?>[]{last,new TypeList(list)};
        }
        DataType<?> get = list.remove(target.intValue());
        return new DataType<?>[]{get,new TypeList(list)};
    }
}
