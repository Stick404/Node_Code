package org.sophia.nodecode.logicSystems.nodes;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.types.TypeList;

import java.util.ArrayList;

import static org.sophia.nodecode.registries.DataTypeRegistry.*;

public class NodeAppendList extends Node {
    public final static DataType[] inputTypes = new DataType[]{TYPE_LIST.get(), TYPE_ANY.get(), TYPE_DOUBLE.get()};
    public final static DataType[] outputTypes = new DataType[]{TYPE_LIST.get()};

    @Override
    public DataType<?>[] run(DataType<?>[] inputs) {
        var list = (ArrayList) inputs[0].getData();
        var input = (Object) inputs[1].getData();
        var slotTemp = inputs[2].getData();

        if (null == slotTemp) {
            list.add(input);
        } else {
            list.set(((Integer) inputs[2].getData()),input);
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
