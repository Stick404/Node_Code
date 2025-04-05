package org.sophia.nodecode.logicSystems.nodes;

import range.main.core.DataType;
import range.main.core.Node;
import range.main.core.NodeEnv;
import range.main.types.TypeObject;

public class NodePrint extends Node {
    public NodePrint(NodeEnv env) {
        super(env,
                new Class[]{TypeObject.class},
                new Class[0] //No output needed, so set to empty

        );
    }

    @Override
    public DataType<?>[] run() {
        DataType<?>[] data = super.run();
        System.out.println(get(0).getData());
        return data;
    }
}
