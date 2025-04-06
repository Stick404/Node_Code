package org.sophia.nodecode.logicSystems.nodes;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.core.NodeEnv;

import static org.sophia.nodecode.registries.DataTypeRegistry.TYPE_ANY;

public class NodePrint extends Node {

    public NodePrint() {
        super();
    }

    public NodePrint(NodeEnv env) {
        super(env);
    }

    @Override
    public DataType[] getInputTypes() {
        return new DataType[]{
                TYPE_ANY.get(),
        };
    }

    @Override
    public DataType[] getOutputTypes() {
        return super.getOutputTypes();
    }

    @Override
    public DataType<?>[] run() {
        DataType<?>[] data = super.run();
        System.out.println(get(0).getData());
        return data;
    }
}
