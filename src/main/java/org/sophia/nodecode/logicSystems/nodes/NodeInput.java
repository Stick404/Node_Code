package org.sophia.nodecode.logicSystems.nodes;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.core.NodeEnv;

public class NodeInput<T> extends Node {
    private Class<? extends DataType<T>> type;
    private T value;
    public NodeInput(NodeEnv env,
                     Class<? extends DataType<T>> type, //The type to use
                     T value) { //The init value to use
        super(env,
                new Class[0],
                new Class[]{value.getClass()}
        );
        this.type = type;
        this.value = value;
    }

    @Override
    protected DataType<?>[] run() {
        try {
            var z = type.getConstructor().newInstance();
            z.setData(this.value);
            System.out.println(z);
            return new DataType[]{z};
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
