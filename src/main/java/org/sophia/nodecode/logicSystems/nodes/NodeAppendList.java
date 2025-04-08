package org.sophia.nodecode.logicSystems.nodes;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.types.TypeList;

import java.util.ArrayList;

import static org.sophia.nodecode.registries.DataTypeRegistry.*;

public class NodeAppendList extends Node {
    public final static DataType<?>[] inputTypes = new DataType[]{TYPE_LIST.get(), TYPE_ANY.get(), TYPE_DOUBLE.get()};
    public final static DataType<?>[] outputTypes = new DataType[]{TYPE_LIST.get()};

    @Override
    public DataType<?>[] run(DataType<?>[] inputs) {
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

    public NodeAppendList() {
        super();
    }

    public NodeAppendList(NodeEnv env) {
        super(env);
    }

    public DataType[] getInputTypes() {
        return inputTypes;
    }

    public DataType[] getOutputTypes() {
        return outputTypes;
    }
}
