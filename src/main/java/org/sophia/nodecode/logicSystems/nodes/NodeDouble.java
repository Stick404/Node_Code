package org.sophia.nodecode.logicSystems.nodes;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.core.NodeEnv;
import org.sophia.nodecode.logicSystems.types.TypeDouble;

import static org.sophia.nodecode.registries.DataTypeRegistry.TYPE_DOUBLE;

public class NodeDouble extends Node {
    private final Double amount;
    public NodeDouble(NodeEnv env, Double amount) {
        super(env,
                new Class[0], //No input needed, so set to empty
                new Class[]{TYPE_DOUBLE}
            );
        this.amount = amount;
    }

    @Override
    public DataType<?>[] run() {
        DataType<?>[] data = super.run();

        data[0] = new TypeDouble(amount);
        return data;
    }
}
