package org.sophia.nodecode.logicSystems.nodes.inputs;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.core.NodeEnv;

public abstract class NodeInput<T> extends Node {
    private final DataType<T> type;
    private final T value;

    public NodeInput(){
        super();
        type = null;
        value = null;
    }

    public NodeInput(NodeEnv env, DataType<T> type, T value) { //The init value to use
        super(env);
        this.type = type;
        this.value = value;
    }

    public DataType[] getInputTypes() {
        return new DataType[0];
    }

    @Override
    protected DataType<?>[] run(DataType<?>[] inputs) {
        DataType output = null;
        try {
            output = type.getClass().getConstructor(this.type.getType()).newInstance(this.value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new DataType[]{output};
    }
}
