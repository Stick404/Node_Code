package org.sophia.nodecode.logicSystems.nodes;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.types.TypeDouble;

import static org.sophia.nodecode.registries.DataTypeRegistry.TYPE_DOUBLE;

public class NodeAdd extends Node {

    public final static DataType[] inputTypes = new DataType[]{TYPE_DOUBLE.get(),TYPE_DOUBLE.get()};
    public final static DataType[] outputTypes = new DataType[]{TYPE_DOUBLE.get()};

    public NodeAdd() {
        super();
    }

    public NodeAdd(NodeEnv env) {
        super(env);
    }

    public DataType[] getInputTypes() {
        return inputTypes;
    }

    public DataType[] getOutputTypes() {
        return outputTypes;
    }

    @Override
    public DataType<?>[] run(DataType<?>[] inputs) {
        return new DataType<?>[]{new TypeDouble((Double) inputs[0].getData() + (Double) inputs[1].getData())};
    }
}
