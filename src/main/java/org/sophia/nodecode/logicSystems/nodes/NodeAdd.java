package org.sophia.nodecode.logicSystems.nodes;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.types.TypeDouble;

import static org.sophia.nodecode.logicSystems.core.registries.TypeRegistry.DOUBLE;

public class NodeAdd extends Node {
    public NodeAdd(NodeEnv env) {
        super(env,
                new Class[]{DOUBLE,DOUBLE},
                new Class[]{DOUBLE}
        );
    }

    @Override
    public DataType<?>[] run() {
        DataType<?>[] data = super.run();

        data[0] = new TypeDouble((Double) get(0).getData() + (Double) get(1).getData());
        return data;
    }
}
