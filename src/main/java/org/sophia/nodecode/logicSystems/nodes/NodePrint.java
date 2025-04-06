package org.sophia.nodecode.logicSystems.nodes;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.core.NodeEnv;

import static org.sophia.nodecode.registries.DataTypeRegistry.TYPE_ANY;

public class NodePrint extends Node {

    public final static DataType[] inputTypes = new DataType[]{TYPE_ANY.get()};
    public final static DataType[] outputTypes = new DataType[]{};

    public NodePrint() {
        super();
    }

    public NodePrint(NodeEnv env) {
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
        DataType<?>[] data = super.run(inputs);

        System.out.println(inputs[0].getData());
        return data;
    }
}
