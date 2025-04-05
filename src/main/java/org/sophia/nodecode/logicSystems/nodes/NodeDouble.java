package org.sophia.nodecode.logicSystems.nodes;

import range.main.core.DataType;
import range.main.core.Node;
import range.main.core.NodeEnv;
import range.main.types.TypeDouble;

import static range.main.core.registries.TypeRegistry.DOUBLE;

public class NodeDouble extends Node {
    private final Double amount;
    public NodeDouble(NodeEnv env, Double amount) {
        super(env,
                new Class[0], //No input needed, so set to empty
                new Class[]{DOUBLE}
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
