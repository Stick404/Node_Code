package org.sophia.nodecode.logicSystems.nodes;

import org.sophia.nodecode.logicSystems.core.DataType;
import org.sophia.nodecode.logicSystems.core.Node;
import org.sophia.nodecode.logicSystems.core.NodeEnv;

public class NodeInput extends Node {
    private DataType<?> type;
    private Object value;

    public NodeInput(){
        super();
    }

    public NodeInput(NodeEnv env, DataType<?> type, Object value) { //The init value to use
        super(env);
        this.type = type;
        this.value = value;
    }

    @Override
    public DataType[] getInputTypes() {
        return new DataType[0];
    }

    @Override
    public DataType[] getOutputTypes() {
        return new DataType[]{
            this.type
        };
    }

    @Override
    protected DataType<?>[] run(DataType<?>[] inputs) {
        try {
            var z = type.getClass().getConstructor().newInstance();
            z.setData(this.value);
            return new DataType[]{z};
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
